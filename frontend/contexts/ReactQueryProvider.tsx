'use client'

import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { useToast } from '@/contexts//HeroProvider'
import { ReactNode, useState } from 'react'
import { useTheme } from 'next-themes'

/* React Query Provider */
export const ReactQueryProvider = ({ children, initTheme }: { children: ReactNode; initTheme?: string }) => {
  const devTheme = process.env.NODE_ENV === 'development' && false
  const { resolvedTheme } = useTheme()
  const { error } = useToast()

  /* Determine theme for React Query Devtools */
  const finalTheme = (resolvedTheme ?? initTheme ?? 'light') as 'dark' | 'light' | 'system'

  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            staleTime: 10 * 60 * 1000, // 5 minutes cache
            retry: 1, // Retry once on failure
            refetchOnReconnect: true, // Refetch on network reconnect
            refetchOnWindowFocus: false, // Disable refetch on window focus
            placeholderData: (prev: any) => prev // Use previous data as placeholder
          },
          mutations: {
            onError: (err: any) => error('Có lỗi xảy ra', err?.message || 'Vui lòng thử lại sau!') // Global error handling for mutations
          }
        }
      })
  )

  return (
    <QueryClientProvider client={queryClient}>
      {children}
      {devTheme && <ReactQueryDevtools initialIsOpen={false} theme={finalTheme} />}
    </QueryClientProvider>
  )
}
