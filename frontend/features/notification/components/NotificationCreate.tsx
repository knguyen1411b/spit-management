'use client'

import { Button, Col, Form, Input, Modal, Row, Select, Transfer, Typography } from 'antd'
import { Fragment, Key, useCallback, useMemo, useState } from 'react'
import { PlusCircleOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'

import { useUsers, useNotifications } from '@/hooks/data'
import ModalError from '@/components/common/ModalError'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { useToast } from '@/contexts'

interface ITransferUser {
  key: Key
  id: number
  username: string
}

interface INotificationCreateDTO {
  title: string
  content: string
  type: 'INFO' | 'WARNING' | 'ALERT'
  receiverIds: number[]
}

export function NotificationCreate() {
  const isMobile = useIsMobile()
  const { users } = useUsers({ size: 2000 })
  const { createNotification } = useNotifications()
  const { success } = useToast()

  const [form] = Form.useForm<INotificationCreateDTO>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [selectedUsers, setSelectedUsers] = useState<Key[]>([])
  const [modalError, setModalError] = useState<string | null>(null)

  const transferDataSource = useMemo<ITransferUser[]>(() => {
    return (users || []).map(user => ({
      key: user.id,
      id: user.id,
      username: user.username
    }))
  }, [users])

  const showModal = () => {
    setIsModalOpen(true)
    setModalError(null)
    setSelectedUsers([])
    form.resetFields()
  }

  const handleOk = async () => {
    try {
      setLoading(true)

      const formData = await form.validateFields()
      formData.receiverIds = selectedUsers.map(id => Number(id))

      const response = await createNotification(formData)

      if (!response?.success) {
        setModalError(response?.message || 'Tạo thông báo thất bại')
        return
      }

      success('Tạo thông báo thành công')
      setIsModalOpen(false)
      form.resetFields()
      setSelectedUsers([])
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Fragment>
      <Button type="primary" icon={<PlusCircleOutlined />} onClick={showModal}>
        Tạo thông báo
      </Button>

      <Modal
        centered
        open={isModalOpen}
        onOk={handleOk}
        onCancel={() => setIsModalOpen(false)}
        okText="Gửi thông báo"
        cancelText="Hủy"
        width={720}
        okButtonProps={{ loading, disabled: loading || selectedUsers.length === 0 }}
      >
        <Typography>
          <Title level={3}>Tạo thông báo mới</Title>
        </Typography>

        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={24}>
              <Form.Item name="title" label="Tiêu đề" rules={[{ required: true, message: 'Tiêu đề là bắt buộc' }]}>
                <Input placeholder="Tiêu đề thông báo" />
              </Form.Item>
            </Col>

            <Col span={24}>
              <Form.Item name="content" label="Nội dung" rules={[{ required: true, message: 'Nội dung là bắt buộc' }]}>
                <Input.TextArea rows={4} placeholder="Nội dung thông báo" />
              </Form.Item>
            </Col>

            <Col span={24}>
              <Form.Item
                name="type"
                label="Loại thông báo"
                rules={[{ required: true, message: 'Vui lòng chọn loại thông báo' }]}
              >
                <Select
                  placeholder="Chọn loại"
                  options={[
                    { label: 'Thông tin', value: 'INFO' },
                    { label: 'Cảnh báo', value: 'WARNING' },
                    { label: 'Khẩn cấp', value: 'ALERT' }
                  ]}
                />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="Người nhận được" required>
            {isMobile ? (
              <Select
                mode="multiple"
                placeholder="Chọn người nhận"
                value={selectedUsers}
                onChange={setSelectedUsers}
                options={transferDataSource.map(u => ({
                  label: u.username,
                  value: u.id
                }))}
              />
            ) : (
              <Transfer
                oneWay
                showSearch
                dataSource={transferDataSource}
                targetKeys={selectedUsers}
                onChange={setSelectedUsers}
                titles={['Tất cả người dùng', 'Người nhận']}
                render={item => item.username}
                className="[&_.ant-transfer-section]:w-full! [&_.ant-transfer-section]:h-72!"
              />
            )}
          </Form.Item>
        </Form>

        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
