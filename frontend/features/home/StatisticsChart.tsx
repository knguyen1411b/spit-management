'use client'

import ReactApexChart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'
import { useStatistics } from '@/hooks/data'
import { useMemo } from 'react'

export default function ClubStatisticsChart() {
  const { task } = useStatistics()

  const chartData = useMemo(() => {
    const members = task?.taskMembersPerMonth ?? []
    const events = task?.tasksPerMonth ?? []

    return {
      categories: members.map(item => item.label),
      members: members.map(item => item.value),
      events: events.map(item => item.value)
    }
  }, [task])

  const options: ApexOptions = {
    colors: ['#465FFF', '#9CB9FF'],
    chart: {
      fontFamily: 'Outfit, sans-serif',
      height: 310,
      type: 'area',
      toolbar: { show: false }
    },
    legend: { show: false },
    stroke: {
      curve: 'smooth',
      width: [2, 2]
    },
    fill: {
      type: 'gradient',
      gradient: {
        opacityFrom: 0.5,
        opacityTo: 0
      }
    },
    markers: {
      size: 0,
      hover: { size: 0 }
    },
    grid: {
      yaxis: { lines: { show: true } },
      xaxis: { lines: { show: false } }
    },
    dataLabels: { enabled: false },
    tooltip: {
      shared: true,
      intersect: false,
      y: {
        formatter: (val: number, { seriesIndex }) => (seriesIndex === 0 ? `${val} thành viên` : `${val} sự kiện`)
      }
    },
    xaxis: {
      categories: chartData.categories,
      tooltip: { enabled: false },
      axisBorder: { show: false },
      axisTicks: { show: false }
    },
    yaxis: {
      labels: {
        style: { fontSize: '12px', colors: ['#6B7280'] }
      }
    }
  }

  const series = [
    {
      name: 'Hội viên tham gia',
      data: chartData.members
    },
    {
      name: 'Sự kiện CLB',
      data: chartData.events
    }
  ]

  return (
    <div className="rounded-2xl border border-gray-200 bg-white px-5 pb-5 pt-5 dark:border-gray-800 dark:bg-white/3 sm:px-6 sm:pt-6">
      <div className="mb-6 flex flex-col gap-5 sm:flex-row sm:justify-between">
        <div>
          <h3 className="text-lg font-semibold text-gray-800 dark:text-white/90">Thống kê hoạt động CLB</h3>
          <p className="mt-1 text-theme-sm text-gray-500 dark:text-gray-400">
            Tổng hợp hội viên và sự kiện theo thời gian
          </p>
        </div>
      </div>

      <div className="max-w-full overflow-x-auto custom-scrollbar">
        <div className="min-w-[1000px] xl:min-w-full">
          <ReactApexChart options={options} series={series} type="area" height={310} />
        </div>
      </div>
    </div>
  )
}
