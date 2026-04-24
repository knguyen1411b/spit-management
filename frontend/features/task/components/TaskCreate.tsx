'use client'

import { Button, Col, Form, Input, Modal, Row, DatePicker, Transfer, Select } from 'antd'
import { Fragment, Key, useMemo, useState } from 'react'
import { PlusCircleOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'

import ModalError from '@/components/common/ModalError'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { useMembers, useTasks } from '@/hooks/data'
import { useAuth, useToast } from '@/contexts'
import { IMember, ITaskCreate } from '@/types'

export enum TaskType {
  TASK = 'TASK',
  EVENT = 'EVENT',
  OTHER = 'OTHER'
}

interface ITransferItem extends IMember {
  key: Key
}

export default function TaskCreate() {
  const isMobile = useIsMobile()
  const { profile } = useAuth()
  const { createTask } = useTasks()
  const { members } = useMembers({ size: 2000, semesterId: profile?.semesterId })
  const { success } = useToast()

  const [form] = Form.useForm<ITaskCreate>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [selectedMembers, setSelectedMembers] = useState<Key[]>([])
  const [modalError, setModalError] = useState<string | null>(null)

  const transferDataSource = useMemo<ITransferItem[]>(() => {
    return (members || []).map(m => ({
      ...m,
      key: m.id
    }))
  }, [members])

  const showModal = () => {
    setModalError(null)
    setIsModalOpen(true)
    form.resetFields()
    setSelectedMembers([])
  }

  const handleOk = async () => {
    try {
      setLoading(true)
      const formData = await form.validateFields()

      const payload: ITaskCreate = {
        title: formData.title,
        description: formData.description || '',
        type: formData.type,
        date: (formData.date as any)?.toISOString(),
        memberIds: selectedMembers.map(Number)
      }

      const response = await createTask(payload)

      if (!response?.success) {
        setModalError(response?.message || 'Tạo công việc thất bại')
        return
      }

      success('Tạo công việc thành công')
      setIsModalOpen(false)
      form.resetFields()
      setSelectedMembers([])
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Fragment>
      <Button type="primary" icon={<PlusCircleOutlined />} onClick={showModal}>
        Tạo công việc mới
      </Button>

      <Modal
        open={isModalOpen}
        onOk={handleOk}
        onCancel={() => setIsModalOpen(false)}
        okText="Tạo công việc"
        cancelText="Hủy"
        centered
        width={800}
        okButtonProps={{ loading, disabled: selectedMembers.length === 0 }}
      >
        <Title level={3}>Tạo công việc mới</Title>

        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="title" label="Tiêu đề" rules={[{ required: true, message: 'Tiêu đề là bắt buộc' }]}>
                <Input placeholder="Nhập tiêu đề công việc" />
              </Form.Item>
            </Col>

            <Col span={12}>
              <Form.Item
                name="type"
                label="Loại công việc"
                rules={[{ required: true, message: 'Loại công việc là bắt buộc' }]}
              >
                <Select
                  placeholder="Chọn loại công việc"
                  options={[
                    { label: 'Sự kiện', value: TaskType.EVENT },
                    { label: 'Công việc', value: TaskType.TASK },
                    { label: 'Khác', value: TaskType.OTHER }
                  ]}
                />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item name="description" label="Mô tả">
            <Input.TextArea rows={3} placeholder="Nhập mô tả công việc" />
          </Form.Item>

          <Form.Item
            name="date"
            label="Ngày thực hiện"
            rules={[{ required: true, message: 'Ngày thực hiện là bắt buộc' }]}
          >
            <DatePicker className="w-full" showTime format="DD/MM/YYYY HH:mm" />
          </Form.Item>

          {isMobile ? (
            <Select
              mode="multiple"
              className="w-full"
              placeholder="Chọn thành viên"
              value={selectedMembers}
              onChange={setSelectedMembers}
              options={transferDataSource.map(m => ({
                label: `${m.lastName} ${m.firstName}`,
                value: m.id
              }))}
            />
          ) : (
            <Transfer
              oneWay
              className="[&_.ant-transfer-section]:w-full! [&_.ant-transfer-section]:h-72!"
              dataSource={transferDataSource}
              targetKeys={selectedMembers}
              onChange={setSelectedMembers}
              render={item => `${item.lastName} ${item.firstName}`}
            />
          )}
        </Form>

        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
