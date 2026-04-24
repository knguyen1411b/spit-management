import { getAllPermissions, getPermissionsDetailAction } from '@/services/api.service'
import { IBasePremsDTO, IPageableMeta, ISearchParams } from '@/types'
import { useQuery } from '@tanstack/react-query'

export const usePermissions = (params: ISearchParams = {}) => {
  const query = useQuery({
    queryKey: ['permissions', params],
    queryFn: () =>
      getAllPermissions({
        size: 2000,
        ...params
      })
  })

  const usePermissionDetail = (id: number) =>
    useQuery({
      queryKey: ['roles', { id }],
      queryFn: () => getPermissionsDetailAction(id),
      enabled: !!id
    })

  return {
    perms: (query.data?.data || []) as IBasePremsDTO[],
    meta: (query.data?.meta ?? {}) as IPageableMeta,
    isLoading: query.isLoading,
    refetch: query.refetch,
    usePermissionDetail
  }
}
