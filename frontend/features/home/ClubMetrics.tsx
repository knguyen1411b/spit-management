'use client'

import { Users, CalendarDays, ClipboardList } from 'lucide-react'
import { useSemesters, useStatistics } from '@/hooks/data'
import { useAuth } from '@/contexts'
import dayjs from 'dayjs'

export const ClubMetrics = () => {
  const { task, member } = useStatistics()
  const { profile } = useAuth()

  const semesterId = profile?.semesterId || 1
  const { data: currentSemester } = useSemesters().useSemesterDetail(semesterId)

  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
      <div
        className="col-span-1 sm:col-span-2 rounded-2xl border border-gray-200 bg-white p-6 shadow-sm transition
        hover:shadow-md dark:border-gray-800 dark:bg-white/5"
      >
        <MetricIcon>
          <CalendarDays />
        </MetricIcon>

        <div className="mt-4 space-y-1">
          <p className="text-sm text-gray-500 dark:text-gray-400">Học kỳ hiện tại</p>

          <h3 className="text-lg font-semibold text-gray-900 dark:text-white">{currentSemester?.data?.name}</h3>

          <p className="text-sm text-gray-600 dark:text-gray-400">
            {dayjs(currentSemester?.data?.startDate).format('DD/MM/YYYY')} –{' '}
            {dayjs(currentSemester?.data?.endDate).format('DD/MM/YYYY')}
          </p>
        </div>
      </div>

      <MetricCard icon={<Users />} label="Tổng thành viên" value={member.totalMemberOfSemester} />

      <MetricCard icon={<ClipboardList />} label="Sự kiện - Công việc" value={task.totalTasks} />
    </div>
  )
}

function MetricCard({ icon, label, value }: { icon: React.ReactNode; label: string; value?: React.ReactNode }) {
  return (
    <div
      className="rounded-2xl border border-gray-200 bg-white p-6 shadow-sm transition
      hover:shadow-md dark:border-gray-800 dark:bg-white/5"
    >
      <MetricIcon>{icon}</MetricIcon>

      <div className="mt-4">
        <p className="text-sm text-gray-500 dark:text-gray-400">{label}</p>
        <h3 className="mt-1 text-2xl font-bold text-gray-900 dark:text-white">{value ?? 0}</h3>
      </div>
    </div>
  )
}

function MetricIcon({ children }: { children: React.ReactNode }) {
  return (
    <div
      className="flex h-12 w-12 items-center justify-center rounded-xl
      bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-white"
    >
      {children}
    </div>
  )
}
