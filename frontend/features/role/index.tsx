'use client'

import { Button, Divider, Flex, Input, Table, TableProps, Tooltip } from 'antd'
import { SearchOutlined, EyeOutlined } from '@ant-design/icons'
import PermissionViewer from './components/PermissionViewer'
import { useDebounce } from '@/hooks/ui/use-debounce'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { Permissions } from '@/constants/permission'
import { parseAsInteger, useQueryState } from 'nuqs'
import { Heading } from '@/components/ui/heading'
import RoleDelete from './components/RoleDelete'
import RoleCreate from './components/RoleCreate'
import { useRouter } from 'next/navigation'
import { useRoles } from '@/hooks/data'
import { IBasePremsDTO } from '@/types'
import { useAuth } from '@/contexts'
import { useMemo } from 'react'

export default function RoleMain() {
  const isMobile = useIsMobile()
  const { hasPermission } = useAuth()
  const { push } = useRouter()
  const [page, setPage] = useQueryState('page', parseAsInteger.withDefault(1))
  const [size, setSize] = useQueryState('size', parseAsInteger.withDefault(10))
  const [keyword, setKeyword] = useQueryState('keyword', { defaultValue: '' })
  const [sort, setSort] = useQueryState('sort', { defaultValue: '' })

  const debouncedSearch = useDebounce(keyword, 500)

  const { roles, meta, isLoading } = useRoles({
    page,
    size,
    keyword: debouncedSearch,
    sort
  })

  const columns: TableProps<IBasePremsDTO>['columns'] = useMemo(
    () => [
      {
        title: 'Mã chức vụ',
        dataIndex: 'code',
        key: 'code',
        sorter: true,
        render: (_, record) => <span className="font-medium">{record.code}</span>
      },
      {
        title: 'Tên chức vụ',
        dataIndex: 'name',
        key: 'name',
        sorter: true
      },
      {
        title: 'Mô tả chức vụ',
        dataIndex: 'description',
        key: 'description'
      },
      {
        title: 'Hành động',
        key: 'action',
        render: (_, i) => (
          <Flex gap={8}>
            <Tooltip title="Xem chi tiết chức vụ">
              <Button type="primary" size="small" icon={<EyeOutlined />} onClick={() => push(`/role/${i.id}`)} />
            </Tooltip>
            {hasPermission([Permissions.Role.DELETE]) && <RoleDelete id={i.id} code={i.code} />}
          </Flex>
        )
      }
    ],
    [hasPermission, push]
  )

  return (
    <Flex vertical gap={10}>
      <Flex justify="space-between" align="center" wrap={isMobile}>
        <Heading title="Quản lý chức vụ" description="Quản lý các chức vụ trong hệ thống" />
        <Flex gap={10}>
          {hasPermission([Permissions.Permission.READ]) && <PermissionViewer />}
          {hasPermission([Permissions.Role.CREATE]) && <RoleCreate />}
        </Flex>
      </Flex>

      <Divider className="my-1!" />

      <Input
        prefix={<SearchOutlined style={{ margin: '0 5px' }} />}
        placeholder="Tìm kiếm chức vụ..."
        value={keyword}
        className="mb-1!"
        onChange={e => setKeyword(e.target.value)}
      />

      <Table<IBasePremsDTO>
        rowKey="id"
        scroll={{ x: 800 }}
        columns={columns}
        dataSource={roles.map(role => ({ ...role, key: role.id }))}
        showSorterTooltip={{ title: 'Bấm để sắp xếp' }}
        footer={() => (
          <p>
            Tổng cộng: <b>{meta?.total || 0}</b> chức vụ
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
        onChange={(_, __, sorter: any) => {
          if (sorter.order) {
            setSort(`${sorter.field},${sorter.order === 'ascend' ? 'asc' : 'desc'}`)
          } else {
            setSort('')
          }
        }}
      />
    </Flex>
  )
}
