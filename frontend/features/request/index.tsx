'use client'

import { Tabs, Table, Tag, Button, Space } from 'antd'
import type { ColumnsType } from 'antd/es/table'
import { useTaskRequests } from '@/hooks/data'
import type { ICheckRequest } from '@/types'
import LoadingScreen from '@/app/loading'
import dayjs from 'dayjs'

export default function RequestMain() {
  const { requests, isLoading, approveRequest, rejectRequest } = useTaskRequests()

  if (isLoading) return <LoadingScreen />

  const columns: ColumnsType<ICheckRequest> = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80
    },
    {
      title: 'Tên thành viên',
      dataIndex: 'memberName',
      render: name => name
    },
    {
      title: 'Tên công việc',
      dataIndex: 'taskTitle',
      render: name => name || '—'
    },
    {
      title: 'Mô tả',
      dataIndex: 'description',
      render: d => d || '—'
    },
    {
      title: 'Ngày diễn ra',
      dataIndex: 'taskDate',
      render: d => dayjs(d).format('DD/MM/YYYY HH:mm')
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      render: v => {
        let color = 'default'
        let text = 'Chờ duyệt'

        if (v === 'APPROVED') {
          color = 'green'
          text = 'Đã duyệt'
        } else if (v === 'REJECTED') {
          color = 'red'
          text = 'Đã từ chối'
        }

        return <Tag color={color}>{text}</Tag>
      }
    },
    {
      title: 'Hành động',
      key: 'action',
      render: (_, r) => (
        <Space>
          {r.status !== 'APPROVED' && (
            <Button type="primary" size="small" onClick={() => approveRequest(r.id)}>
              Duyệt
            </Button>
          )}
          {r.status !== 'REJECTED' && (
            <Button danger size="small" onClick={() => rejectRequest(r.id)}>
              Từ chối
            </Button>
          )}
        </Space>
      )
    }
  ]

  return (
    <Tabs
      type="card"
      defaultActiveKey="pending"
      items={[
        {
          key: 'pending',
          label: (
            <span className="font-medium">
              Chờ duyệt <span className="text-orange-500">({requests.pending.length})</span>
            </span>
          ),
          children: (
            <Table
              rowKey="id"
              columns={columns}
              dataSource={requests.pending}
              pagination={{ pageSize: 10 }}
              scroll={{ x: 'max-content' }}
              footer={() => (
                <div style={{ padding: '8px 4px', fontSize: 13 }}>
                  Tổng cộng: <b>{requests.pending.length || 0}</b> yêu cầu
                </div>
              )}
              size="middle"
            />
          )
        },
        {
          key: 'approved',
          label: (
            <span className="font-medium">
              Đã duyệt <span className="text-green-600">({requests.approved.length})</span>
            </span>
          ),
          children: (
            <Table
              rowKey="id"
              columns={columns}
              dataSource={requests.approved}
              pagination={{ pageSize: 10 }}
              scroll={{ x: 'max-content' }}
              footer={() => (
                <div style={{ padding: '8px 4px', fontSize: 13 }}>
                  Tổng cộng: <b>{requests.approved.length || 0}</b> yêu cầu
                </div>
              )}
              size="middle"
            />
          )
        },
        {
          key: 'rejected',
          label: (
            <span className="font-medium">
              Từ chối <span className="text-red-500">({requests.rejected.length})</span>
            </span>
          ),
          children: (
            <Table
              rowKey="id"
              columns={columns}
              dataSource={requests.rejected}
              pagination={{ pageSize: 10 }}
              scroll={{ x: 'max-content' }}
              footer={() => (
                <div style={{ padding: '8px 4px', fontSize: 13 }}>
                  Tổng cộng: <b>{requests.rejected.length || 0}</b> yêu cầu
                </div>
              )}
            />
          )
        }
      ]}
    />
  )
}
