'use client'

import { useMutation, useQueryClient } from '@tanstack/react-query'
import { updateUserSemesterAction } from '@/services/api.service'

export const useUpdateUserSemester = () => {
  const queryClient = useQueryClient()

  const updateSemesterId = useMutation({
    mutationFn: ({ id, semesterId }: { id: number; semesterId: number }) => updateUserSemesterAction(id, semesterId),
    onSuccess: () => {
      ;[
        'members',
        'boards',
        'tasks',
        'my-tasks',
        'task-requests',
        'board-members',
        'statistics-0',
        'statistics-1'
      ].forEach(key => {
        queryClient.invalidateQueries({ queryKey: [key] })
      })
    }
  })

  return {
    updateSemesterId: updateSemesterId.mutateAsync
  }
}
