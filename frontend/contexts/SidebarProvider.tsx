'use client'

import { createContext, JSX, ReactNode, useContext, useEffect, useState } from 'react'

/**
 * Sidebar context type definition
 * Describes the shape of the context value
 */
interface SidebarContextType {
  isExpanded: boolean // Whether the sidebar is expanded (desktop)
  isMobileOpen: boolean // Whether the mobile sidebar is open
  activeItem: string | null // Currently active menu item
  openSubmenu: string | null // Currently open submenu
  toggleSidebar: () => void // Toggle sidebar expansion
  toggleMobileSidebar: () => void // Toggle mobile sidebar open state
  setActiveItem: (item: string | null) => void // Set active menu item
  toggleSubmenu: (item: string) => void // Toggle open submenu
}

/**
 * Create React context for sidebar
 */
const SidebarContext = createContext<SidebarContextType | undefined>(undefined)

/**
 * Sidebar provider component
 * Provides state and methods to manage sidebar behavior
 */
export const SidebarProvider = ({ children }: { children: ReactNode }): JSX.Element => {
  const [isExpanded, setIsExpanded] = useState(true) // Desktop sidebar expanded state
  const [isMobileOpen, setIsMobileOpen] = useState(false) // Mobile sidebar open state
  const [isMobile, setIsMobile] = useState(false) // Flag indicating if viewport is mobile
  const [activeItem, setActiveItem] = useState<string | null>(null) // Active menu item
  const [openSubmenu, setOpenSubmenu] = useState<string | null>(null) // Currently open submenu

  /**
   * Handle window resize to update mobile state
   */
  useEffect(() => {
    const handleResize = () => {
      const mobile = window.innerWidth < 768
      setIsMobile(mobile)
      if (!mobile) setIsMobileOpen(false) // Close mobile sidebar when switching to desktop
    }

    handleResize() // Initialize on mount
    window.addEventListener('resize', handleResize)

    return () => {
      window.removeEventListener('resize', handleResize)
    }
  }, [])

  /**
   * Toggle sidebar expansion (desktop)
   */
  const toggleSidebar = () => {
    const newState = !isExpanded
    setIsExpanded(newState)
    document.cookie = `sidebar-expanded=${newState};path=/;max-age=${365 * 24 * 60 * 60};SameSite=Strict;Secure`
  }

  /**
   * Toggle mobile sidebar open state
   */
  const toggleMobileSidebar = () => {
    setIsMobileOpen(prev => !prev)
  }

  /**
   * Toggle submenu open/close
   * @param item Menu item key
   */
  const toggleSubmenu = (item: string) => {
    setOpenSubmenu(prev => (prev === item ? null : item))
  }

  return (
    <SidebarContext.Provider
      value={{
        isExpanded: isMobile ? false : isExpanded,
        isMobileOpen,
        activeItem,
        openSubmenu,
        toggleSidebar,
        toggleMobileSidebar,
        setActiveItem,
        toggleSubmenu
      }}
    >
      {children}
    </SidebarContext.Provider>
  )
}

/**
 * Custom hook to use sidebar context
 * @returns SidebarContextType
 * @throws Error if used outside SidebarProvider
 */
export const useSidebar = (): SidebarContextType => {
  const context = useContext(SidebarContext)
  if (!context) {
    throw new Error('useSidebar must be used within a SidebarProvider')
  }
  return context
}
