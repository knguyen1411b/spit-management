import { searchParamsCache } from '@/libs/searchParams'
import { SearchParams } from 'nuqs/server'
import RoleMain from '@/features/role'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Quản lý chức vụ - Spit Management'
}

export default async function RolesPage({ searchParams }: { searchParams: Promise<SearchParams> }) {
  searchParamsCache.parse(await searchParams)

  return <RoleMain />
}
