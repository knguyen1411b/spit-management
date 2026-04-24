import { searchParamsCache } from '@/libs/searchParams'
import NotificationMain from '@/features/notification'
import { SearchParams } from 'nuqs/server'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Thông báo - Spit Management'
}

export default async function UsersPage({ searchParams }: { searchParams: Promise<SearchParams> }) {
  searchParamsCache.parse(await searchParams)

  return <NotificationMain />
}
