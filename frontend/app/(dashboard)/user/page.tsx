import { searchParamsCache } from '@/libs/searchParams'
import { SearchParams } from 'nuqs/server'
import UserMain from '@/features/user'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Quản lý người dùng - Spit Management'
}

export default async function UsersPage({ searchParams }: { searchParams: Promise<SearchParams> }) {
  searchParamsCache.parse(await searchParams)

  return <UserMain />
}
