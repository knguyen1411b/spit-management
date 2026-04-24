import ScheduleMain from '@/features/schedule'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Lịch công tác - Spit Management'
}

export default function SchedulePage() {
  return <ScheduleMain />
}
