'use client'

import {
  getTaskDetailAction,
  createTaskAction,
  getListTasksAction,
  updateTaskAction,
  deleteTaskAction,
  getMyTasksAction
} from '@/services/api.service'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { IMyTask, IPageableMeta, ISearchParams, ITask } from '@/types'

export const useTasks = (params: ISearchParams = {}) => {
  const queryClient = useQueryClient()

  const listQuery = useQuery({
    queryKey: ['tasks', params],
    queryFn: () => getListTasksAction(params)
  })

  const mylistQuery = useQuery({
    queryKey: ['my-tasks', params],
    queryFn: () => getMyTasksAction(params)
  })

  const createMutation = useMutation({
    mutationFn: createTaskAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks'] })
      queryClient.invalidateQueries({ queryKey: ['my-tasks'] })
    }
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<ITask> }) => updateTaskAction(id, data),
    onSuccess: (data, variables) => {
      queryClient.setQueryData(['tasks', { id: variables.id }], data)

      queryClient.invalidateQueries({ queryKey: ['tasks'] })
      queryClient.invalidateQueries({ queryKey: ['my-tasks'] })
    }
  })

  const deleteMutation = useMutation({
    mutationFn: deleteTaskAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks'] })
      queryClient.invalidateQueries({ queryKey: ['my-tasks'] })
    }
  })

  const useTaskDetail = (id: number) =>
    useQuery({
      queryKey: ['tasks', { id }],
      queryFn: () => getTaskDetailAction(id),
      enabled: !!id
    })

  return {
    tasks: (listQuery.data?.data ?? []) as ITask[],
    meta: (listQuery.data?.meta ?? {}) as IPageableMeta,

    myTasks: (mylistQuery.data?.data ?? []) as IMyTask[],
    myMeta: (mylistQuery.data?.meta ?? {}) as IPageableMeta,

    isLoading: listQuery.isLoading || mylistQuery.isLoading,
    isFetching: listQuery.isFetching || mylistQuery.isFetching,
    error: listQuery.error,

    refetch: listQuery.refetch,

    createTask: createMutation.mutateAsync,
    updateTask: updateMutation.mutateAsync,
    deleteTask: deleteMutation.mutateAsync,

    useTaskDetail
  }
}
