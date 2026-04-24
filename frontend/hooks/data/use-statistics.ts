'use client'

import { getStatisticTasksAction, getStatisticMembersAction } from '@/services/api.service'
import { useQuery } from '@tanstack/react-query'

interface ChartPoint {
  label: string
  value: number
}
interface TaskStatistics {
  totalTasks: number
  joinedTasks: number
  notJoinedTasks: number
  taskMembersPerMonth: ChartPoint[]
  tasksPerMonth: ChartPoint[]
}
interface MemberStatistics {
  totalMemberOfSemester: number
  memberCountBySemesters: {
    semesterId: number
    totalMembers: number
  }[]
}

export const useStatistics = () => {
  const { data: taskStatistics, isLoading: isLoadingTaskStatistics } = useQuery({
    queryKey: ['statistics-0'],
    queryFn: () => getStatisticTasksAction()
  })
  const { data: memberStatistics, isLoading: isLoadingMemberStatistics } = useQuery({
    queryKey: ['statistics-1'],
    queryFn: () => getStatisticMembersAction()
  })
  return {
    task: (taskStatistics?.data || {}) as TaskStatistics,
    member: (memberStatistics?.data || {}) as MemberStatistics,
    isLoading: isLoadingTaskStatistics || isLoadingMemberStatistics
  }
}
