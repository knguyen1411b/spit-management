'use client'

import { Button, Col, Form, Input, Modal, Row, DatePicker, Select, Transfer } from 'antd'
import { Fragment, Key, useCallback, useMemo, useState } from 'react'
import { EditOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import dayjs from 'dayjs'

import ModalError from '@/components/common/ModalError'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { useMembers, useTasks } from '@/hooks/data'
import { IMember, ITask } from '@/types'
import { useToast } from '@/contexts'

export enum TaskType {
  TASK = 'TASK',
  EVENT = 'EVENT',
  OTHER = 'OTHER'
}

interface TaskFormValues {
  title: string
  description?: string
  type: TaskType
  date: dayjs.Dayjs
}

interface ITransferItem extends IMember {
  key: Key
}

export default function TaskUpdate({ task }: { task: ITask }) {
  const isMobile = useIsMobile()
  const { updateTask } = useTasks()
  const { members } = useMembers({ size: 2000 })
  const { success } = useToast()

  const [form] = Form.useForm<TaskFormValues>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)
  const [selectedMembers, setSelectedMembers] = useState<Key[]>(task.members.map(tm => String(tm.id)))

  const transferDataSource = useMemo<ITransferItem[]>(() => {
    return (members || []).map(m => ({
      ...m,
      key: String(m.id)
    }))
  }, [members])

  const showModal = useCallback(() => {
    setModalError(null)
    setIsModalOpen(true)

    form.setFieldsValue({
      title: task.title,
      description: task.description,
      type: task.type,
      date: dayjs(task.date)
    })

    setSelectedMembers(task.members.map(tm => String(tm.id)))
  }, [form, task])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)
      const values = await form.validateFields()

      const payload = {
        title: values.title,
        description: values.description || '',
        type: values.type,
        date: (values.date as any)?.toISOString(),
        memberIds: selectedMembers.map(Number)
      }

      const response = await updateTask({
        id: task.id,
        data: payload
      })

      if (!response?.success) {
        setModalError(response?.message || 'Cập nhật công việc thất bại')
        return
      }

      success('Cập nhật công việc thành công')
      setIsModalOpen(false)
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra, vui lòng thử lại')
    } finally {
      setLoading(false)
    }
  }, [form, selectedMembers, updateTask, task.id, success])

  return (
    <Fragment>
      <Button type="primary" icon={<EditOutlined />} onClick={showModal}>
        Chỉnh sửa công việc
      </Button>

      <Modal
        title={<Title level={3}>Cập nhật công việc</Title>}
        centered
        open={isModalOpen}
        onOk={handleOk}
        onCancel={() => setIsModalOpen(false)}
        okText="Cập nhật"
        cancelText="Hủy"
        width={800}
        okButtonProps={{ loading, disabled: selectedMembers.length === 0 }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="title" label="Tiêu đề" rules={[{ required: true, message: 'Tiêu đề là bắt buộc' }]}>
                <Input placeholder="Nhập tiêu đề công việc" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
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
            <Input.TextArea rows={3} />
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
                label: `${m.firstName} ${m.lastName}`,
                value: m.id
              }))}
            />
          ) : (
            <Transfer
              oneWay
              className="[&_.ant-transfer-section]:w-full! [&_.ant-transfer-section]:h-72!"
              titles={['Danh sách thành viên', 'Tham gia công việc']}
              dataSource={transferDataSource}
              targetKeys={selectedMembers}
              onChange={setSelectedMembers}
              render={item => `${item.firstName} ${item.lastName}`}
            />
          )}
        </Form>

        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
