'use client'

import { Avatar, Button, Divider, Flex, Input, Table, TableProps, Tag, Tooltip } from 'antd'
import { SearchOutlined, EyeOutlined, UserOutlined } from '@ant-design/icons'
import { useDebounce } from '@/hooks/ui/use-debounce'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { Permissions } from '@/constants/permission'
import { parseAsInteger, useQueryState } from 'nuqs'
import { Heading } from '@/components/ui/heading'
import UserDelete from './components/UserDelete'
import UserCreate from './components/UserCreate'
import { useRouter } from 'next/navigation'
import { useUsers } from '@/hooks/data'
import { useAuth } from '@/contexts'
import { IUser } from '@/types'
import { useMemo } from 'react'
import dayjs from 'dayjs'

export default function UserMain() {
  const isMobile = useIsMobile()
  const { hasPermission } = useAuth()
  const { push } = useRouter()
  const [page, setPage] = useQueryState('page', parseAsInteger.withDefault(1))
  const [size, setSize] = useQueryState('size', parseAsInteger.withDefault(10))
  const [keyword, setKeyword] = useQueryState('keyword', { defaultValue: '' })
  const [sort, setSort] = useQueryState('sort', { defaultValue: '' })
  const [enabled, setEnabled] = useQueryState('enabled', { defaultValue: '' })

  const debouncedSearch = useDebounce(keyword, 500)

  const { users, meta, isLoading } = useUsers({
    enabled: enabled === '' ? undefined : enabled === 'true',
    page,
    size,
    keyword: debouncedSearch,
    sort
  })

  const columns: TableProps<IUser>['columns'] = useMemo(
    () => [
      {
        title: 'Tên người dùng',
        dataIndex: 'username',
        key: 'username',
        width: 400,
        sorter: true,
        render: (_, record) => (
          <Flex align="center" gap={12}>
            <Avatar alt={record.username} icon={<UserOutlined />} />
            <span className="font-medium">{record.username}</span>
          </Flex>
        )
      },
      {
        title: 'Thời gian tạo',
        dataIndex: 'createdAt',
        key: 'createdAt',
        render: (_, record) => dayjs(record?.createdAt).format('DD/MM/YYYY HH:mm') || '--'
      },
      {
        title: 'Thời gian cập nhật',
        dataIndex: 'updatedAt',
        key: 'updatedAt',
        render: (_, record) => dayjs(record?.createdAt).format('DD/MM/YYYY HH:mm') || '--'
      },
      {
        title: 'Trạng thái',
        dataIndex: 'enabled',
        key: 'enabled',
        filters: [
          { text: 'Kích hoạt', value: 'true' },
          { text: 'Vô hiệu hoá', value: 'false' }
        ],
        filterMultiple: false,
        render: enabled => (enabled ? <Tag color="green">Kích hoạt</Tag> : <Tag color="red">Vô hiệu hoá</Tag>)
      },
      {
        title: 'Hành động',
        key: 'action',
        render: (_, i) => (
          <Flex gap={8}>
            <Tooltip title="Xem chi tiết chức vụ">
              <Button type="primary" size="small" icon={<EyeOutlined />} onClick={() => push(`/user/${i.id}`)} />
            </Tooltip>
            {hasPermission([Permissions.Role.DELETE]) && (
              <UserDelete id={i.id} username={i.username} superuser={i.superuser} />
            )}
          </Flex>
        )
      }
    ],
    [push, hasPermission]
  )

  return (
    <Flex vertical gap={10}>
      <Flex justify="space-between" align="center" wrap={isMobile}>
        <Heading title="Quản lý người dùng" description="Quản lý các người dùng trong hệ thống" />
        <Flex gap={10}>{hasPermission([Permissions.User.CREATE]) && <UserCreate />}</Flex>
      </Flex>

      <Divider className="my-1!" />

      <Input
        prefix={<SearchOutlined style={{ margin: '0 5px' }} />}
        placeholder="Tìm kiếm người dùng..."
        value={keyword}
        className="mb-1!"
        onChange={e => setKeyword(e.target.value)}
      />

      <Table<IUser>
        rowKey="id"
        scroll={{ x: 800 }}
        columns={columns}
        dataSource={users.map(user => ({ ...user, key: user.id }))}
        showSorterTooltip={{ title: 'Bấm để sắp xếp' }}
        footer={() => (
          <p>
            Tổng cộng: <b>{meta?.total || 0}</b> người dùng
          </p>
        )}
        loading={isLoading}
        pagination={{
          current: page,
          pageSize: size,
          total: meta?.total,
          showSizeChanger: true,
          onChange: (p, s) => {
            setPage(p)
            setSize(s)
          }
        }}
        onChange={(_, filters: any, sorter: any) => {
          if (sorter?.order) {
            setSort(`${sorter.field},${sorter.order === 'ascend' ? 'asc' : 'desc'}`)
          } else {
            setSort('')
          }

          const filterVal = filters.enabled?.[0]

          if (filterVal === 'true' || filterVal === 'false') {
            setEnabled(filterVal)
          } else {
            setEnabled('')
          }
        }}
      />
    </Flex>
  )
}
