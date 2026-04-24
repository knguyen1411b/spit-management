'use client'

import {
  ArrowLeftOutlined,
  HistoryOutlined,
  IdcardOutlined,
  InfoCircleOutlined,
  UnorderedListOutlined
} from '@ant-design/icons'
import { Button, Divider, Flex, Table, Tag, Typography } from 'antd'
import { notFound, useRouter } from 'next/navigation'
import { Permissions } from '@/constants/permission'
import { Heading } from '@/components/ui/heading'
import LoadingScreen from '@/app/loading'
import { Fragment, useMemo } from 'react'
import { useRoles } from '@/hooks/data'
import { IBasePremsDTO } from '@/types'
import RoleUpdate from './RoleUpdate'
import { useAuth } from '@/contexts'
import dayjs from 'dayjs'

export default function RoleDetail({ id }: { id: number }) {
  const { hasPermission } = useAuth()
  const { back } = useRouter()
  const { data: roleData, isLoading } = useRoles().useRoleDetail(id)

  const data = roleData?.data
  const perms = data?.permissions || []

  if (roleData?.statusCode === 404) {
    notFound()
  }

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

  if (isLoading) {
    return <LoadingScreen />
  }

  return (
    <Fragment>
      <Heading title="Chi tiết chức vụ" description="Xem thông tin chi tiết của chức vụ" />

      <Divider />

      <Flex vertical gap={18}>
        <Flex gap={6}>
          <IdcardOutlined /> <b>Mã chức vụ:</b> {data?.code}
        </Flex>
        <Flex gap={6}>
          <IdcardOutlined /> <b>Tên chức vụ:</b> {data?.name}
        </Flex>
        <Flex gap={6}>
          <InfoCircleOutlined /> <b>Mô tả:</b> {data?.description}
        </Flex>
        <Flex gap={6}>
          <HistoryOutlined /> <b>Ngày tạo:</b> {dayjs(data?.createdAt).format('DD/MM/YYYY HH:mm')}
        </Flex>
        <Flex gap={6}>
          <HistoryOutlined /> <b>Ngày cập nhật:</b> {dayjs(data?.updatedAt).format('DD/MM/YYYY HH:mm')}
        </Flex>
      </Flex>

      <Divider />

      <Typography.Text strong>
        <Flex gap={6} align="center" className="mb-5!">
          <UnorderedListOutlined /> Danh sách quyền
        </Flex>
      </Typography.Text>

      <Table<IBasePremsDTO>
        rowKey="id"
        footer={() => (
          <p>
            Tổng cộng: <b>{perms.length || 0}</b> quyền
          </p>
        )}
        scroll={{ x: 800 }}
        loading={isLoading}
        dataSource={perms.map((perm: IBasePremsDTO) => ({ ...perm, key: perm.id }))}
        columns={columns}
        pagination={{ pageSize: 10 }}
      />

      <Divider className="mt-1!" />

      <Flex justify="space-between" align="center">
        <Button icon={<ArrowLeftOutlined />} onClick={() => back()}>
          Quay lại
        </Button>

        {hasPermission([Permissions.Role.UPDATE]) && (
          <RoleUpdate
            id={id}
            role={{
              ...data,
              permissionIds: perms.map((perm: IBasePremsDTO) => perm.id)
            }}
          />
        )}
      </Flex>
    </Fragment>
  )
}
