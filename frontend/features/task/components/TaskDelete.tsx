'use client'

import { Button, Input, Modal, Tooltip, Typography } from 'antd'
import ModalError from '@/components/common/ModalError'
import { DeleteOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { Fragment, useState } from 'react'
import { useTasks } from '@/hooks/data'
import { useToast } from '@/contexts'

export default function TaskDelete({ id, name }: { id: number; name: string }) {
  const { deleteTask } = useTasks()
  const { success } = useToast()

  const [isOpen, setIsOpen] = useState(false)
  const [inputValue, setInputValue] = useState('')
  const [loading, setLoading] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const openModal = () => {
    setInputValue('')
    setIsOpen(true)
  }

  const closeModal = () => {
    if (!loading) {
      setInputValue('')
      setIsOpen(false)
    }
  }

  const handleDelete = async () => {
    try {
      setLoading(true)
      await deleteTask(id)
      success('Xóa nhiệm vụ thành công')
      setIsOpen(false)
    } catch (err: any) {
      setModalError(err?.response?.data?.message || 'Xóa thất bại')
    } finally {
      setLoading(false)
    }
  }

  const confirmDisabled = inputValue.trim() !== name

  return (
    <Fragment>
      <Tooltip title="Xóa nhiệm vụ">
        <Button type="primary" danger size="small" icon={<DeleteOutlined />} onClick={openModal} />
      </Tooltip>

      <Modal
        open={isOpen}
        title={
          <Typography>
            <Title level={3}>Xoá nhiệm vụ</Title>
          </Typography>
        }
        okText="Xóa"
        cancelText="Hủy"
        centered
        okButtonProps={{ danger: true, disabled: confirmDisabled, loading }}
        cancelButtonProps={{ disabled: loading }}
        onOk={handleDelete}
        onCancel={closeModal}
      >
        <div className="space-y-4 text-sm text-gray-700">
          <p className="dark:text-white">Bạn có chắc chắn muốn xoá nhiệm vụ?</p>

          <p className="text-red-600 font-semibold">
            Hành động này sẽ xóa vĩnh viễn toàn bộ dữ liệu liên quan. Đây là thao tác <u>không thể hoàn tác</u>.
          </p>

          <div>
            <p className="text-gray-800 mb-4 dark:text-white">
              Vui lòng nhập <strong>{name}</strong> để xác nhận:
            </p>

            <Input
              placeholder={`Nhập "${name}" để xác nhận`}
              value={inputValue}
              onChange={e => setInputValue(e.target.value)}
            />
          </div>
          {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
        </div>
      </Modal>
    </Fragment>
  )
}
