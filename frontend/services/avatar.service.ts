'use server'

import { ACCESS_TOKEN_COOKIE_NAME } from '@/constants/cookies'
import { IBaseApiResponse } from '@/types'
import { cookies } from 'next/headers'
import { api } from '@/libs/api'

export const uploadAvatar = async (id: number, file: File) => {
  const cookieStore = await cookies()
  const accessToken = cookieStore.get(ACCESS_TOKEN_COOKIE_NAME)?.value

  const formData = new FormData()
  formData.append('avatarFile', file)

  const res = await api.post<IBaseApiResponse>(`/api/v1/member/${id}/avatar`, formData, {
    headers: {
      Authorization: `Bearer ${accessToken}`
    }
  })

  return res
}

export const uploadAvatarUser = async (file: File) => {
  const cookieStore = await cookies()
  const accessToken = cookieStore.get(ACCESS_TOKEN_COOKIE_NAME)?.value

  const formData = new FormData()
  formData.append('avatarFile', file)

  const res = await api.post<IBaseApiResponse>('/api/v1/auth/avatar', formData, {
    headers: {
      Authorization: `Bearer ${accessToken}`
    }
  })

  return res
}
