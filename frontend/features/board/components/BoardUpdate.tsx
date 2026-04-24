'use client'

import { Button, Col, Form, Input, Modal, Row, Typography } from 'antd'
import ModalError from '@/components/common/ModalError'
import { Fragment, useCallback, useState } from 'react'
import { EditOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { useBoards } from '@/hooks/data'
import { useToast } from '@/contexts'
import { IBoard } from '@/types'

export default function BoardUpdate({ id, board }: { id: number; board: IBoard }) {
  const { updateBoard } = useBoards()
  const { success } = useToast()

  const [form] = Form.useForm<Partial<IBoard>>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const showModal = useCallback(() => {
    setIsModalOpen(true)
    setModalError(null)

    form.setFieldsValue({
      name: board.name,
      description: board.description || ''
    })
  }, [board, form])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)

      const formData = await form.validateFields()

      const payload = {
        name: formData.name,
        description: formData.description || ''
      }

      const response = await updateBoard({ id, data: payload })

      if (!response?.success) {
        setModalError(response?.message || 'Cập nhật ban thất bại')
        return
      }

      success('Cập nhật ban thành công')
      setIsModalOpen(false)
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [form, id, updateBoard, success])

  const handleCancel = useCallback(() => {
    if (!loading) setIsModalOpen(false)
  }, [loading])

  return (
    <Fragment>
      <Button type="primary" icon={<EditOutlined />} onClick={showModal}>
        Chỉnh sửa ban
      </Button>

      <Modal
        title={
          <Typography>
            <Title level={3}>Cập nhật ban</Title>
          </Typography>
        }
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        centered
        okText="Cập nhật"
        cancelText="Hủy"
        width={600}
        okButtonProps={{ loading }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={24}>
              <Form.Item name="name" label="Tên ban" rules={[{ required: true, message: 'Tên ban là bắt buộc' }]}>
                <Input placeholder="Nhập tên ban" />
              </Form.Item>
            </Col>

            <Col span={24}>
              <Form.Item name="description" label="Mô tả (tuỳ chọn)">
                <Input.TextArea rows={3} placeholder="Nhập mô tả ban" />
              </Form.Item>
            </Col>
          </Row>
        </Form>
        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
