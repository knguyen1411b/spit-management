'use client'

import { ModeToggle } from '@/components/ui/ModeToggle'
import { SemesterSwitcher } from './SemesterSwitcher'
import { GlobalSearch } from './GlobalSearch'
import { Breadcrumb } from './Breadcrumb'
import { UserMenu } from './UserMenu'

export const AppHeader = () => {
  return (
    <header className="sticky top-0 w-full shadow-sm bg-background z-50">
      <div className="flex items-center px-4 py-2 lg:px-6 lg:py-3">
        <div className="flex items-center">
          <Breadcrumb />
        </div>

        <div className="flex flex-1 justify-center px-6">{/* <GlobalSearch /> */}</div>

        <div className="flex items-center gap-3">
          <SemesterSwitcher />
          <ModeToggle />
          <UserMenu />
        </div>
      </div>
    </header>
  )
}
