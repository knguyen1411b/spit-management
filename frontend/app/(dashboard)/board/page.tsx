import { searchParamsCache } from '@/libs/searchParams'
import { SearchParams } from 'nuqs/server'
import BoardMain from '@/features/board'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Quản lý ban - Spit Management'
}

export default async function UsersPage({ searchParams }: { searchParams: Promise<SearchParams> }) {
  searchParamsCache.parse(await searchParams)

  return <BoardMain />
}
