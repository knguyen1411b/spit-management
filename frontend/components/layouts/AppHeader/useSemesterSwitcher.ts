'use client'

import { useUpdateUserSemester } from '@/hooks/data/use-update-user-semester'
import { useAuth, useToast } from '@/contexts'
import { useSemesters } from '@/hooks/data'

export const useSemesterSwitcher = () => {
  const { profile, refetch } = useAuth()
  const { updateSemesterId } = useUpdateUserSemester()
  const { success, error } = useToast()
  const { semesters, useSemesterDetail } = useSemesters()

  const semesterRes = useSemesterDetail(profile?.semesterId || 1)
  const currentSemester = semesterRes?.data?.data

  const changeSemester = async (semesterId: number) => {
    if (!profile) return
    try {
      const res = await updateSemesterId({ id: profile.id, semesterId })
      console.log('semesterId', res)
      await refetch()
      success('Thay đổi học kì thành công!')
    } catch {
      error('Đổi kỳ học thất bại!')
    }
  }

  return {
    semesters,
    currentSemester,
    changeSemester
  }
}
