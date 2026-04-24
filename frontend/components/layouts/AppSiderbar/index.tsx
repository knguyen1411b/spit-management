'use client'

import { EllipsisOutlined, MenuOutlined } from '@ant-design/icons'
import { GetItemsSidebar, NavItem } from './ItemsSidebar'
import { usePathname, useRouter } from 'next/navigation'
import { AnimatePresence, motion } from 'framer-motion'
import { Button } from '@heroui/button'
import { useSidebar } from '@/contexts'
import { useCallback } from 'react'
import { cn } from '@/libs/utils'
import Image from 'next/image'

export const AppSidebar: React.FC = () => {
  const { personalItems, managementItems } = GetItemsSidebar()
  const router = useRouter()
  const pathname = usePathname()

  const { isExpanded, isMobileOpen, toggleSidebar, toggleMobileSidebar } = useSidebar()

  const isActive = useCallback((path: string) => path === pathname, [pathname])
  const isShowContent = isExpanded || isMobileOpen

  const renderMenuItems = (navItems: NavItem[]) => (
    <ul className="flex flex-col gap-2">
      {navItems.map((nav, _) => (
        <motion.li
          key={nav.name}
          initial={false}
          animate={{
            opacity: 1,
            x: 0
          }}
          transition={{
            duration: 0.2,
            ease: [0.4, 0, 0.2, 1]
          }}
        >
          {nav.path && (
            <button
              onClick={() => router.push(nav.path)}
              className={cn(
                'text-theme-sm group relative flex w-full items-center gap-3 rounded-lg px-3 py-2.5 font-medium transition-all duration-200 ease-in-out',
                isActive(nav.path)
                  ? 'bg-blue-50 text-blue-600 shadow-sm dark:bg-blue-900/40 dark:text-blue-400'
                  : 'text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-800',
                !isExpanded ? 'lg:justify-center' : 'lg:justify-start'
              )}
            >
              <motion.span
                className="shrink-0 text-lg"
                animate={{
                  scale: isActive(nav.path) ? 1.1 : 1
                }}
                transition={{ duration: 0.2 }}
              >
                {nav.icon}
              </motion.span>

              <AnimatePresence mode="wait">
                {isShowContent && (
                  <motion.div
                    initial={{ opacity: 0, width: 0 }}
                    animate={{
                      opacity: 1,
                      width: 'auto',
                      transition: {
                        duration: 0.25,
                        ease: [0.4, 0, 0.2, 1]
                      }
                    }}
                    exit={{
                      opacity: 0,
                      width: 0,
                      transition: {
                        duration: 0.2,
                        ease: [0.4, 0, 0.2, 1]
                      }
                    }}
                    className="overflow-hidden whitespace-nowrap"
                  >
                    {nav.name}
                  </motion.div>
                )}
              </AnimatePresence>
            </button>
          )}
        </motion.li>
      ))}
    </ul>
  )

  const renderHeaderMenu = (name: string) => (
    <motion.h2
      initial={false}
      className="relative mb-4 flex h-4 items-center text-xs font-semibold tracking-wider text-gray-500 uppercase dark:text-gray-400"
      animate={{
        justifyContent: isShowContent ? 'flex-start' : 'center'
      }}
      transition={{ duration: 0.3, ease: [0.4, 0, 0.2, 1] }}
    >
      <AnimatePresence mode="wait">
        {!isShowContent && (
          <motion.span
            key="icon"
            initial={{ opacity: 0, scale: 0.8 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 0.8 }}
            transition={{ duration: 0.2 }}
          >
            <EllipsisOutlined />
          </motion.span>
        )}
        {isShowContent && (
          <motion.span
            key="text"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.25, ease: [0.4, 0, 0.2, 1] }}
          >
            {name}
          </motion.span>
        )}
      </AnimatePresence>
    </motion.h2>
  )

  const handleToggle = () => {
    if (window.innerWidth >= 1024) toggleSidebar()
    else toggleMobileSidebar()
  }

  return (
    <motion.aside
      initial={false}
      animate={{
        width: isExpanded || isMobileOpen ? 290 : 90
      }}
      transition={{
        duration: 0.3,
        ease: [0.4, 0, 0.2, 1]
      }}
      className={cn(
        'fixed top-0 left-0 z-50 mt-16 flex h-screen flex-col border-r lg:mt-0 lg:translate-x-0',
        'border-gray-200 dark:border-gray-900',
        'px-5',
        isMobileOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
      )}
    >
      <div className="relative flex items-center justify-between py-6">
        <motion.div
          animate={{
            scale: isShowContent ? 1 : 0.9
          }}
          transition={{ duration: 0.2 }}
          className="flex items-center gap-3"
        >
          <Image
            src="/logo.png"
            alt="Logo"
            width={40}
            height={40}
            className={cn('transition-all duration-200', isShowContent ? 'opacity-100' : 'opacity-0')}
          />

          <AnimatePresence>
            {isShowContent && (
              <motion.span
                initial={{ opacity: 0, width: 0 }}
                animate={{ opacity: 1, width: 'auto' }}
                exit={{ opacity: 0, width: 0 }}
                transition={{ duration: 0.25, ease: 'easeIn' }}
                className="overflow-hidden whitespace-nowrap text-lg font-bold"
              >
                S.P.I.T
              </motion.span>
            )}
          </AnimatePresence>
        </motion.div>

        <AnimatePresence>
          {(isShowContent || !isMobileOpen) && (
            <Button isIconOnly onPress={handleToggle} variant="bordered">
              <MenuOutlined style={{ fontSize: 18 }} />
            </Button>
          )}
        </AnimatePresence>
      </div>

      <div className="no-scrollbar flex flex-1 flex-col overflow-y-auto pb-3">
        <nav className="flex-1">
          <motion.div
            className="mb-8"
            initial={false}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.3, delay: 0.1 }}
          >
            {renderHeaderMenu('Cá nhân')}
            {renderMenuItems(personalItems)}
          </motion.div>

          {managementItems.length > 0 && (
            <motion.div
              className="mb-8"
              initial={false}
              animate={{ opacity: 1 }}
              transition={{ duration: 0.3, delay: 0.15 }}
            >
              {renderHeaderMenu('Quản lý')}
              {renderMenuItems(managementItems)}
            </motion.div>
          )}
        </nav>
      </div>
    </motion.aside>
  )
}
