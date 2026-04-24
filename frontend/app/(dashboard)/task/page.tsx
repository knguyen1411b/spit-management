import { searchParamsCache } from '@/libs/searchParams'
import { SearchParams } from 'nuqs/server'
import TaskMain from '@/features/task'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Quản lý công việc - Spit Management'
}

export default async function UsersPage({ searchParams }: { searchParams: Promise<SearchParams> }) {
  searchParamsCache.parse(await searchParams)

  return <TaskMain />
}
