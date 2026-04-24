'use client'

import { Button, Col, Form, Input, Modal, Row, Typography } from 'antd'
import ModalError from '@/components/common/ModalError'
import { Fragment, useCallback, useState } from 'react'
import { PlusCircleOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { useBoards } from '@/hooks/data'
import { useToast } from '@/contexts'
import { IBoard } from '@/types'

export default function BoardCreate() {
  const { createBoard } = useBoards()
  const { success } = useToast()

  const [form] = Form.useForm<Partial<IBoard>>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const nameValue = Form.useWatch('name', form)

  const showModal = useCallback(() => {
    setModalError(null)
    setIsModalOpen(true)
    form.resetFields()
  }, [form])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)

      const formData = await form.validateFields()

      const payload = {
        name: formData.name,
        description: formData.description || ''
      }

      const response = await createBoard(payload)

      if (!response?.success) {
        setModalError(response?.message || 'Tạo ban thất bại')
        return
      }

      success('Tạo ban thành công.')
      setIsModalOpen(false)
      form.resetFields()
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [form, createBoard, success])

  const handleCancel = useCallback(() => setIsModalOpen(false), [])

  const isSubmitDisabled = loading || !nameValue

  return (
    <Fragment>
      <Button type="primary" icon={<PlusCircleOutlined />} onClick={showModal}>
        Tạo ban mới
      </Button>

      <Modal
        title={
          <Typography>
            <Title level={3}>Tạo ban mới</Title>
          </Typography>
        }
        centered
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        okText="Tạo ban"
        cancelText="Hủy"
        width={600}
        okButtonProps={{ loading, disabled: isSubmitDisabled }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={24}>
              <Form.Item name="name" label="Tên ban" rules={[{ required: true, message: 'Tên ban là bắt buộc' }]}>
                <Input placeholder="Ví dụ: General Discussion" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item name="description" label="Mô tả (tuỳ chọn)">
            <Input.TextArea rows={3} placeholder="Nhập mô tả ban (không bắt buộc)" />
          </Form.Item>
        </Form>
        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
