'use client'

import { Button, Col, DatePicker, Form, Input, Modal, Row, Select, Typography } from 'antd'
import ModalError from '@/components/common/ModalError'
import { Fragment, useCallback, useState } from 'react'
import { PlusCircleOutlined } from '@ant-design/icons'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import Title from 'antd/es/typography/Title'
import { useMembers } from '@/hooks/data'
import { useToast } from '@/contexts'

export default function MemberCreate() {
  const isMobile = useIsMobile()
  const { createMember } = useMembers()
  const { success } = useToast()

  const [form] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const showModal = useCallback(() => {
    form.resetFields()
    setIsModalOpen(true)
    setModalError(null)
  }, [form])

  const handleCancel = useCallback(() => {
    if (!loading) setIsModalOpen(false)
  }, [loading])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)

      const formData = await form.validateFields()

      const payload = {
        lastName: formData.lastName,
        firstName: formData.firstName,
        gender: formData.gender,
        birthday: formData.birthday?.toISOString(),
        email: formData.email,
        phone: formData.phone,
        className: formData.className,
        generation: formData.generation,
        username: formData.username || '',
        description: formData.description || ''
      }

      const res = await createMember(payload)

      if (!res?.success) {
        setModalError(res?.message || 'Tạo thành viên thất bại')
        return
      }

      success('Tạo thành viên thành công')
      setIsModalOpen(false)
      form.resetFields()
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra')
    } finally {
      setLoading(false)
    }
  }, [form, createMember, success])

  return (
    <Fragment>
      <Button type="primary" icon={<PlusCircleOutlined />} onClick={showModal}>
        Thêm thành viên mới
      </Button>

      <Modal
        title={
          <Typography>
            <Title level={3}>Tạo thành viên mới</Title>
          </Typography>
        }
        open={isModalOpen}
        centered
        onOk={handleOk}
        onCancel={handleCancel}
        okText="Tạo thành viên"
        cancelText="Hủy"
        width="850px"
        okButtonProps={{ loading }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form layout="vertical" form={form} disabled={loading}>
          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="lastName" label="Họ" rules={[{ required: true, message: 'Họ là bắt buộc' }]}>
                <Input placeholder="Nhập họ" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="firstName" label="Tên" rules={[{ required: true, message: 'Tên là bắt buộc' }]}>
                <Input placeholder="Nhập tên" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="gender" label="Giới tính" rules={[{ required: true, message: 'Giới tính là bắt buộc' }]}>
                <Select
                  placeholder="Chọn giới tính"
                  options={[
                    { label: 'Nam', value: true },
                    { label: 'Nữ', value: false }
                  ]}
                />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="birthday"
                label="Ngày sinh"
                rules={[{ required: true, message: 'Ngày sinh là bắt buộc' }]}
              >
                <DatePicker className="w-full" format="DD/MM/YYYY" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="email"
                label="Email"
                rules={[
                  { required: true, message: 'Email là bắt buộc' },
                  { type: 'email', message: 'Email không hợp lệ' }
                ]}
              >
                <Input placeholder="Nhập email" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="phone"
                label="Số điện thoại"
                rules={[
                  { required: true, message: 'Số điện thoại là bắt buộc' },
                  {
                    pattern: /^0\d{9}$/,
                    message: 'Số điện thoại phải gồm 10 số và bắt đầu bằng số 0'
                  }
                ]}
              >
                <Input placeholder="Nhập số điện thoại" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="className" label="Lớp" rules={[{ required: true, message: 'Lớp là bắt buộc' }]}>
                <Input placeholder="VD: CNTT_K47" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="generation" label="Thế hệ" rules={[{ required: true, message: 'Thế hệ là bắt buộc' }]}>
                <Input placeholder="VD: K1" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item name="username" label="Mã số sinh viên (tuỳ chọn)">
            <Input placeholder="Nhập MSSV..." />
          </Form.Item>

          <Form.Item name="description" label="Mô tả (tuỳ chọn)">
            <Input.TextArea rows={3} placeholder="Nhập mô tả..." />
          </Form.Item>
        </Form>
        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
