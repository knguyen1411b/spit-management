'use client'

import { Button, Divider, Flex, Input, Table, TableProps, Tooltip } from 'antd'
import { SearchOutlined, EyeOutlined } from '@ant-design/icons'
import { useDebounce } from '@/hooks/ui/use-debounce'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { Permissions } from '@/constants/permission'
import { parseAsInteger, useQueryState } from 'nuqs'
import BoardDelete from './components/BoardDelete'
import BoardCreate from './components/BoardCreate'
import { Heading } from '@/components/ui/heading'
import { useRouter } from 'next/navigation'
import { useBoards } from '@/hooks/data'
import { useAuth } from '@/contexts'
import { IBoard } from '@/types'
import dayjs from 'dayjs'

export default function BoardMain() {
  const { push } = useRouter()
  const isMobile = useIsMobile()
  const { hasPermission } = useAuth()

  const [page, setPage] = useQueryState('page', parseAsInteger.withDefault(1))
  const [size, setSize] = useQueryState('size', parseAsInteger.withDefault(10))
  const [keyword, setKeyword] = useQueryState('keyword', { defaultValue: '' })
  const [sort, setSort] = useQueryState('sort', { defaultValue: '' })

  const debouncedSearch = useDebounce(keyword, 500)

  const { boards, meta, isLoading } = useBoards({
    page,
    size,
    keyword: debouncedSearch,
    sort
  })

  const columns: TableProps<IBoard>['columns'] = [
    {
      title: 'Tên ban',
      dataIndex: 'name',
      key: 'name',
      sorter: true,
      render: (v: string) => <b>{v}</b>
    },
    {
      title: 'Mô tả',
      dataIndex: 'description',
      key: 'description'
    },
    {
      title: 'Ngày tạo',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: d => dayjs(d).format('DD/MM/YYYY')
    },
    {
      title: 'Ngày cập nhật',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      render: d => dayjs(d).format('DD/MM/YYYY')
    },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, b) => (
        <Flex gap={8}>
          <Tooltip title="Xem chi tiết ban">
            <Button type="primary" size="small" icon={<EyeOutlined />} onClick={() => push(`/board/${b.id}`)} />
          </Tooltip>
          {hasPermission([Permissions.Board.DELETE]) && <BoardDelete id={b.id} name={b.name} />}
        </Flex>
      )
    }
  ]

  return (
    <Flex vertical gap={10}>
      <Flex justify="space-between" align="center" wrap={isMobile}>
        <Heading title="Quản lý Ban" description="Danh sách các ban trong hệ thống" />
        {hasPermission([Permissions.Board.CREATE]) && <BoardCreate />}
      </Flex>

      <Divider className="my-1!" />

      <Input
        prefix={<SearchOutlined style={{ margin: '0 5px' }} />}
        placeholder="Tìm kiếm ban..."
        value={keyword}
        className="mb-1!"
        onChange={e => setKeyword(e.target.value)}
      />

      <Table<IBoard>
        rowKey="id"
        scroll={{ x: 900 }}
        columns={columns}
        dataSource={boards.map(b => ({ ...b, key: b.id }))}
        loading={isLoading}
        footer={() => (
          <p>
            Tổng cộng: <b>{meta?.total || 0}</b> ban
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
