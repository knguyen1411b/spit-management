'use client'

import {
  CalendarOutlined,
  InfoCircleOutlined,
  HistoryOutlined,
  ArrowLeftOutlined,
  EyeOutlined,
  UserOutlined
} from '@ant-design/icons'
import { Descriptions, Divider, Tag, Typography, Space, Button, Flex, Table, Avatar, Tooltip } from 'antd'
import { notFound, useRouter } from 'next/navigation'
import { Permissions } from '@/constants/permission'
import { Heading } from '@/components/ui/heading'
import type { IMemberTask } from '@/types'
import LoadingScreen from '@/app/loading'
import { Fragment, useMemo } from 'react'
import { useTasks } from '@/hooks/data'
import type { TableProps } from 'antd'
import TaskUpdate from './TaskUpdate'
import { useAuth } from '@/contexts'
import dayjs from 'dayjs'

export default function TaskDetail({ id }: { id: number }) {
  const { data: taskData, isLoading } = useTasks().useTaskDetail(id)
  const { back, push } = useRouter()
  const { hasPermission } = useAuth()

  const task = taskData?.data

  const columns: TableProps<IMemberTask>['columns'] = useMemo(
    () => [
      {
        title: 'Thành viên',
        key: 'member',
        render: (_, tm) => (
          <Flex gap={8} align="center">
            <Avatar src={tm.avatar} icon={<UserOutlined />} />
            <strong>
              {tm.lastName} {tm.firstName}
            </strong>
          </Flex>
        )
      },
      {
        title: 'Email',
        render: (_, tm) => tm.email
      },
      {
        title: 'Trạng thái',
        dataIndex: 'status',
        render: (status: string) => {
          let color = ''
          let text = ''

          switch (status) {
            case 'NONE':
              color = 'default'
              text = 'Chưa hoàn thành'
              break
            case 'PENDING':
              color = 'orange'
              text = 'Đã gửi yêu cầu'
              break
            case 'APPROVED':
              color = 'green'
              text = 'Đã hoàn thành'
              break
            case 'REJECTED':
              color = 'red'
              text = 'Thất bại'
              break
            default:
              color = 'default'
              text = status
          }

          return <Tag color={color}>{text}</Tag>
        }
      },
      {
        title: 'Mô tả',
        dataIndex: 'description',
        render: (d?: string) => d || '—'
      },
      {
        title: 'Ngày tham gia',
        dataIndex: 'requestedAt',
        render: (d?: string) => (d ? dayjs(d).format('DD/MM/YYYY') : '—')
      },
      {
        title: 'Hành động',
        key: 'action',
        render: (_, tm) => (
          <Tooltip title="Xem chi tiết thành viên">
            <Button size="small" icon={<EyeOutlined />} onClick={() => push(`/member/${tm.id}`)} />
          </Tooltip>
        )
      }
    ],
    [push]
  )

  if (taskData?.statusCode === 404) {
    notFound()
  }

  if (isLoading) {
    return <LoadingScreen />
  }

  return (
    <Fragment>
      <Heading title="Chi tiết công việc" description="Thông tin đầy đủ về công việc" />

      <Divider />

      <Descriptions
        title={<Typography.Title level={3}>Thông tin chung</Typography.Title>}
        column={{ xl: 2, lg: 2, md: 1, sm: 1, xs: 1 }}
        styles={{ label: { fontWeight: 600 } }}
      >
        <Descriptions.Item label="Tiêu đề">
          <Space>
            <InfoCircleOutlined /> {task?.title}
          </Space>
        </Descriptions.Item>

        <Descriptions.Item label="Loại công việc">
          <Tag color="blue">{task?.type}</Tag>
        </Descriptions.Item>

        <Descriptions.Item label="Ngày thực hiện">
          <Space>
            <CalendarOutlined />
            {dayjs(task?.date).format('DD/MM/YYYY HH:mm')}
          </Space>
        </Descriptions.Item>

        <Descriptions.Item label="Mô tả">{task?.description || '—'}</Descriptions.Item>

        <Descriptions.Item label="Ngày tạo">
          <Space>
            <HistoryOutlined />
            {dayjs(task?.createdAt).format('DD/MM/YYYY HH:mm')}
          </Space>
        </Descriptions.Item>

        <Descriptions.Item label="Cập nhật lần cuối">
          <Space>
            <HistoryOutlined />
            {dayjs(task?.updatedAt).format('DD/MM/YYYY HH:mm')}
          </Space>
        </Descriptions.Item>
      </Descriptions>

      <Divider />

      <Typography.Title level={3}>Thành viên tham gia</Typography.Title>

      <Table<IMemberTask>
        rowKey="id"
        size="middle"
        dataSource={task?.members || []}
        columns={columns}
        pagination={{ pageSize: 10 }}
        footer={() => (
          <p>
            Tổng cộng: <b>{task?.members.length || 0}</b> người
          </p>
        )}
      />
      <Divider />
      <Flex justify="space-between">
        <Button icon={<ArrowLeftOutlined />} onClick={() => back()}>
          Quay lại
        </Button>

        {hasPermission([Permissions.Task.UPDATE]) && task && <TaskUpdate task={task} />}
      </Flex>
    </Fragment>
  )
}
