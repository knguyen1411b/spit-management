'use client'

import ReactApexChart from 'react-apexcharts'
import { useStatistics } from '@/hooks/data'
import { ApexOptions } from 'apexcharts'

export default function SemesterActivityProgress() {
  const { task } = useStatistics()

  const totalTasks = task?.totalTasks ?? 0
  const joinedTasks = task?.joinedTasks ?? 0
  const notJoinedTasks = task?.notJoinedTasks ?? 0

  const series = [totalTasks > 0 ? Number(((joinedTasks / (joinedTasks + notJoinedTasks)) * 100).toFixed(2)) : 0]

  const options: ApexOptions = {
    colors: ['#465FFF'],
    chart: {
      fontFamily: 'Outfit, sans-serif',
      type: 'radialBar',
      height: 330,
      sparkline: { enabled: true }
    },
    plotOptions: {
      radialBar: {
        startAngle: -85,
        endAngle: 85,
        hollow: { size: '80%' },
        track: {
          background: '#E4E7EC',
          strokeWidth: '100%',
          margin: 5
        },
        dataLabels: {
          name: { show: false },
          value: {
            fontSize: '36px',
            fontWeight: 600,
            offsetY: -40,
            color: '#1D2939',
            formatter: val => `${val}%`
          }
        }
      }
    },
    fill: {
      type: 'solid',
      colors: ['#465FFF']
    },
    stroke: { lineCap: 'round' },
    labels: ['Tỷ lệ tham gia']
  }

  return (
    <div className="rounded-2xl border border-gray-200 bg-gray-100 dark:border-gray-800 dark:bg-white/3">
      <div className="rounded-2xl bg-white px-5 pt-5 pb-11 shadow-sm dark:bg-gray-900 sm:px-6 sm:pt-6">
        <div>
          <h3 className="text-lg font-semibold text-gray-800 dark:text-white/90">Tham gia hoạt động</h3>
          <p className="mt-1 text-sm text-gray-500 dark:text-gray-400">Tỷ lệ tham gia hoạt động trong học kỳ</p>
        </div>

        <div className="relative mt-4">
          <div className="max-h-[330px]">
            <ReactApexChart options={options} series={series} type="radialBar" height={330} />
          </div>
        </div>

        <p className="mx-auto mt-8 max-w-[380px] text-center text-sm text-gray-500 sm:text-base">
          Học kỳ hiện tại có <span className="font-semibold text-gray-700 dark:text-white">{totalTasks}</span> hoạt động
          được tổ chức.
        </p>

        <p className="mx-auto mt-2 max-w-[380px] text-center text-sm text-gray-500 sm:text-base">
          Tổng số lượt tham gia là <span className="font-semibold text-gray-700 dark:text-white">{joinedTasks}</span>.
        </p>
      </div>

      <div className="flex items-center justify-center gap-6 px-6 py-4">
        <div className="text-center">
          <p className="text-xs text-gray-500 dark:text-gray-400">Chưa tham gia</p>
          <p className="mt-1 text-lg font-semibold text-red-600">{notJoinedTasks}</p>
        </div>

        <div className="h-7 w-px bg-gray-200 dark:bg-gray-800" />

        <div className="text-center">
          <p className="text-xs text-gray-500 dark:text-gray-400">Tham gia</p>
          <p className="mt-1 text-lg font-semibold text-green-600">{joinedTasks}</p>
        </div>
      </div>
    </div>
  )
}
