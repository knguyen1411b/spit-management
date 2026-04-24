'use client'

import ReactApexChart from 'react-apexcharts'
import type { ApexOptions } from 'apexcharts'
import { useStatistics } from '@/hooks/data'

export default function MonthlyMemberActivityChart() {
  const { task } = useStatistics()
  const members = task?.taskMembersPerMonth ?? []
  const options: ApexOptions = {
    colors: ['#465fff'],
    chart: {
      fontFamily: 'Outfit, sans-serif',
      type: 'bar',
      height: 180,
      toolbar: { show: false }
    },
    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: '38%',
        borderRadius: 5,
        borderRadiusApplication: 'end'
      }
    },
    dataLabels: { enabled: false },
    stroke: {
      show: true,
      width: 1,
      colors: ['transparent']
    },
    xaxis: {
      categories: members.map(item => item.label.split(' ')[0]),
      axisBorder: { show: false },
      axisTicks: { show: false }
    },
    grid: {
      yaxis: {
        lines: { show: true }
      }
    },
    yaxis: {
      forceNiceScale: true
    },
    fill: { opacity: 1 },
    tooltip: {
      y: {
        formatter: (val: number) => `${val} thành viên`
      }
    },
    legend: {
      show: true,
      position: 'top',
      horizontalAlign: 'left'
    }
  }

  const series = [
    {
      name: 'Thành viên tham gia',
      data: members.map(item => item.value)
    }
  ]

  return (
    <div className="overflow-hidden rounded-2xl border border-gray-200 bg-white px-5 pt-5 dark:border-gray-800 dark:bg-white/3 sm:px-6 sm:pt-6">
      <div className="flex items-center justify-between">
        <h3 className="text-lg font-semibold text-gray-800 dark:text-white/90">Thành viên hoạt động theo tháng</h3>
      </div>

      <div className="max-w-full overflow-x-auto custom-scrollbar">
        <div className="-ml-5 min-w-[650px] xl:min-w-full pl-2">
          <ReactApexChart options={options} series={series} type="bar" height={180} />
        </div>
      </div>
    </div>
  )
}
