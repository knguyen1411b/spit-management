import UserDetail from '@/features/user/components/UserDetail'
import { parseNumericParam } from '@/libs/parseNumericParam'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Chi tiết người dùng - Spit Management'
}

type Props = {
  params: Promise<{ id: string }>
}

export default async function RolePage({ params }: Props) {
  const { id } = await params

  return <UserDetail id={parseNumericParam(id)} />
}
