'use client'

import { Button, Input, Modal, Tooltip, Typography } from 'antd'
import ModalError from '@/components/common/ModalError'
import { Fragment, useCallback, useState } from 'react'
import { DeleteOutlined } from '@ant-design/icons'
import { useBoardMembers } from '@/hooks/data'
import Title from 'antd/es/typography/Title'
import { useToast } from '@/contexts'

interface BoardDeleteProps {
  boardId: number
  memberId: number
  name: string
}

export default function BoardMemberDelete({ boardId, memberId, name }: BoardDeleteProps) {
  const { removeMember } = useBoardMembers(boardId)
  const { success } = useToast()

  const [isOpen, setIsOpen] = useState(false)
  const [inputValue, setInputValue] = useState('')
  const [loading, setLoading] = useState(false)
  const [modalError, setModalError] = useState<string | null>(null)

  const openModal = useCallback(() => {
    setInputValue('')
    setIsOpen(true)
    setModalError(null)
  }, [])

  const closeModal = useCallback(() => {
    if (!loading) {
      setInputValue('')
      setIsOpen(false)
    }
  }, [loading])

  const handleDelete = useCallback(async () => {
    try {
      setLoading(true)
      await removeMember(memberId)
      success('Xóa thành viên khỏi ban thành công')
      setIsOpen(false)
    } catch (err: any) {
      setModalError(err?.response?.data?.message || 'Xóa thành viên khỏi ban thất bại')
    } finally {
      setLoading(false)
    }
  }, [removeMember, memberId, success])

  const confirmDisabled = inputValue.trim() !== name

  return (
    <Fragment>
      <Tooltip title="Xóa thành viên khỏi ban">
        <Button type="primary" danger size="small" icon={<DeleteOutlined />} onClick={openModal} />
      </Tooltip>

      <Modal
        open={isOpen}
        title={
          <Typography>
            <Title level={3}>Xoá thành viên khỏi ban</Title>
          </Typography>
        }
        centered
        okText="Xóa"
        cancelText="Hủy"
        okButtonProps={{ danger: true, disabled: confirmDisabled, loading }}
        cancelButtonProps={{ disabled: loading }}
        onOk={handleDelete}
        onCancel={closeModal}
      >
        <div className="space-y-4 text-sm text-gray-700">
          <p className="dark:text-white">
            Bạn có chắc chắn muốn xóa thành viên <strong>{name}</strong> khỏi ban này không?
          </p>

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
