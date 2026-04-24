'use client'

import {
  getMemberDetailAction,
  createMemberAction,
  getListMembersAction,
  updateMemberAction,
  deleteMemberAction
} from '@/services/api.service'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { IMember, IPageableMeta, ISearchParams } from '@/types'

export const useMembers = (params: ISearchParams = {}) => {
  const queryClient = useQueryClient()

  const listQuery = useQuery({
    queryKey: ['members', params],
    queryFn: () => getListMembersAction(params)
  })

  const createMutation = useMutation({
    mutationFn: createMemberAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['members'] })
    }
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: any }) => updateMemberAction(id, data),
    onSuccess: (data, variables) => {
      queryClient.setQueryData(['members', { id: variables.id }], data)
      queryClient.invalidateQueries({ queryKey: ['members'] })
    }
  })

  const deleteMutation = useMutation({
    mutationFn: deleteMemberAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['members'] })
    }
  })

  const useMemberDetail = (id: number) =>
    useQuery({
      queryKey: ['members', { id }],
      queryFn: () => getMemberDetailAction(id),
      enabled: !!id
    })

  return {
    members: (listQuery.data?.data ?? []) as IMember[],
    meta: (listQuery.data?.meta ?? {}) as IPageableMeta,

    isLoading: listQuery.isLoading,
    isFetching: listQuery.isFetching,
    error: listQuery.error,

    refetch: listQuery.refetch,

    createMember: createMutation.mutateAsync,
    updateMember: updateMutation.mutateAsync,
    deleteMember: deleteMutation.mutateAsync,

    useMemberDetail
  }
}
