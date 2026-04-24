'use client'

import { Button, Divider, Flex, Input, Table, TableProps, Tag, Tooltip } from 'antd'
import { SearchOutlined, EyeOutlined } from '@ant-design/icons'
import SemesterDelete from './components/SemesterDelete'
import SemesterCreate from './components/SemesterCreate'
import { useDebounce } from '@/hooks/ui/use-debounce'
import SemesterCopy from './components/SemesterCopy'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { Permissions } from '@/constants/permission'
import { parseAsInteger, useQueryState } from 'nuqs'
import { Heading } from '@/components/ui/heading'
import { useRouter } from 'next/navigation'
import { useSemesters } from '@/hooks/data'
import { useAuth } from '@/contexts'
import { ISemester } from '@/types'
import { useMemo } from 'react'
import dayjs from 'dayjs'

export default function SemesterMain() {
  const isMobile = useIsMobile()
  const { hasPermission } = useAuth()
  const { push } = useRouter()
  const [page, setPage] = useQueryState('page', parseAsInteger.withDefault(1))
  const [size, setSize] = useQueryState('size', parseAsInteger.withDefault(10))
  const [keyword, setKeyword] = useQueryState('keyword', { defaultValue: '' })
  const [sort, setSort] = useQueryState('sort', { defaultValue: '' })

  const debouncedSearch = useDebounce(keyword, 500)

  const { semesters, meta, isLoading } = useSemesters({
    page,
    size,
    keyword: debouncedSearch,
    sort
  })

  const columns: TableProps<ISemester>['columns'] = useMemo(
    () => [
      {
        title: 'Mã kì học',
        dataIndex: 'code',
        key: 'code',
        sorter: true,
        render: code => <span className="font-medium">{code}</span>
      },
      {
        title: 'Tên kì học',
        dataIndex: 'name',
        key: 'name',
        sorter: true
      },
      {
        title: 'Ngày bắt đầu',
        dataIndex: 'startDate',
        key: 'startDate',
        render: date => (date ? dayjs(date).format('DD/MM/YYYY') : '--')
      },
      {
        title: 'Ngày kết thúc',
        dataIndex: 'endDate',
        key: 'endDate',
        render: date => (date ? dayjs(date).format('DD/MM/YYYY') : '--')
      },
      {
        title: 'Kì thứ',
        dataIndex: 'semesterOrder',
        key: 'semesterOrder'
      },
      {
        title: 'Kì hiện tại',
        dataIndex: 'current',
        key: 'current',
        sorter: true,
        render: current => (current ? <Tag color="green">Có</Tag> : <Tag color="default">Không</Tag>)
      },
      {
        title: 'Hành động',
        key: 'action',
        render: (_, item) => (
          <Flex gap={8}>
            <Tooltip title="Xem chi tiết kì học">
              <Button type="primary" size="small" icon={<EyeOutlined />} onClick={() => push(`/semester/${item.id}`)} />
            </Tooltip>
            {hasPermission([Permissions.Semester.DELETE]) && <SemesterDelete id={item.id} code={item.code} />}
          </Flex>
        )
      }
    ],
    [push, hasPermission]
  )

  return (
    <Flex vertical gap={10}>
      <Flex justify="space-between" align="center" wrap={isMobile}>
        <Heading title="Quản lý kì học" description="Quản lý các kì học trong hệ thống" />
        <Flex gap={10}>
          {hasPermission([Permissions.Semester.CREATE]) && (
            <>
              <SemesterCopy /> <SemesterCreate />
            </>
          )}
        </Flex>
      </Flex>

      <Divider className="my-1!" />

      <Input
        prefix={<SearchOutlined style={{ margin: '0 5px' }} />}
        placeholder="Tìm kiếm kì học..."
        value={keyword}
        className="mb-1!"
        onChange={e => setKeyword(e.target.value)}
      />

      <Table<ISemester>
        rowKey="id"
        scroll={{ x: 900 }}
        columns={columns}
        dataSource={semesters.map(se => ({ ...se, key: se.id }))}
        showSorterTooltip={{ title: 'Bấm để sắp xếp' }}
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
        footer={() => (
          <div style={{ padding: '8px 4px', fontSize: 13 }}>
            Tổng cộng: <b>{meta?.total || 0}</b> kì học
          </div>
        )}
      />
    </Flex>
  )
}
