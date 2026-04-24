'use client'

import { Alert } from 'antd'

interface ModalErrorProps {
  title: string
  description?: string | null
  onClose?: () => void
  className?: string
}

export default function ModalError({ title = 'Có lỗi xảy ra', description, onClose, className }: ModalErrorProps) {
  return (
    <Alert
      type="error"
      title={title}
      description={description}
      showIcon
      className={className}
      closable={
        onClose
          ? {
              closeIcon: true,
              onClose
            }
          : false
      }
    />
  )
}
