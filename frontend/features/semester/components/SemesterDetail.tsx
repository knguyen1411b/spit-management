'use client'

import {
  CalendarOutlined,
  InfoCircleOutlined,
  IdcardOutlined,
  HistoryOutlined,
  ArrowLeftOutlined,
  EyeOutlined,
  UserOutlined
} from '@ant-design/icons'
import { Descriptions, Divider, Tag, Typography, Space, Button, Flex, Table, Avatar, TableProps, Tooltip } from 'antd'
import { useMembers, useSemesters } from '@/hooks/data'
import { notFound, useRouter } from 'next/navigation'
import { Permissions } from '@/constants/permission'
import { Heading } from '@/components/ui/heading'
import SemesterUpdate from './SemesterUpdate'
import LoadingScreen from '@/app/loading'
import { Fragment, useMemo } from 'react'
import { useAuth } from '@/contexts'
import { IMember } from '@/types'
import dayjs from 'dayjs'

export default function SemesterDetail({ id }: { id: number }) {
  const { data: semesterData, isLoading } = useSemesters().useSemesterDetail(id)
  const { back, push } = useRouter()
  const { members } = useMembers({ semesterId: id })
  const { hasPermission } = useAuth()

  const data = semesterData?.data

  const columns: TableProps<IMember>['columns'] = useMemo(
    () => [
      {
        title: 'Tên đầy đủ',
        key: 'fullName',
        render: (_, m) => (
          <Flex gap={8} align="center">
            <Avatar src={m.avatar} icon={<UserOutlined />} />
            <strong>
              {m.lastName} {m.firstName}
            </strong>
          </Flex>
        )
      },
      {
        title: 'Email',
        dataIndex: 'email',
        key: 'email'
      },
      {
        title: 'Số điện thoại',
        dataIndex: 'phone',
        key: 'phone'
      },
      {
        title: 'Giới tính',
        dataIndex: 'gender',
        key: 'gender',
        render: (g: boolean) => (g ? <Tag color="blue">Nam</Tag> : <Tag color="magenta">Nữ</Tag>)
      },
      {
        title: 'Tên người dùng',
        dataIndex: 'username',
        key: 'username'
      },
      {
        title: 'Ngày sinh',
        dataIndex: 'birthday',
        key: 'birthday',
        render: (d: string) => dayjs(d).format('DD/MM/YYYY')
      },
      {
        title: 'Lớp',
        dataIndex: 'className',
        key: 'className'
      },
      {
        title: 'Mô tả',
        dataIndex: 'description',
        key: 'description'
      },
      {
        title: 'Thế hệ',
        dataIndex: 'generation',
        key: 'generation'
      },
      {
        title: 'Hành động',
        key: 'action',
        render: (_, m) => (
          <Tooltip title="Xem chi tiết thành viên">
            <Button type="primary" size="small" icon={<EyeOutlined />} onClick={() => push(`/member/${m.id}`)} />
          </Tooltip>
        )
      }
    ],
    [push]
  )

  if (semesterData?.statusCode === 404) {
    notFound()
  }

  if (isLoading) {
    return <LoadingScreen />
  }

  return (
    <Fragment>
      <Heading title="Chi tiết kỳ học" description="Thông tin đầy đủ về kỳ học được chọn" />

      <Divider />

      <Descriptions
        title={<Typography.Title level={3}>Thông tin chung</Typography.Title>}
        column={{
          xxl: 3,
          xl: 2,
          lg: 2,
          md: 1,
          sm: 1,
          xs: 1
        }}
        styles={{
          label: { fontWeight: 600 },
          content: { fontSize: 15 }
        }}
      >
        <Descriptions.Item label="Mã kỳ học">
          <Space>
            <IdcardOutlined /> {data?.code}
          </Space>
        </Descriptions.Item>

        <Descriptions.Item label="Tên kỳ học">
          <Space>
            <IdcardOutlined /> {data?.name}
          </Space>
        </Descriptions.Item>

        <Descriptions.Item label="Mô tả">
          <Space>
            <InfoCircleOutlined /> {data?.description || '—'}
          </Space>
        </Descriptions.Item>

        <Descriptions.Item label="Ngày bắt đầu">
          <Space>
            <CalendarOutlined /> {dayjs(data?.startDate).format('DD/MM/YYYY')}
          </Space>
        </Descriptions.Item>

        <Descriptions.Item label="Ngày kết thúc">
          <Space>
            <CalendarOutlined /> {dayjs(data?.endDate).format('DD/MM/YYYY')}
          </Space>
        </Descriptions.Item>

        <Descriptions.Item label="Kì thứ">{data?.semesterOrder}</Descriptions.Item>

        <Descriptions.Item label="Kỳ hiện tại">
          {data?.current ? <Tag color="green">Kỳ hiện tại</Tag> : <Tag color="default">Không</Tag>}
        </Descriptions.Item>

        <Descriptions.Item label="Ngày tạo">
          <Space>
            <HistoryOutlined />
            {dayjs(data?.createdAt).format('DD/MM/YYYY HH:mm')}
          </Space>
        </Descriptions.Item>

        <Descriptions.Item label="Ngày cập nhật">
          <Space>
            <HistoryOutlined />
            {dayjs(data?.updatedAt).format('DD/MM/YYYY HH:mm')}
          </Space>
        </Descriptions.Item>
      </Descriptions>

      <Divider />
      <Typography.Title level={3}>Thành viên trong kì</Typography.Title>
      <Table<IMember>
        rowKey="id"
        size="middle"
        footer={() => (
          <p>
            Tổng cộng: <b>{members.length || 0}</b> người
          </p>
        )}
        scroll={{ x: 800 }}
        loading={isLoading}
        dataSource={members}
        columns={columns}
        pagination={{ pageSize: 10 }}
      />

      <Flex justify="space-between" align="center">
        <Button icon={<ArrowLeftOutlined />} onClick={() => back()}>
          Quay lại
        </Button>
        {hasPermission([Permissions.Semester.UPDATE]) && (
          <SemesterUpdate id={id} semester={data} semesterMember={members} />
        )}
      </Flex>
    </Fragment>
  )
}
