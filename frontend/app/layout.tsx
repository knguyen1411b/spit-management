import type { Metadata, Viewport } from 'next'

import { NuqsAdapter } from 'nuqs/adapters/next'
import NextTopLoader from 'nextjs-toploader'
import { cookies } from 'next/headers'
import { ReactNode } from 'react'
import Script from 'next/script'

import {
  AntdProvider,
  AuthProvider,
  HeroProvider,
  ReactQueryProvider,
  SidebarProvider,
  ThemeProvider
} from '@/contexts'

import { fontVariables } from '@/libs/font'
import { cn } from '@/libs/utils'

import './globals.css'

const META_THEME_COLORS = { light: '#ffffff', dark: '#09090b' }

export const metadata: Metadata = {
  title: 'Trang chủ - Spit Management',
  description: 'Hệ thống quản lý dự câu lạp bộ Hỗ trợ lập trình S.P.I.T.'
}

export const viewport: Viewport = { themeColor: META_THEME_COLORS.light }

export default async function RootLayout({
  children
}: Readonly<{
  children: ReactNode
}>) {
  const themeKey = 'app-theme'
  const initTheme = (await cookies()).get(themeKey)?.value
  return (
    <html lang="en" suppressHydrationWarning>
      <head>
        <Script id="theme-sync" strategy="beforeInteractive">
          {`
            (function () {
              try {
                const stored = localStorage.getItem('${themeKey}');
                const systemDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
                const finalTheme =
                  stored === 'system'
                    ? (systemDark ? 'dark' : 'light')
                    : (stored || 'light');
                var oneYear = 365 * 24 * 60 * 60;
                document.cookie =
                  '${themeKey}=' + finalTheme +
                  '; Path=/' +
                  '; Max-Age=' + oneYear +
                  '; SameSite=Strict; Secure';
                if (finalTheme === 'dark') {
                  document
                    .querySelector('meta[name="theme-color"]')
                    ?.setAttribute('content', '${META_THEME_COLORS.dark}');
                }
              } catch (e) {}
            })();
          `}
        </Script>
      </head>
      <body className={cn('font-sans antialiased', fontVariables)}>
        <NextTopLoader showSpinner={false} color="#155dfc" />
        <NuqsAdapter>
          <ThemeProvider storageKey={themeKey}>
            <AntdProvider initTheme={initTheme}>
              <HeroProvider>
                <ReactQueryProvider initTheme={initTheme}>
                  <AuthProvider>
                    <SidebarProvider>{children}</SidebarProvider>
                  </AuthProvider>
                </ReactQueryProvider>
              </HeroProvider>
            </AntdProvider>
          </ThemeProvider>
        </NuqsAdapter>
      </body>
    </html>
  )
}
