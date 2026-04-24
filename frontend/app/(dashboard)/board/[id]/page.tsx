import BoardDetail from '@/features/board/components/BoardDetail'
import { parseNumericParam } from '@/libs/parseNumericParam'

export const metadata = {
  title: 'Chi tiết ban - Spit Management'
}

type Props = {
  params: Promise<{ id: string }>
}

export default async function RolePage({ params }: Props) {
  const { id } = await params

  return <BoardDetail id={parseNumericParam(id)} />
}
