'use client'

import {
  getBoardDetailAction,
  createBoardAction,
  getListBoardsAction,
  updateBoardAction,
  deleteBoardAction
} from '@/services/api.service'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { ISearchParams, IBoard, IPageableMeta } from '@/types'

export const useBoards = (params: ISearchParams = {}) => {
  const queryClient = useQueryClient()

  const listQuery = useQuery({
    queryKey: ['boards', params],
    queryFn: () => getListBoardsAction(params)
  })

  const createMutation = useMutation({
    mutationFn: createBoardAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['boards'] })
    }
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<IBoard> }) => updateBoardAction(id, data),
    onSuccess: (data, variables) => {
      queryClient.setQueryData(['boards', { id: variables.id }], data)

      queryClient.invalidateQueries({ queryKey: ['boards'] })
    }
  })

  const deleteMutation = useMutation({
    mutationFn: deleteBoardAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['boards'] })
    }
  })

  const useBoardDetail = (id: number) =>
    useQuery({
      queryKey: ['boards', { id }],
      queryFn: () => getBoardDetailAction(id),
      enabled: !!id
    })

  return {
    boards: (listQuery.data?.data ?? []) as IBoard[],
    meta: (listQuery.data?.meta ?? {}) as IPageableMeta,

    isLoading: listQuery.isLoading,
    isFetching: listQuery.isFetching,
    error: listQuery.error,

    refetch: listQuery.refetch,

    createBoard: createMutation.mutateAsync,
    updateBoard: updateMutation.mutateAsync,
    deleteBoard: deleteMutation.mutateAsync,

    useBoardDetail
  }
}
