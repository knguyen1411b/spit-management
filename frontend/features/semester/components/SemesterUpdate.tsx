'use client'

import { Button, Col, Form, Input, Modal, Row, DatePicker, Switch, Typography, Select, Transfer } from 'antd'
import { Fragment, Key, useCallback, useMemo, useState } from 'react'
import ModalError from '@/components/common/ModalError'
import { useMembers, useSemesters } from '@/hooks/data'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { EditOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { IMember, ISemester } from '@/types'
import { useToast } from '@/contexts'
import dayjs from 'dayjs'

interface SemesterFormValues {
  code: string
  name: string
  description?: string
  startDate: dayjs.Dayjs
  endDate: dayjs.Dayjs
  semesterOrder: number
  current: boolean
}

interface ITransferItem extends IMember {
  key: Key
}

export default function SemesterUpdate({
  id,
  semester,
  semesterMember
}: {
  id: number
  semester: ISemester
  semesterMember: IMember[]
}) {
  const isMobile = useIsMobile()
  const { updateSemester } = useSemesters()
  const { members } = useMembers({ size: 2000 })
  const { success } = useToast()

  const [form] = Form.useForm<SemesterFormValues>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const codeValue = Form.useWatch('code', form)
  const nameValue = Form.useWatch('name', form)

  const [selectedMembers, setSelectedMembers] = useState<Key[]>([])

  const transferDataSource = useMemo<ITransferItem[]>(() => {
    return (members || []).map(member => ({
      ...member,
      key: String(member.id),
      title: `${member.lastName} ${member.firstName}`
    }))
  }, [members])

  const filterOptionTransfer = useCallback((input: string, option: ITransferItem) => {
    const keyword = input.toLowerCase()
    const fullName = `${option.firstName} ${option.lastName}`.toLowerCase()
    return fullName.includes(keyword)
  }, [])

  const showModal = useCallback(() => {
    setModalError(null)
    setIsModalOpen(true)

    form.setFieldsValue({
      code: semester.code,
      name: semester.name,
      description: semester.description || '',
      startDate: dayjs(semester.startDate),
      endDate: dayjs(semester.endDate),
      semesterOrder: semester.semesterOrder,
      current: semester.current
    })

    setSelectedMembers(semesterMember.map(m => String(m.id)))
  }, [form, semester, semesterMember])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)

      const formData = await form.validateFields()

      const payload = {
        code: formData.code,
        name: formData.name,
        description: formData.description || '',
        startDate: formData.startDate.toISOString(),
        endDate: formData.endDate.toISOString(),
        semesterOrder: Number(formData.semesterOrder),
        current: Boolean(formData.current),
        memberIds: selectedMembers
      }

      const response = await updateSemester({ id, data: payload })

      if (!response?.success) {
        setModalError(response?.message || 'Cập nhật kỳ học thất bại')
        return
      }

      success('Cập nhật kỳ học thành công')
      setIsModalOpen(false)
      form.resetFields()
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [form, id, selectedMembers, updateSemester, success])

  const handleChangeTransfer = useCallback((keys: Key[]) => {
    setSelectedMembers(keys)
  }, [])

  const renderTransferItem = useCallback(
    (member: ITransferItem) => (
      <span>
        {member.lastName} {member.firstName}
      </span>
    ),
    []
  )

  const isSubmitDisabled = loading || !codeValue || !nameValue || selectedMembers.length === 0

  const handleCancel = useCallback(() => {
    setIsModalOpen(false)
    form.resetFields()
  }, [form])

  return (
    <Fragment>
      <Button type="primary" icon={<EditOutlined />} onClick={showModal}>
        Chỉnh sửa kỳ học
      </Button>

      <Modal
        title={
          <Typography>
            <Title level={3}>Cập nhật kỳ học</Title>
          </Typography>
        }
        centered
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        okText="Cập nhật"
        cancelText="Hủy"
        width={800}
        okButtonProps={{ loading, disabled: isSubmitDisabled }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="code" label="Mã kỳ học" rules={[{ required: true, message: 'Mã kỳ học là bắt buộc' }]}>
                <Input placeholder="Nhập mã kỳ học" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="name" label="Tên kỳ học" rules={[{ required: true, message: 'Tên kỳ học là bắt buộc' }]}>
                <Input placeholder="Nhập tên kỳ học" />
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
                <DatePicker className="w-full" format="DD/MM/YYYY" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="endDate"
                label="Ngày kết thúc"
                rules={[{ required: true, message: 'Ngày kết thúc là bắt buộc' }]}
              >
                <DatePicker className="w-full" format="DD/MM/YYYY" />
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
                <Input type="number" min={1} placeholder="VD: 1" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="current" label="Kỳ hiện tại" valuePropName="checked" required>
                <Switch />
              </Form.Item>
            </Col>
          </Row>

          {isMobile ? (
            <Select
              mode="multiple"
              className="w-full!"
              placeholder="Chọn thành viên"
              value={selectedMembers}
              showSearch={{
                optionFilterProp: 'label'
              }}
              onChange={setSelectedMembers}
              options={transferDataSource.map(m => ({
                label: `${m.firstName} ${m.lastName}`,
                value: m.id
              }))}
            />
          ) : (
            <Transfer
              oneWay
              className="[&_.ant-transfer-section]:w-full! [&_.ant-transfer-section]:h-72!"
              titles={['Danh sách thành viên', 'Thành viên của kỳ học']}
              rowKey={item => String(item.id)}
              dataSource={transferDataSource}
              showSearch
              targetKeys={selectedMembers}
              onChange={handleChangeTransfer}
              filterOption={filterOptionTransfer}
              render={renderTransferItem}
            />
          )}
        </Form>
        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
