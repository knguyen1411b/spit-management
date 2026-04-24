import RoleDetail from '@/features/role/components/RoleDetail'
import { parseNumericParam } from '@/libs/parseNumericParam'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Chi tiết chức vụ - Spit Management'
}

type Props = {
  params: Promise<{ id: string }>
}

export default async function RolePage({ params }: Props) {
  const { id } = await params

  return <RoleDetail id={parseNumericParam(id)} />
}
