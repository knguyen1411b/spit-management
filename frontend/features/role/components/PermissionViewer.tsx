'use client'

import { Button, Modal, Table, Tag, Typography } from 'antd'
import { Fragment, useMemo, useState } from 'react'
import { EyeOutlined } from '@ant-design/icons'
import { usePermissions } from '@/hooks/data'
import type { IBasePremsDTO } from '@/types'

export default function PermissionViewer() {
  const { perms, isLoading } = usePermissions()
  const [open, setOpen] = useState(false)

  const columns = useMemo(
    () => [
      {
        key: 'code',
        title: 'Mã quyền',
        dataIndex: 'code',
        render: (code: string) => {
          if (code.includes('read')) return <Tag color="green">{code}</Tag>
          if (code.includes('create')) return <Tag color="blue">{code}</Tag>
          if (code.includes('update')) return <Tag color="magenta">{code}</Tag>
          if (code.includes('delete')) return <Tag color="red">{code}</Tag>
          return <Tag color="gold">{code}</Tag>
        }
      },
      {
        key: 'name',
        title: 'Tên quyền',
        dataIndex: 'name'
      },
      {
        key: 'description',
        title: 'Mô tả',
        dataIndex: 'description'
      }
    ],
    []
  )

  return (
    <Fragment>
      <Button type="primary" icon={<EyeOutlined />} onClick={() => setOpen(true)}>
        Xem danh sách quyền
      </Button>

      <Modal
        title={
          <Typography.Title level={2} style={{ margin: 0 }}>
            Danh sách quyền
          </Typography.Title>
        }
        centered
        width={900}
        open={open}
        footer={null}
        onCancel={() => setOpen(false)}
      >
        <Table<IBasePremsDTO>
          rowKey="id"
          size="middle"
          footer={() => (
            <p>
              Tổng cộng: <b>{perms.length || 0}</b> quyền
            </p>
          )}
          scroll={{ x: 800 }}
          loading={isLoading}
          dataSource={perms}
          columns={columns}
          pagination={{ pageSize: 10 }}
        />
      </Modal>
    </Fragment>
  )
}
