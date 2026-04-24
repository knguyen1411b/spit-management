'use client'

import {
  ArrowLeftOutlined,
  HistoryOutlined,
  IdcardOutlined,
  InfoCircleOutlined,
  UnorderedListOutlined,
  UserOutlined
} from '@ant-design/icons'
import { Avatar, Button, Divider, Flex, Table, Tag, Typography } from 'antd'
import { notFound, useRouter } from 'next/navigation'
import { Permissions } from '@/constants/permission'
import { Heading } from '@/components/ui/heading'
import Title from 'antd/es/typography/Title'
import LoadingScreen from '@/app/loading'
import { Fragment, useMemo } from 'react'
import { useUsers } from '@/hooks/data'
import { IBasePremsDTO } from '@/types'
import UserUpdate from './UserUpdate'
import { useAuth } from '@/contexts'
import dayjs from 'dayjs'

export default function UserDetail({ id }: { id: number }) {
  const { hasPermission } = useAuth()
  const { back } = useRouter()
  const { data: userData, isLoading } = useUsers().useUserDetail(id)

  const data = userData?.data
  const roles = data?.roles || []

  if (userData?.statusCode === 404) {
    notFound()
  }

  const columns = useMemo(
    () => [
      {
        key: 'code',
        title: 'Mã quyền',
        dataIndex: 'code',
        render: (code: string) => {
          const color = code.includes('read')
            ? 'green'
            : code.includes('create')
              ? 'blue'
              : code.includes('update')
                ? 'magenta'
                : code.includes('delete')
                  ? 'red'
                  : 'gold'

          return <Tag color={color}>{code}</Tag>
        }
      },
      {
        key: 'name',
        title: 'Tên quyền',
        dataIndex: 'name',
        render: (text: string) => <b>{text}</b>
      },
      {
        key: 'description',
        title: 'Mô tả',
        dataIndex: 'description'
      }
    ],
    []
  )

  if (isLoading) {
    return <LoadingScreen />
  }

  return (
    <Fragment>
      <Heading title="Chi tiết người dùng" description="Xem thông tin chi tiết của người dùng" />
      <Divider />
      <Flex align="center" gap={16} style={{ marginBottom: 20 }}>
        <Avatar icon={<UserOutlined />} size={64} />
        <div>
          <Title level={2} style={{ marginBottom: 0 }}>
            {data?.username}
          </Title>
          <Typography.Text type="secondary">Chi tiết người dùng trong hệ thống</Typography.Text>
        </div>
      </Flex>
      <Flex vertical gap={14}>
        <Flex gap={8} align="center">
          <IdcardOutlined />
          <Typography.Text strong>Tên đăng nhập:</Typography.Text>
          <Typography.Text>{data?.username}</Typography.Text>
        </Flex>

        <Flex gap={8} align="center">
          <InfoCircleOutlined />
          <Typography.Text strong>Trạng thái:</Typography.Text>
          {data?.enabled ? <Tag color="green">Kích hoạt</Tag> : <Tag color="red">Vô hiệu hoá</Tag>}
        </Flex>

        {data?.superuser && (
          <Flex gap={8} align="center">
            <UserOutlined />
            <Typography.Text strong>Quyền hệ thống:</Typography.Text>
            <Tag color="red">Người dùng quản trị</Tag>
          </Flex>
        )}

        <Flex gap={8} align="center">
          <HistoryOutlined />
          <Typography.Text strong>Ngày tạo:</Typography.Text>
          {dayjs(data?.createdAt).format('DD/MM/YYYY HH:mm')}
        </Flex>

        <Flex gap={8} align="center">
          <HistoryOutlined />
          <Typography.Text strong>Ngày cập nhật:</Typography.Text>
          {dayjs(data?.updatedAt).format('DD/MM/YYYY HH:mm')}
        </Flex>
      </Flex>
      <Divider />
      <Typography.Text strong>
        <Flex gap={6} align="center" className="mb-5!">
          <UnorderedListOutlined /> Danh sách chức vụ
        </Flex>
      </Typography.Text>

      <Table<IBasePremsDTO>
        rowKey="id"
        footer={() => (
          <p>
            Tổng cộng: <b>{roles.length || 0}</b> chức vụ
          </p>
        )}
        scroll={{ x: 800 }}
        loading={isLoading}
        dataSource={roles.map((role: IBasePremsDTO) => ({ ...role, key: role.id }))}
        columns={columns}
        pagination={{ pageSize: 10 }}
      />

      <Divider className="mt-0!" />

      <Flex justify="space-between" align="center">
        <Button icon={<ArrowLeftOutlined />} onClick={() => back()}>
          Quay lại
        </Button>

        {hasPermission([Permissions.Role.UPDATE]) && (
          <UserUpdate
            id={id}
            superuser={data?.superuser || false}
            user={{
              ...data,
              roleIds: roles.map((role: IBasePremsDTO) => role.id)
            }}
          />
        )}
      </Flex>
    </Fragment>
  )
}
