import MemberDetail from '@/features/member/components/MemberDetail'
import { parseNumericParam } from '@/libs/parseNumericParam'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Chi tiết thành viên - Spit Management'
}

type Props = {
  params: Promise<{ id: string }>
}

export default async function RolePage({ params }: Props) {
  const { id } = await params

  return <MemberDetail id={parseNumericParam(id)} />
}
