'use client'

import { useTheme } from 'next-themes'
import { useCallback } from 'react'

import { MoonOutlined, SunOutlined } from '@ant-design/icons'
import { Button } from '@heroui/button'

/* Mode Toggle Component */
export function ModeToggle() {
  const { setTheme, resolvedTheme } = useTheme()

  /* Handle Theme Toggle */
  const handleThemeToggle = useCallback(() => {
    const newMode = resolvedTheme === 'dark' ? 'light' : 'dark'

    document.cookie = `app-theme=${newMode};path=/;max-age=${365 * 24 * 60 * 60};SameSite=Strict;Secure`

    document.startViewTransition(() => setTheme(newMode))

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [setTheme])

  return (
    <Button radius="full" aria-label="Toggle theme" variant="bordered" isIconOnly onPress={handleThemeToggle}>
      <MoonOutlined className="block! dark:hidden!" />
      <SunOutlined className="hidden! dark:block!" />
      <span className="sr-only">Thay đổi theme</span>
    </Button>
  )
}
