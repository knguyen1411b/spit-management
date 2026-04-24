import SemesterActivityProgress from './SemesterActivityProgress'
import MonthlySalesChart from './MonthlySalesChart'
import StatisticsChart from './StatisticsChart'
import { ClubMetrics } from './ClubMetrics'

export default function HomeMain() {
  return (
    <div className="grid grid-cols-12 gap-4 md:gap-6">
      <div className="col-span-12 space-y-6 xl:col-span-7">
        <ClubMetrics />

        <MonthlySalesChart />
      </div>

      <div className="col-span-12 xl:col-span-5">
        <SemesterActivityProgress />
      </div>

      <div className="col-span-12">
        <StatisticsChart />
      </div>
    </div>
  )
}
