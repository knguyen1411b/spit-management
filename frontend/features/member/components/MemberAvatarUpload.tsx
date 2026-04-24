'use client'

import { UserOutlined, CameraOutlined } from '@ant-design/icons'
import { uploadAvatar } from '@/services/avatar.service'
import { useQueryClient } from '@tanstack/react-query'
import { Permissions } from '@/constants/permission'
import { useAuth, useToast } from '@/contexts'
import { useRef } from 'react'
import { Avatar } from 'antd'

export default function MemberAvatarUpload({ memberId, avatarUrl }: { memberId: number; avatarUrl?: string }) {
  const { hasPermission, refetch } = useAuth()
  const queryClient = useQueryClient()
  const { success, error } = useToast()
  const inputRef = useRef<HTMLInputElement>(null)

  const canEdit = hasPermission([Permissions.Member.UPDATE])

  const handleClick = () => {
    if (!canEdit) return
    inputRef.current?.click()
  }

  const handleChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (!file) return

    if (!file.type.startsWith('image/')) {
      error('Chỉ được upload ảnh')
      return
    }

    try {
      await uploadAvatar(memberId, file)
      success('Cập nhật avatar thành công')
      await refetch()
      queryClient.invalidateQueries({ queryKey: ['members'] })
    } catch {
      error('Upload avatar thất bại')
    }
  }

  return (
    <>
      <div className={`relative inline-block ${canEdit ? 'cursor-pointer group' : ''}`} onClick={handleClick}>
        <Avatar size={100} src={avatarUrl} icon={<UserOutlined />} />

        {canEdit && (
          <div className="absolute inset-0 rounded-full bg-black/40 opacity-0 group-hover:opacity-100 flex items-center justify-center transition">
            <CameraOutlined className="text-white text-xl" />
          </div>
        )}
      </div>

      <input ref={inputRef} type="file" accept="image/*" hidden onChange={handleChange} />
    </>
  )
}
