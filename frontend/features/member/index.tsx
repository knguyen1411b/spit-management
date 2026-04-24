'use client'

import {
  Avatar,
  Button,
  Checkbox,
  Divider,
  Dropdown,
  Flex,
  Input,
  Select,
  Table,
  TableColumnType,
  TableProps,
  Tag,
  Tooltip
} from 'antd'
import { SearchOutlined, EyeOutlined, FilterOutlined, ClusterOutlined, UserOutlined } from '@ant-design/icons'
import { useDebounce } from '@/hooks/ui/use-debounce'
import MemberDelete from './components/MemberDelete'
import MemberCreate from './components/MemberCreate'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { Permissions } from '@/constants/permission'
import { parseAsInteger, useQueryState } from 'nuqs'
import { Heading } from '@/components/ui/heading'
import { useRouter } from 'next/navigation'
import { useMembers } from '@/hooks/data'
import { useMemo, useState } from 'react'
import { useAuth } from '@/contexts'
import { IMember } from '@/types'
import dayjs from 'dayjs'

export default function MemberMain() {
  const { push } = useRouter()
  const isMobile = useIsMobile()
  const { hasPermission, profile } = useAuth()
  const [page, setPage] = useQueryState('page', parseAsInteger.withDefault(1))
  const [size, setSize] = useQueryState('size', parseAsInteger.withDefault(10))
  const [sort, setSort] = useQueryState('sort', { defaultValue: '' })
  const [fullname, setFullname] = useQueryState('fullName', { defaultValue: '' })
  const [gender, setGender] = useQueryState('gender', { defaultValue: '' })

  const [email, setEmail] = useQueryState('email', { defaultValue: '' })
  const [phone, setPhone] = useQueryState('phone', { defaultValue: '' })
  const [className, setClassName] = useQueryState('className', { defaultValue: '' })
  const [generation, setGeneration] = useQueryState('generation', { defaultValue: '' })
  const [username, setUsername] = useQueryState('username', { defaultValue: '' })
  const [description, setDescription] = useQueryState('description', { defaultValue: '' })

  const debouncedSearch = useDebounce(fullname, 500)

  const [currentSemester, setCurrentSemester] = useState<boolean>(true)

  const { members, meta, isLoading } = useMembers({
    semesterId: currentSemester ? profile?.semesterId : undefined,
    email,
    phone,
    className,
    generation,
    username,
    description,
    gender,
    fullName: debouncedSearch,
    page,
    size,
    sort
  })

  const getColumnSearchProps = (): TableColumnType<IMember> => ({
    filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
      <Flex onKeyDown={e => e.stopPropagation()} className="flex-col! p-2! gap-2!">
        <Input
          value={(selectedKeys[0] as string) || ''}
          onChange={e => {
            const val = e.target.value
            setSelectedKeys(val ? [val] : [])
          }}
          className="rounded-lg!"
          placeholder="Tìm kiếm..."
          onPressEnter={() => confirm()}
        />
        <Flex justify="end" gap={8}>
          <Button
            size="small"
            onClick={() => {
              clearFilters?.()
              confirm()
            }}
          >
            Đặt lại
          </Button>
          <Button type="primary" size="small" onClick={() => confirm()}>
            Tìm kiếm
          </Button>
        </Flex>
      </Flex>
    ),
    filterIcon: (filtered: boolean) => <SearchOutlined style={{ color: filtered ? '#1677ff' : undefined }} />
  })

  const allColumns: TableProps<IMember>['columns'] = useMemo(
    () => [
      {
        title: 'Tên đầy đủ',
        key: 'fullName',
        render: (_, m) => (
          <Flex gap={8} align="center">
            <Avatar src={m?.avatar || undefined} icon={<UserOutlined />} />
            <strong>
              {m.lastName} {m.firstName}
            </strong>
          </Flex>
        )
      },
      {
        title: 'Email',
        sorter: true,
        dataIndex: 'email',
        key: 'email',
        ...getColumnSearchProps()
      },
      {
        title: 'Số điện thoại',
        dataIndex: 'phone',
        key: 'phone',
        ...getColumnSearchProps()
      },
      {
        title: 'Giới tính',
        dataIndex: 'gender',
        key: 'gender',
        filters: [
          { text: 'Nam', value: 'true' },
          { text: 'Nữ', value: 'false' }
        ],
        sorter: true,
        filterMultiple: false,
        render: g => (g ? <Tag color="blue">Nam</Tag> : <Tag color="magenta">Nữ</Tag>)
      },
      {
        title: 'Tên người dùng',
        dataIndex: 'username',
        key: 'username',
        sorter: true,
        ...getColumnSearchProps()
      },
      {
        title: 'Ngày sinh',
        dataIndex: 'birthday',
        key: 'birthday',
        render: d => dayjs(d).format('DD/MM/YYYY')
      },
      {
        title: 'Lớp',
        dataIndex: 'className',
        key: 'className',
        sorter: true,
        ...getColumnSearchProps()
      },
      {
        title: 'Mô tả',
        dataIndex: 'description',
        key: 'description',
        ...getColumnSearchProps()
      },
      {
        title: 'Thế hệ',
        dataIndex: 'generation',
        key: 'generation',
        sorter: true,
        ...getColumnSearchProps()
      },
      {
        title: 'Hành động',
        key: 'action',
        render: (_, m) => (
          <Flex gap={8}>
            <Tooltip title="Xem chi tiết thành viên">
              <Button type="primary" size="small" icon={<EyeOutlined />} onClick={() => push(`/member/${m.id}`)} />
            </Tooltip>
            {hasPermission([Permissions.Member.DELETE]) && (
              <MemberDelete id={m.id} fullname={`${m.lastName} ${m.firstName}`} />
            )}
          </Flex>
        )
      }
    ],
    [push, hasPermission]
  )

  const [visibleColumns, setVisibleColumns] = useState<string[]>(
    allColumns
      .filter(c => c.key && !['', 'generation', 'description'].includes(c.key as string))
      .map(c => c.key as string)
  )

  return (
    <>
      <Flex vertical gap={10}>
        <Flex justify="space-between" align="center" wrap={isMobile}>
          <Heading title="Quản lý thành viên" description="Danh sách thành viên trong hệ thống" />
          {hasPermission([Permissions.Member.CREATE]) && <MemberCreate />}
        </Flex>

        <Divider className="my-2!" />

        <Flex gap={10} className="mb-2!">
          <Input
            placeholder="Tìm kiếm theo tên..."
            prefix={<SearchOutlined />}
            value={fullname}
            onChange={e => setFullname(e.target.value)}
            allowClear
          />
          <Select
            style={{ width: 150 }}
            prefix={<ClusterOutlined />}
            options={[
              { value: true, label: 'Kì hiện tại' },
              { value: false, label: 'Tất cả' }
            ]}
            value={currentSemester}
            onChange={setCurrentSemester}
          />
          <Dropdown
            trigger={['click']}
            menu={{
              items: allColumns.map(col => ({
                key: col.key as string,
                label: (
                  <Checkbox
                    checked={visibleColumns.includes(col.key as string)}
                    onChange={e => {
                      setVisibleColumns(prev => {
                        if (e.target.checked) {
                          return [...prev, col.key as string]
                        } else {
                          return prev.filter(c => c !== col.key)
                        }
                      })
                    }}
                  >
                    {typeof col.title === 'function' ? col.title({}) : col.title}
                  </Checkbox>
                )
              }))
            }}
          >
            <Button icon={<FilterOutlined />} type="primary">
              Hiển thị cột
            </Button>
          </Dropdown>
        </Flex>

        <Table<IMember>
          rowKey="id"
          scroll={{ x: 1000 }}
          columns={allColumns.filter(col => visibleColumns.includes(col.key as string))}
          dataSource={members.map(m => ({ ...m, key: m.id }))}
          loading={isLoading}
          footer={() => (
            <p>
              Tổng cộng: <b>{meta?.total || 0}</b> thành viên
            </p>
          )}
          showSorterTooltip={{ title: 'Bấm để sắp xếp' }}
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
            if (sorter.order) {
              setSort(`${sorter.field},${sorter.order === 'ascend' ? 'asc' : 'desc'}`)
            } else {
              setSort('')
            }

            const filtersMap: Record<string, (val: string) => void> = {
              gender: setGender,
              email: setEmail,
              phone: setPhone,
              className: setClassName,
              generation: setGeneration,
              username: setUsername,
              description: setDescription
            }

            Object.keys(filtersMap).forEach(key => {
              const val = filters[key]?.[0]
              if (val) {
                filtersMap[key](val)
              } else {
                filtersMap[key]('')
              }
            })
          }}
        />
      </Flex>
    </>
  )
}
