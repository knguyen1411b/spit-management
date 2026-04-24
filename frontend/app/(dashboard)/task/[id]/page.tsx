import TaskDetail from '@/features/task/components/TaskDetail'
import { parseNumericParam } from '@/libs/parseNumericParam'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Chi tiết công việc - Spit Management'
}

type Props = {
  params: Promise<{ id: string }>
}

export default async function TaskPage({ params }: Props) {
  const { id } = await params

  return <TaskDetail id={parseNumericParam(id)} />
}
