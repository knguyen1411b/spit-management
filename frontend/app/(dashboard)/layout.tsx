'use client'

import { AppHeader, AppSidebar, Backdrop } from '@/components/layouts'
import { useSidebar } from '@/contexts'

export default function Layout({ children }: { children: React.ReactNode }) {
  const { isExpanded, isMobileOpen } = useSidebar()

  const mainContentMargin = isMobileOpen ? 'ml-0' : isExpanded ? 'lg:ml-[290px]' : 'lg:ml-[90px]'

  return (
    <div className="min-h-screen flex">
      <AppSidebar />
      <Backdrop />

      <div className={`flex flex-col flex-1 min-h-screen transition-all duration-300 ${mainContentMargin}`}>
        <AppHeader />

        <main className="flex-1 mx-auto w-full px-4 pt-5 md:px-6">{children}</main>

        <footer className="mx-auto w-full px-4 py-4 text-center text-sm text-gray-500 md:px-6">
          © {new Date().getFullYear()} – Spit Management
        </footer>
      </div>
    </div>
  )
}
