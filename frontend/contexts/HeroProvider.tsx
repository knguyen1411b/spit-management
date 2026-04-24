'use client'

import { addToast, closeAll, ToastProvider } from '@heroui/toast'
import { createContext, JSX, ReactNode, useContext } from 'react'
import { HeroUIProvider } from '@heroui/system'
import { X } from 'lucide-react'

/* Define Toast Context Type */
interface ToastContextType {
  toast: typeof addToast
  success: (title: string, description?: string) => void
  error: (title: string, description?: string) => void
  warning: (title: string, description?: string) => void
  info: (title: string, description?: string) => void
  closeAll: () => void
}

/* Create Toast Context */
const ToastContext = createContext<ToastContextType | undefined>(undefined)

/* Toast Hooks Provider Component */
const ToastHooksProvider = ({ children }: { children: ReactNode }): JSX.Element => {
  return (
    <ToastContext.Provider
      value={{
        toast: addToast,
        closeAll,
        success: (title, description) => addToast({ title, description, color: 'success' }),
        error: (title, description) => addToast({ title, description, color: 'danger' }),
        warning: (title, description) => addToast({ title, description, color: 'warning' }),
        info: (title, description) => addToast({ title, description, color: 'primary' })
      }}
    >
      {children}
    </ToastContext.Provider>
  )
}

/* Hero Provider Component */
export const HeroProvider = ({ children }: { children: ReactNode }): JSX.Element => {
  return (
    <HeroUIProvider>
      <ToastProvider
        placement="top-right"
        maxVisibleToasts={2}
        toastOffset={10}
        toastProps={{
          timeout: 3000,
          classNames: {
            closeButton: 'opacity-100 absolute right-4 top-1/2 -translate-y-1/2'
          },
          shouldShowTimeoutProgress: true,
          closeIcon: <X size={24} />
        }}
      />
      <ToastHooksProvider>{children}</ToastHooksProvider>
    </HeroUIProvider>
  )
}

/* Custom hook to use Toast Context */
export const useToast = (): ToastContextType => {
  const context = useContext(ToastContext)
  if (!context) {
    throw new Error('useToast must be used within HeroProvider')
  }
  return context
}
