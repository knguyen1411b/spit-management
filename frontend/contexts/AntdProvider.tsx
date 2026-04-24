'use client'

import type { NotificationInstance } from 'antd/es/notification/interface'
import { createContext, JSX, ReactNode, useContext } from 'react'
import type { MessageInstance } from 'antd/es/message/interface'
import { App, ConfigProvider, theme, ThemeConfig } from 'antd'
import { AntdRegistry } from '@ant-design/nextjs-registry'
import { useTheme } from 'next-themes'
import antdConfig from '@/libs/antd'

/** Global Ant Design App configuration */
const appConfig = {
  message: { maxCount: 1, duration: 4 },
  notification: { duration: 4, placement: 'topRight' as const, maxCount: 1 }
}

/** Notification context type */
interface NotificationContextType {
  message: MessageInstance
  notification: NotificationInstance
}

/** React context to provide notification methods */
const NotificationContext = createContext<NotificationContextType | undefined>(undefined)

/** Internal provider that exposes notification methods via context */
const NotificationHooksProvider = ({ children }: { children: ReactNode }): JSX.Element => {
  const { message, notification } = App.useApp()
  return <NotificationContext.Provider value={{ message, notification }}>{children}</NotificationContext.Provider>
}

/** Ant Design provider that sets up theming and notification context */
export const AntdProvider = ({ children, initTheme }: { children: ReactNode; initTheme?: string }): JSX.Element => {
  const { resolvedTheme } = useTheme()

  const finalTheme = resolvedTheme ?? initTheme ?? 'light'

  const themeConfig: ThemeConfig = {
    algorithm: finalTheme === 'dark' ? theme.darkAlgorithm : theme.defaultAlgorithm,
    ...antdConfig
  }

  return (
    <AntdRegistry>
      <ConfigProvider theme={themeConfig}>
        <App {...appConfig}>
          <NotificationHooksProvider>{children}</NotificationHooksProvider>
        </App>
      </ConfigProvider>
    </AntdRegistry>
  )
}

/** Custom hook to access global notification methods  */
export const useNotification = (): NotificationContextType => {
  const context = useContext(NotificationContext)
  if (!context) {
    throw new Error('useNotification must be used within a ThemeProvider')
  }
  return context
}
