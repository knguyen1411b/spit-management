import { Geist, Geist_Mono, Instrument_Sans, Inter, Mulish, Noto_Sans_Mono } from 'next/font/google'

import { cn } from '@/libs/utils'

/**
 * Primary sans-serif font (Geist)
 * Used for general UI text and standard readable typography.
 */
const fontSans = Geist({
  subsets: ['latin'],
  variable: '--font-sans'
})

/**
 * Monospaced variant of Geist
 * Ideal for displaying code, numbers, and technical content.
 */
const fontMono = Geist_Mono({
  subsets: ['latin'],
  variable: '--font-mono'
})

/**
 * Instrument Sans
 * A stylistic sans-serif font used for headers or decorative elements.
 */
const fontInstrument = Instrument_Sans({
  subsets: ['latin'],
  variable: '--font-instrument'
})

/**
 * Noto Sans Mono
 * Another monospaced font with broader multilingual support.
 */
const fontNotoMono = Noto_Sans_Mono({
  subsets: ['latin'],
  variable: '--font-noto-mono'
})

/**
 * Mulish font
 * Clean sans-serif font that works great for UI, forms, or dashboard text.
 */
const fontMullish = Mulish({
  subsets: ['latin'],
  variable: '--font-mullish'
})

/**
 * Inter font
 * Widely used for dashboards and modern web applications.
 */
const fontInter = Inter({
  subsets: ['latin'],
  variable: '--font-inter'
})

/**
 * Combined CSS variable classes for all loaded fonts.
 *
 * @description
 * - This string contains all `--font-*` CSS variables.
 * - It is meant to be applied on `<html>` or `<body>` to register all font families.
 * - Example usage:
 *   ```tsx
 *   <html className={fontVariables}>
 *   ```
 */
export const fontVariables = cn(
  fontSans.variable,
  fontMono.variable,
  fontInstrument.variable,
  fontNotoMono.variable,
  fontMullish.variable,
  fontInter.variable
)
