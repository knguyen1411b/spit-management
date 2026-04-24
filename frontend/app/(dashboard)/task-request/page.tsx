import { searchParamsCache } from '@/libs/searchParams'
import RequestMain from '@/features/request'
import { SearchParams } from 'nuqs/server'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Kiểm tra công việc - Spit Management'
}

export default async function UsersPage({ searchParams }: { searchParams: Promise<SearchParams> }) {
  searchParamsCache.parse(await searchParams)

  return <RequestMain />
}
