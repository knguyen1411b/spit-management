'use client'

import { Card, Space, Button, Empty, Row, Col, Flex, Modal, Input, Form } from 'antd'
import { CalendarOutlined } from '@ant-design/icons'
import dayjs from 'dayjs'

import { useTaskRequests, useTasks } from '@/hooks/data'
import { Heading } from '@/components/ui/heading'
import Title from 'antd/es/typography/Title'
import Text from 'antd/es/typography/Text'
import LoadingScreen from '@/app/loading'
import { useToast } from '@/contexts'
import { useState } from 'react'

export default function ScheduleMain() {
  const [open, setOpen] = useState(false)
  const [loading, setLoading] = useState(false)
  const [selectedTaskMemberId, setSelectedTaskMemberId] = useState<number | null>(null)

  const [form] = Form.useForm()
  const { myTasks, isLoading } = useTasks({ size: 2000 })
  const { success, error } = useToast()
  const { createRequest } = useTaskRequests()

  const openJoinModal = (taskMemberId: number) => {
    setSelectedTaskMemberId(taskMemberId)
    form.resetFields()
    setOpen(true)
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      setLoading(true)

      const res = await createRequest({
        id: selectedTaskMemberId!,
        description: values.description
      })

      if (res.statusCode !== 200) {
        error(res.message || 'Đã có lỗi xảy ra')
        return
      }

      success('Gửi yêu cầu thành công')
      setOpen(false)
    } catch (err: any) {
      error(err.message || 'Đã có lỗi xảy ra')
    } finally {
      setLoading(false)
    }
  }

  const renderTypeLabel = (type: string) => {
    switch (type) {
      case 'EVENT':
        return <Text type="secondary">[Sự kiện]</Text>
      case 'TASK':
        return <Text type="secondary">[Công việc]</Text>
      default:
        return <Text type="secondary">[Khác]</Text>
    }
  }

  const renderActionButton = (task: any) => {
    const canAction = task.status === 'NONE' || task.status === 'REJECTED'

    if (canAction) {
      return (
        <Button
          type="primary"
          block
          danger={task.status === 'REJECTED'}
          onClick={() => openJoinModal(task.taskMemberId)}
        >
          {task.status === 'REJECTED' ? 'Yêu cầu lại' : 'Đánh dấu tham gia'}
        </Button>
      )
    }

    return (
      <Button type="default" block disabled>
        {task.status === 'PENDING' ? 'Đang chờ duyệt' : task.status === 'APPROVED' ? 'Đã tham gia' : 'Không khả dụng'}
      </Button>
    )
  }

  if (isLoading) {
    return <LoadingScreen />
  }

  return (
    <Flex vertical gap={10}>
      <Flex justify="space-between" align="center" gap={16}>
        <Flex vertical gap={4}>
          <Heading title="Thông báo" description="Danh sách thông báo trong hệ thống" />
        </Flex>
      </Flex>

      {!myTasks || myTasks.length === 0 ? (
        <Empty description="Không có lịch công tác nào" />
      ) : (
        <Row gutter={[16, 16]}>
          {myTasks.map(task => (
            <Col xs={24} md={12} lg={8} key={task.taskMemberId}>
              <Card
                hoverable
                style={{
                  borderRadius: 12,
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column'
                }}
              >
                <Space orientation="vertical" size={12} style={{ width: '100%' }}>
                  <Flex align="center" gap={8}>
                    <Title level={5} style={{ margin: 0, flex: 1 }}>
                      {task.title}
                    </Title>
                    {renderTypeLabel(task.type)}
                  </Flex>

                  <Text
                    type="secondary"
                    style={{
                      display: '-webkit-box',
                      WebkitLineClamp: 2,
                      WebkitBoxOrient: 'vertical',
                      overflow: 'hidden'
                    }}
                  >
                    {task.description || 'Không có mô tả'}
                  </Text>

                  <Space>
                    <CalendarOutlined />
                    <Text>{dayjs(task.date).format('DD/MM/YYYY • HH:mm')}</Text>
                  </Space>

                  {renderActionButton(task)}
                </Space>
              </Card>
            </Col>
          ))}
        </Row>
      )}
      <Modal
        title="Gửi yêu cầu đã tham gia"
        open={open}
        centered
        onOk={handleSubmit}
        onCancel={() => setOpen(false)}
        okText="Gửi yêu cầu"
        cancelText="Hủy"
        confirmLoading={loading}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            label="Mô tả / Ghi chú"
            name="description"
            rules={[
              { required: true, message: 'Vui lòng nhập mô tả' },
              { max: 500, message: 'Tối đa 500 ký tự' }
            ]}
          >
            <Input.TextArea
              rows={4}
              placeholder="Nhập mô tả lý do tham gia hoặc ghi chú thêm..."
              showCount
              maxLength={500}
            />
          </Form.Item>
        </Form>
      </Modal>
    </Flex>
  )
}
