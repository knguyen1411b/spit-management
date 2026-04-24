'use client'

import { Button, Col, Form, Input, Modal, Row, DatePicker, Switch, Typography, Select } from 'antd'
import ModalError from '@/components/common/ModalError'
import { Fragment, useCallback, useState } from 'react'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { CopyOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { useSemesters } from '@/hooks/data'
import { useToast } from '@/contexts'
import dayjs from 'dayjs'

interface SemesterCopyForm {
  sourceSemesterId: number
  code: string
  name: string
  description?: string
  startDate: dayjs.Dayjs
  endDate: dayjs.Dayjs
  semesterOrder: number
  current: boolean
  copyMembers: boolean
  copyBoards: boolean
}

export default function SemesterCopy() {
  const isMobile = useIsMobile()
  const { success } = useToast()
  const { semesters, copySemester } = useSemesters()

  const [form] = Form.useForm<SemesterCopyForm>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const codeValue = Form.useWatch('code', form)
  const nameValue = Form.useWatch('name', form)
  const sourceSemesterId = Form.useWatch('sourceSemesterId', form)

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
        sourceSemesterId: Number(formData.sourceSemesterId),
        code: formData.code,
        name: formData.name,
        description: formData.description || '',
        startDate: formData.startDate.toISOString(),
        endDate: formData.endDate.toISOString(),
        semesterOrder: Number(formData.semesterOrder),
        current: Boolean(formData.current),
        copyMembers: Boolean(formData.copyMembers),
        copyBoards: Boolean(formData.copyBoards)
      }

      const response = await copySemester(payload)

      if (!response?.success) {
        setModalError(response?.message || 'Sao chép kỳ học thất bại')
        return
      }

      success('Sao chép kỳ học thành công.')
      setIsModalOpen(false)
      form.resetFields()
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [form, success, copySemester])

  const isSubmitDisabled = loading || !codeValue || !nameValue || !sourceSemesterId

  const handleCancel = () => setIsModalOpen(false)

  return (
    <Fragment>
      <Button type="primary" icon={<CopyOutlined />} onClick={showModal}>
        Sao chép kỳ học
      </Button>

      <Modal
        title={
          <Typography>
            <Title level={3}>Sao chép kỳ học</Title>
          </Typography>
        }
        open={isModalOpen}
        onOk={handleOk}
        centered
        onCancel={handleCancel}
        okText="Sao chép"
        cancelText="Hủy"
        width="800px"
        okButtonProps={{ loading, disabled: isSubmitDisabled }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Form.Item
            name="sourceSemesterId"
            label="Chọn kỳ học gốc"
            rules={[{ required: true, message: 'Vui lòng chọn kỳ học gốc để sao chép' }]}
          >
            <Select
              placeholder="Chọn kỳ học cần sao chép"
              options={semesters.map(s => ({
                value: s.id,
                label: `${s.code} — ${s.name}`
              }))}
            />
          </Form.Item>

          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="code"
                label="Mã kỳ học mới"
                rules={[{ required: true, message: 'Mã kỳ học là bắt buộc' }]}
              >
                <Input placeholder="Nhập mã kỳ học (ví dụ HK1-2025)" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="name"
                label="Tên kỳ học mới"
                rules={[{ required: true, message: 'Tên kỳ học là bắt buộc' }]}
              >
                <Input placeholder="Nhập tên kỳ học mới" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item name="description" label="Mô tả (tuỳ chọn)">
            <Input.TextArea rows={3} placeholder="Nhập mô tả kỳ học" />
          </Form.Item>

          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="startDate"
                label="Ngày bắt đầu"
                rules={[{ required: true, message: 'Ngày bắt đầu là bắt buộc' }]}
              >
                <DatePicker className="w-full" format="DD/MM/YYYY" placeholder="Chọn ngày bắt đầu" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="endDate"
                label="Ngày kết thúc"
                rules={[{ required: true, message: 'Ngày kết thúc là bắt buộc' }]}
              >
                <DatePicker className="w-full" format="DD/MM/YYYY" placeholder="Chọn ngày kết thúc" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="semesterOrder"
                label="Thứ tự kỳ học"
                rules={[{ required: true, message: 'Thứ tự kỳ học là bắt buộc' }]}
              >
                <Input type="number" min={1} placeholder="Nhập thứ tự kỳ (ví dụ: 1)" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="current" label="Kỳ hiện tại" valuePropName="checked" initialValue={false}>
                <Switch />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="copyMembers" label="Sao chép Members" valuePropName="checked" initialValue={true}>
                <Switch />
              </Form.Item>
            </Col>

            <Col span={12}>
              <Form.Item name="copyBoards" label="Sao chép Boards" valuePropName="checked" initialValue={true}>
                <Switch />
              </Form.Item>
            </Col>
          </Row>
        </Form>
        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
