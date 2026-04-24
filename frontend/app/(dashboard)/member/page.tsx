import { searchParamsCache } from '@/libs/searchParams'
import MemberMain from '@/features/member'
import { SearchParams } from 'nuqs/server'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Quản lý thành viên - Spit Management'
}

export default async function UsersPage({ searchParams }: { searchParams: Promise<SearchParams> }) {
  searchParamsCache.parse(await searchParams)

  return <MemberMain />
}
