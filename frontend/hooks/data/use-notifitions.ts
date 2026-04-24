'use client'

import {
  getListNotifications,
  createNotification,
  readNotificationAction,
  getUnreadNotifications
} from '@/services/api.service'

import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import type { INotification, IPageableMeta, ISearchParams } from '@/types'

export const useNotifications = (params: ISearchParams = {}) => {
  const queryClient = useQueryClient()

  const listQuery = useQuery({
    queryKey: ['notifications', 'list', params],
    queryFn: () => getListNotifications(params)
  })

  const unreadQuery = useQuery({
    queryKey: ['notifications', 'unread-count'],
    queryFn: getUnreadNotifications
  })

  const createMutation = useMutation({
    mutationFn: createNotification,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] })
    }
  })

  const readMutation = useMutation({
    mutationFn: readNotificationAction,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] })
    }
  })

  return {
    notifications: (listQuery.data?.data ?? []) as INotification[],
    meta: (listQuery.data?.meta ?? {}) as IPageableMeta,
    unreadCount: unreadQuery.data?.data ?? 0,

    isLoading: listQuery.isLoading,
    isFetching: listQuery.isFetching,
    error: listQuery.error,

    refetch: listQuery.refetch,
    createNotification: createMutation.mutateAsync,
    markAsRead: readMutation.mutateAsync
  }
}
