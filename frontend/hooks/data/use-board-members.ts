'use client'

import {
  getListBoardMembersAction,
  addBoardMemberAction,
  removeBoardMemberAction,
  updateBoardMemberAction,
  getBoardMemberDetailAction
} from '@/services/api.service'

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { ISearchParams, IBoardMember, IPageableMeta } from '@/types'

export const useBoardMembers = (boardId: number, params: ISearchParams = {}) => {
  const queryClient = useQueryClient()

  const listQuery = useQuery({
    queryKey: ['board-members', boardId, params],
    queryFn: () => getListBoardMembersAction(boardId, params),
    enabled: !!boardId
  })

  const createMutation = useMutation({
    mutationFn: (data: Partial<IBoardMember>) => addBoardMemberAction(boardId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['board-members', boardId] })
    }
  })

  const updateMutation = useMutation({
    mutationFn: ({ memberId, data }: { memberId: number; data: Partial<IBoardMember> }) =>
      updateBoardMemberAction(boardId, memberId, data),
    onSuccess: (data, variables) => {
      queryClient.setQueryData(['board-members', boardId, { id: variables.memberId }], data)
      queryClient.invalidateQueries({ queryKey: ['board-members', boardId] })
    }
  })

  const deleteMutation = useMutation({
    mutationFn: (memberId: number) => removeBoardMemberAction(boardId, memberId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['board-members', boardId] })
    }
  })

  const useBoardMemberDetail = (memberId: number) =>
    useQuery({
      queryKey: ['board-members', boardId, { id: memberId }],
      queryFn: () => getBoardMemberDetailAction(boardId, memberId),
      enabled: !!memberId
    })

  return {
    members: (listQuery.data?.data ?? []) as IBoardMember[],
    meta: (listQuery.data?.meta ?? {}) as IPageableMeta,

    isLoading: listQuery.isLoading,
    isFetching: listQuery.isFetching,
    error: listQuery.error,

    refetch: listQuery.refetch,

    addMember: createMutation.mutateAsync,
    updateMember: updateMutation.mutateAsync,
    removeMember: deleteMutation.mutateAsync,

    useBoardMemberDetail
  }
}
