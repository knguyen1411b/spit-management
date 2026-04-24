'use client'

import {
  getSemesterDetailAction,
  createSemesterAction,
  getListSemestersAction,
  updateSemesterAction,
  deleteSemesterAction,
  copySemesterAction
} from '@/services/api.service'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { IPageableMeta, ISearchParams, ISemester } from '@/types'

export const useSemesters = (params: ISearchParams = {}) => {
  const queryClient = useQueryClient()

  const listQuery = useQuery({
    queryKey: ['semesters', params],
    queryFn: () => getListSemestersAction(params)
  })

  const createMutation = useMutation({
    mutationFn: createSemesterAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['semesters'] })
      queryClient.invalidateQueries({ queryKey: ['members'] })
    }
  })

  const copyMutation = useMutation({
    mutationFn: copySemesterAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['semesters'] })
    }
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<ISemester> }) => updateSemesterAction(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['semesters'] })
      queryClient.invalidateQueries({ queryKey: ['members'] })
    }
  })

  const deleteMutation = useMutation({
    mutationFn: deleteSemesterAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['semesters'] })
    }
  })

  const useSemesterDetail = (id: number) =>
    useQuery({
      queryKey: ['semesters', { id }],
      queryFn: () => getSemesterDetailAction(id),
      enabled: !!id
    })

  return {
    semesters: (listQuery.data?.data ?? []) as ISemester[],
    meta: (listQuery.data?.meta ?? {}) as IPageableMeta,

    isLoading: listQuery.isLoading,
    isFetching: listQuery.isFetching,
    error: listQuery.error,

    refetch: listQuery.refetch,

    createSemester: createMutation.mutateAsync,
    copySemester: copyMutation.mutateAsync,
    updateSemester: updateMutation.mutateAsync,
    deleteSemester: deleteMutation.mutateAsync,

    useSemesterDetail
  }
}
