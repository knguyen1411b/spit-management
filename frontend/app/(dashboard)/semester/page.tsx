import { searchParamsCache } from '@/libs/searchParams'
import SemesterMain from '@/features/semester'
import { SearchParams } from 'nuqs/server'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Quản lý kì học - Spit Management'
}

export default async function UsersPage({ searchParams }: { searchParams: Promise<SearchParams> }) {
  searchParamsCache.parse(await searchParams)

  return <SemesterMain />
}
