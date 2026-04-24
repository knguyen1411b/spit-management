import SemesterDetail from '@/features/semester/components/SemesterDetail'
import { parseNumericParam } from '@/libs/parseNumericParam'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Chi tiết kì học - Spit Management'
}

type Props = {
  params: Promise<{ id: string }>
}

export default async function RolePage({ params }: Props) {
  const { id } = await params

  return <SemesterDetail id={parseNumericParam(id)} />
}
