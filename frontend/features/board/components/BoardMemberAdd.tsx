'use client'

import { Button, Col, Form, Input, Modal, Row, Select, Typography } from 'antd'
import { useMembers, useBoardMembers } from '@/hooks/data'
import ModalError from '@/components/common/ModalError'
import { Fragment, useState, useCallback } from 'react'
import { PlusCircleOutlined } from '@ant-design/icons'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import Title from 'antd/es/typography/Title'
import { IAddBoardMemberDTO } from '@/types'
import { useToast } from '@/contexts'

export default function BoardMemberAdd({ id }: { id: number }) {
  const isMobile = useIsMobile()
  const { members } = useMembers({ size: 2000 })
  const { addMember } = useBoardMembers(id)
  const { success } = useToast()

  const [form] = Form.useForm<IAddBoardMemberDTO>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const positionValue = Form.useWatch('position', form)
  const descriptionBoardValue = Form.useWatch('descriptionBoard', form)
  const memberIdValue = Form.useWatch('memberId', form)

  const showModal = useCallback(() => {
    form.resetFields()
    setModalError(null)
    setIsModalOpen(true)
  }, [form])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)
      const formData = await form.validateFields()

      const payload: IAddBoardMemberDTO = {
        position: formData.position.trim(),
        descriptionBoard: formData.descriptionBoard?.trim() || '',
        memberId: formData.memberId
      }

      const response = await addMember(payload)

      if (!response?.success) {
        if (response.statusCode === 400) {
          setModalError('Thành viên này đã có trong ban!')
          setIsModalOpen(false)
          return
        }
        setModalError(response?.message || 'Thêm thành viên vào ban thất bại')
        return
      }

      success('Thêm thành viên vào ban thành công!')
      setIsModalOpen(false)
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [form, addMember, success])

  return (
    <Fragment>
      <Button type="primary" icon={<PlusCircleOutlined />} onClick={showModal}>
        Thêm thành viên vào ban
      </Button>

      <Modal
        title={
          <Typography>
            x<Title level={3}>Thêm thành viên vào ban</Title>
          </Typography>
        }
        open={isModalOpen}
        onOk={handleOk}
        onCancel={() => setIsModalOpen(false)}
        okText="Thêm"
        centered
        cancelText="Hủy"
        width={650}
        okButtonProps={{ loading, disabled: !positionValue || !memberIdValue || loading || !descriptionBoardValue }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="position"
                label="Chức vụ trong ban"
                rules={[{ required: true, message: 'Chức vụ là bắt buộc' }]}
              >
                <Input placeholder="VD: Trưởng ban, Phó ban..." />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="descriptionBoard"
                label="Mô tả"
                rules={[{ required: true, message: 'Mô tả là bắt buộc' }]}
              >
                <Input placeholder="Nhập mô tả..." />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item
            name="memberId"
            label="Chọn 1 thành viên"
            rules={[{ required: true, message: 'Vui lòng chọn 1 thành viên' }]}
          >
            <Select
              showSearch
              placeholder="Chọn thành viên"
              className="w-full"
              options={members.map(m => ({
                label: `${m.lastName} ${m.firstName} - ${m.className}`,
                value: m.id
              }))}
            />
          </Form.Item>
        </Form>
        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
