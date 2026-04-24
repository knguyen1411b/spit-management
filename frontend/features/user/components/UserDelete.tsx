'use client'

import { Button, Input, Modal, Tooltip, Typography } from 'antd'
import ModalError from '@/components/common/ModalError'
import { Fragment, useCallback, useState } from 'react'
import { DeleteOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { useUsers } from '@/hooks/data'
import { useToast } from '@/contexts'

export default function UserDelete({ id, username, superuser }: { id: number; username: string; superuser: boolean }) {
  const { deleteUser } = useUsers()
  const { success } = useToast()

  const [isOpen, setIsOpen] = useState(false)
  const [inputValue, setInputValue] = useState('')
  const [loading, setLoading] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const openModal = useCallback(() => {
    setInputValue('')
    setModalError(null)
    setIsOpen(true)
  }, [])

  const closeModal = useCallback(() => {
    if (!loading) {
      setInputValue('')
      setModalError(null)
      setIsOpen(false)
    }
  }, [loading])

  const handleDelete = useCallback(async () => {
    if (superuser) {
      setIsOpen(false)
      setModalError('Không thể xóa người dùng quản trị hệ thống')
      return
    }
    try {
      setLoading(true)
      await deleteUser(id)
      success('Xóa người dùng thành công')
      setIsOpen(false)
    } catch (err: any) {
      setModalError(err?.response?.data?.message || 'Xóa thất bại')
    } finally {
      setLoading(false)
    }
  }, [id, superuser, success, deleteUser])

  const confirmDisabled = inputValue.trim() !== username

  return (
    <Fragment>
      <Tooltip title="Xóa người dùng">
        <Button type="primary" danger size="small" icon={<DeleteOutlined />} onClick={openModal} />
      </Tooltip>

      <Modal
        open={isOpen}
        title={
          <Typography>
            <Title level={3}>Xoá người dùng</Title>
          </Typography>
        }
        okText="Xóa"
        cancelText="Hủy"
        width={500}
        centered
        okButtonProps={{ danger: true, disabled: confirmDisabled, loading }}
        cancelButtonProps={{ disabled: loading }}
        onOk={handleDelete}
        onCancel={closeModal}
      >
        <div className="space-y-4 text-sm text-gray-700">
          <p className="dark:text-white">Bạn có chắc chắn muốn xóa người dùng?</p>

          <p className="text-red-600 font-semibold">
            Hành động này sẽ xóa vĩnh viễn toàn bộ dữ liệu liên quan. Đây là thao tác <u>không thể hoàn tác</u>.
          </p>

          <div>
            <p className="text-gray-800 mb-4 dark:text-white">
              Vui lòng nhập <strong>{username}</strong> để xác nhận:
            </p>

            <Input
              placeholder={`Nhập "${username}" để xác nhận`}
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
