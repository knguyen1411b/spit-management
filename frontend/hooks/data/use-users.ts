'use client'

import {
  createUserAction,
  deleteUserAction,
  getListUsersAction,
  getUserDetailAction,
  updateUserAction
} from '@/services/api.service'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { IPageableMeta, ISearchParams, IUser } from '@/types'

export const useUsers = (params: ISearchParams = {}) => {
  const queryClient = useQueryClient()

  const listQuery = useQuery({
    queryKey: ['users', params],
    queryFn: () => getListUsersAction(params)
  })

  const createMutation = useMutation({
    mutationFn: createUserAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] })
    }
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: any }) => updateUserAction(id, data),
    onSuccess: (data, variables) => {
      queryClient.setQueryData(['users', { id: variables.id }], data)
      queryClient.invalidateQueries({ queryKey: ['users'] })
    }
  })

  const deleteMutation = useMutation({
    mutationFn: deleteUserAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] })
    }
  })

  const useUserDetail = (id: number) =>
    useQuery({
      queryKey: ['users', { id }],
      queryFn: () => getUserDetailAction(id),
      enabled: !!id
    })

  return {
    users: (listQuery.data?.data ?? []) as IUser[],
    meta: (listQuery.data?.meta ?? {}) as IPageableMeta,

    isLoading: listQuery.isLoading,
    isFetching: listQuery.isFetching,
    error: listQuery.error,

    refetch: listQuery.refetch,

    createUser: createMutation.mutateAsync,
    updateUser: updateMutation.mutateAsync,
    deleteUser: deleteMutation.mutateAsync,

    useUserDetail
  }
}
