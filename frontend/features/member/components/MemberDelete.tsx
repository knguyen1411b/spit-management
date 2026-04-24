'use client'

import { Button, Input, Modal, Tooltip, Typography } from 'antd'
import ModalError from '@/components/common/ModalError'
import { Fragment, useCallback, useState } from 'react'
import { DeleteOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { useMembers } from '@/hooks/data'
import { useToast } from '@/contexts'

export default function MemberDelete({ id, fullname }: { id: number; fullname: string }) {
  const { deleteMember } = useMembers()
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
    try {
      setLoading(true)
      await deleteMember(id)
      success('Xóa thành viên thành công')
      setIsOpen(false)
    } catch (err: any) {
      setModalError(err?.response?.data?.message || 'Xóa thành viên thất bại')
    } finally {
      setLoading(false)
    }
  }, [id, deleteMember, success])

  const confirmDisabled = inputValue.trim() !== fullname

  return (
    <Fragment>
      <Tooltip title="Xóa thành viên">
        <Button type="primary" danger size="small" icon={<DeleteOutlined />} onClick={openModal} />
      </Tooltip>

      <Modal
        open={isOpen}
        title={
          <Typography>
            <Title level={3}>Xoá thành viên</Title>
          </Typography>
        }
        okText="Xóa"
        centered
        cancelText="Hủy"
        okButtonProps={{ danger: true, disabled: confirmDisabled, loading }}
        cancelButtonProps={{ disabled: loading }}
        onOk={handleDelete}
        onCancel={closeModal}
      >
        <div className="space-y-4 text-sm text-gray-700">
          <p className="dark:text-white">
            Bạn có chắc chắn muốn xóa thành viên <strong>{fullname}</strong>?
          </p>

          <p className="text-red-600 font-semibold">
            Hành động này sẽ xóa vĩnh viễn toàn bộ dữ liệu liên quan. Đây là thao tác <u>không thể hoàn tác</u>.
          </p>

          <div>
            <p className="text-gray-800 mb-4 dark:text-white">
              Vui lòng nhập <strong>{fullname}</strong> để xác nhận:
            </p>

            <Input
              placeholder={`Nhập "${fullname}" để xác nhận`}
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
