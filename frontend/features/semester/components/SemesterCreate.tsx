'use client'

import { Button, Col, Form, Input, Modal, Row, DatePicker, Switch, Typography, Transfer, Select } from 'antd'
import { Fragment, Key, useCallback, useMemo, useState } from 'react'
import ModalError from '@/components/common/ModalError'
import { useMembers, useSemesters } from '@/hooks/data'
import { PlusCircleOutlined } from '@ant-design/icons'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import Title from 'antd/es/typography/Title'
import { IMember, ISemester } from '@/types'
import { useToast } from '@/contexts'

interface ITransferItem extends IMember {
  key: Key
}
export default function SemesterCreate() {
  const isMobile = useIsMobile()
  const { createSemester } = useSemesters()
  const { members } = useMembers({ size: 2000 })
  const { success } = useToast()

  const [form] = Form.useForm<Partial<ISemester>>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)

  const [selectedMembers, setSelectedMembers] = useState<Key[]>([])
  const [modalError, setModalError] = useState<string | null>(null)

  const transferDataSource = useMemo<ITransferItem[]>(() => {
    return (members || []).map(member => ({
      ...member,
      key: member.id,
      name: member.firstName + ' ' + member.lastName
    }))
  }, [members])

  const filterOptionTransfer = useCallback((input: string, option: ITransferItem) => {
    const keyword = input.toLowerCase()
    const fullName = `${option.firstName} ${option.lastName}`.toLowerCase()
    return fullName.includes(keyword)
  }, [])

  const codeValue = Form.useWatch('code', form)
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
        code: formData.code,
        name: formData.name,
        description: formData.description || '',
        startDate: (formData.startDate as any)?.toISOString(),
        endDate: (formData.endDate as any)?.toISOString(),
        semesterOrder: Number(formData.semesterOrder),
        current: Boolean(formData.current),
        memberIds: selectedMembers
      }

      const response = await createSemester(payload)

      if (!response?.success) {
        setModalError(response?.message || 'Tạo kỳ học thất bại')
        return
      }

      success('Tạo kỳ học thành công.')
      setIsModalOpen(false)
      form.resetFields()
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [createSemester, form, selectedMembers, success])

  const isSubmitDisabled = loading || !codeValue || !nameValue || selectedMembers.length === 0

  const handleChangeTransfer = useCallback((keys: Key[]) => {
    setSelectedMembers(keys)
  }, [])

  const renderTransferItem = useCallback(
    (member: ITransferItem) => (
      <span>
        {member.firstName} {member.lastName}
      </span>
    ),
    []
  )

  const handleCancel = () => setIsModalOpen(false)

  return (
    <Fragment>
      <Button type="primary" icon={<PlusCircleOutlined />} onClick={showModal}>
        Tạo kỳ học mới
      </Button>

      <Modal
        title={
          <Typography>
            <Title level={3}>Tạo kỳ học mới</Title>
          </Typography>
        }
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        okText="Tạo kỳ học"
        centered
        cancelText="Hủy"
        width="800px"
        okButtonProps={{ loading, disabled: isSubmitDisabled }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="code" label="Mã kỳ học" rules={[{ required: true, message: 'Mã kỳ học là bắt buộc' }]}>
                <Input placeholder="Nhập mã kỳ học (ví dụ HK1-2025)" />
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
              <Form.Item name="current" label="Kỳ hiện tại" valuePropName="checked" initialValue={false} required>
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
              options={[
                ...transferDataSource.map(perm => ({
                  label: `${perm.firstName} ${perm.lastName}`,
                  value: perm.id
                }))
              ]}
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
