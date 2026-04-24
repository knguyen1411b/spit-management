'use client'

import { ThemeProvider as NextThemesProvider, ThemeProviderProps } from 'next-themes'

/** ThemeProvider to manage light and dark themes in the application. */
export const ThemeProvider = ({ children, ...props }: ThemeProviderProps) => {
  return (
    <NextThemesProvider
      {...props}
      attribute="class"
      defaultTheme="system"
      enableSystem
      disableTransitionOnChange
      enableColorScheme
    >
      {children}
    </NextThemesProvider>
  )
}
