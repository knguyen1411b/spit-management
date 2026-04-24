'use client'

import { Button, Col, Form, Input, Modal, Row, Radio, DatePicker, Typography } from 'antd'
import ModalError from '@/components/common/ModalError'
import { Fragment, useCallback, useState } from 'react'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { EditOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { useMembers } from '@/hooks/data'
import { useToast } from '@/contexts'
import { IMember } from '@/types'
import dayjs from 'dayjs'

export default function MemberUpdate({ id, member }: { id: number; member: IMember }) {
  const isMobile = useIsMobile()
  const { updateMember } = useMembers()
  const { success } = useToast()

  const [form] = Form.useForm()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const showModal = useCallback(() => {
    setIsModalOpen(true)
    setModalError(null)

    form.setFieldsValue({
      lastName: member.lastName,
      firstName: member.firstName,
      gender: member.gender,
      birthday: dayjs(member.birthday),
      email: member.email,
      phone: member.phone,
      className: member.className,
      generation: member.generation,
      username: member.username,
      description: member.description
    })
  }, [form, member])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)
      const values = await form.validateFields()

      const payload = {
        ...values,
        birthday: values.birthday.toISOString()
      }

      const res = await updateMember({ id, data: payload })

      if (!res?.success) {
        setModalError(res?.message || 'Cập nhật thành viên thất bại')
        return
      }

      success('Cập nhật thành viên thành công!')
      setIsModalOpen(false)
    } catch (err: any) {
      setModalError(err?.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [form, updateMember, id, success])

  return (
    <Fragment>
      <Button type="primary" icon={<EditOutlined />} onClick={showModal}>
        Chỉnh sửa thành viên
      </Button>

      <Modal
        open={isModalOpen}
        title={
          <Typography>
            <Title level={3}>Cập nhật thông tin thành viên</Title>
          </Typography>
        }
        onOk={handleOk}
        centered
        onCancel={() => !loading && setIsModalOpen(false)}
        okButtonProps={{ loading }}
        cancelButtonProps={{ disabled: loading }}
        width={800}
        okText="Cập nhật"
        cancelText="Hủy"
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="lastName" label="Họ" rules={[{ required: true }]}>
                <Input placeholder="Nhập họ" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="firstName" label="Tên" rules={[{ required: true }]}>
                <Input placeholder="Nhập tên" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="gender" label="Giới tính" rules={[{ required: true }]}>
                <Radio.Group>
                  <Radio value={true}>Nam</Radio>
                  <Radio value={false}>Nữ</Radio>
                </Radio.Group>
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="birthday" label="Ngày sinh" rules={[{ required: true }]}>
                <DatePicker className="w-full" format="DD/MM/YYYY" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="email" label="Email" rules={[{ type: 'email', required: true }]}>
                <Input placeholder="Nhập email" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="phone"
                label="Số điện thoại"
                rules={[
                  { required: true, message: 'Vui lòng nhập số điện thoại' },
                  {
                    pattern: /^0[0-9]{9}$/,
                    message: 'Số điện thoại phải bắt đầu bằng 0 và có đúng 10 số'
                  }
                ]}
              >
                <Input placeholder="Nhập số điện thoại" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="className" label="Lớp" rules={[{ required: true }]}>
                <Input placeholder="Nhập lớp" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="generation" label="Thế hệ" rules={[{ required: true }]}>
                <Input placeholder="Nhập thế hệ" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item name="username" label="MSSV" rules={[{ required: true }]}>
            <Input placeholder="Nhập mã số sinh viên" />
          </Form.Item>

          <Form.Item name="description" label="Mô tả">
            <Input.TextArea rows={3} placeholder="Nhập mô tả (không bắt buộc)" />
          </Form.Item>
        </Form>
        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
