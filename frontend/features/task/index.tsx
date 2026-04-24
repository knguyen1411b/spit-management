'use client'

import { Button, Divider, Flex, Input, Table, TableProps, Tag, Tooltip } from 'antd'
import { EyeOutlined, SearchOutlined } from '@ant-design/icons'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { Permissions } from '@/constants/permission'
import { parseAsInteger, useQueryState } from 'nuqs'
import { Heading } from '@/components/ui/heading'
import TaskDelete from './components/TaskDelete'
import TaskCreate from './components/TaskCreate'
import { useRouter } from 'next/navigation'
import { useTasks } from '@/hooks/data'
import { useAuth } from '@/contexts'
import { ITask } from '@/types'
import dayjs from 'dayjs'

export default function TaskMain() {
  const { hasPermission } = useAuth()
  const isMobile = useIsMobile()
  const { push } = useRouter()

  const [page, setPage] = useQueryState('page', parseAsInteger.withDefault(1))
  const [size, setSize] = useQueryState('size', parseAsInteger.withDefault(10))
  const [sort, setSort] = useQueryState('sort', { defaultValue: '' })

  const { tasks, meta, isLoading } = useTasks({
    page,
    size,
    sort
  })

  const columns: TableProps<ITask>['columns'] = [
    {
      title: 'Tên công việc',
      dataIndex: 'title',
      key: 'title',
      sorter: true,
      render: (v: string) => <b>{v}</b>
    },
    {
      title: 'Mô tả',
      dataIndex: 'description',
      key: 'description'
    },
    {
      title: 'Loại',
      dataIndex: 'type',
      key: 'type',
      sorter: true,
      render: (type: string) => {
        if (type.includes('EVENT')) return <Tag color="green">Sự kiện</Tag>
        if (type.includes('TASK')) return <Tag color="blue">Công việc</Tag>
        return <Tag color="default">Khác</Tag>
      }
    },
    {
      title: 'Thời gian',
      dataIndex: 'date',
      sorter: true,
      key: 'date',
      render: d => dayjs(d).format('HH:mm DD/MM/YYYY')
    },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, b) => (
        <Flex gap={8}>
          <Tooltip title="Xem chi tiết ban">
            <Button type="primary" size="small" icon={<EyeOutlined />} onClick={() => push(`/task/${b.id}`)} />
          </Tooltip>
          {hasPermission([Permissions.Task.DELETE]) && <TaskDelete id={b.id} name={b.title} />}
        </Flex>
      )
    }
  ]

  return (
    <Flex vertical gap={10}>
      <Flex justify="space-between" align="center" wrap={isMobile}>
        <Heading title="Quản lý công việc" description="Quản lý các công việc trong kỳ" />
        <Flex gap={10}>{hasPermission([Permissions.Task.CREATE]) && <TaskCreate />}</Flex>
      </Flex>

      <Divider className="my-1!" />

      <Input
        prefix={<SearchOutlined style={{ margin: '0 5px' }} />}
        placeholder="Tìm kiếm kì học..."
        className="mb-1!"
      />
      <Table<ITask>
        rowKey="id"
        scroll={{ x: 900 }}
        columns={columns}
        dataSource={tasks.map(t => ({ ...t, key: t.id }))}
        loading={isLoading}
        footer={() => (
          <p>
            Tổng cộng: <b>{meta?.total || 0}</b> công việc
          </p>
        )}
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
