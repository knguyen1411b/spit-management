'use client'

import {
  createRoleAction,
  deleteRoleAction,
  getListRolesAction,
  getRoleDetailAction,
  updateRoleAction
} from '@/services/api.service'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { IBasePremsDTO, IPageableMeta, ISearchParams } from '@/types'

export const useRoles = (params: ISearchParams = {}) => {
  const queryClient = useQueryClient()

  const listQuery = useQuery({
    queryKey: ['roles', params],
    queryFn: () => getListRolesAction(params)
  })

  const createMutation = useMutation({
    mutationFn: createRoleAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roles'] })
    }
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: any }) => updateRoleAction(id, data),
    onSuccess: (data, variables) => {
      queryClient.setQueryData(['roles', { id: variables.id }], data)
      queryClient.invalidateQueries({ queryKey: ['roles'] })
    }
  })

  const deleteMutation = useMutation({
    mutationFn: deleteRoleAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roles'] })
    }
  })

  const useRoleDetail = (id: number) =>
    useQuery({
      queryKey: ['roles', { id }],
      queryFn: () => getRoleDetailAction(id),
      enabled: !!id
    })

  return {
    roles: (listQuery.data?.data || []) as IBasePremsDTO[],
    meta: (listQuery.data?.meta ?? {}) as IPageableMeta,

    isLoading: listQuery.isLoading,
    isFetching: listQuery.isFetching,
    error: listQuery.error,

    refetch: listQuery.refetch,
    createRole: createMutation.mutateAsync,
    updateRole: updateMutation.mutateAsync,
    deleteRole: deleteMutation.mutateAsync,

    useRoleDetail
  }
}
