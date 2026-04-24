'use client'

import {
  getListTaskRequestsAction,
  createTaskRequestAction,
  rejectTaskRequestAction,
  approveTaskRequestAction
} from '@/services/api.service'

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { ISearchParams, ITaskRequestGrouped } from '@/types'

export const useTaskRequests = (params: ISearchParams = {}) => {
  const queryClient = useQueryClient()

  const listQuery = useQuery({
    queryKey: ['task-requests', params],
    queryFn: () => getListTaskRequestsAction(params)
  })

  type CreateRequest = { id: number; description: string }
  const createMutation = useMutation({
    mutationFn: (data: CreateRequest) => createTaskRequestAction(data.id, data.description),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['task-requests'] })
      queryClient.invalidateQueries({ queryKey: ['my-tasks'] })
    }
  })

  const approveMutation = useMutation({
    mutationFn: (id: number) => approveTaskRequestAction(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['task-requests'] })
      queryClient.invalidateQueries({ queryKey: ['statistics-0'] })
    }
  })

  const rejectMutation = useMutation({
    mutationFn: (id: number) => rejectTaskRequestAction(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['task-requests'] })
      queryClient.invalidateQueries({ queryKey: ['statistics-0'] })
    }
  })

  return {
    requests: (listQuery.data?.data ?? {}) as ITaskRequestGrouped,

    isLoading: listQuery.isLoading,
    isFetching: listQuery.isFetching,
    error: listQuery.error,

    refetch: listQuery.refetch,

    createRequest: createMutation.mutateAsync,
    approveRequest: approveMutation.mutateAsync,
    rejectRequest: rejectMutation.mutateAsync
  }
}
