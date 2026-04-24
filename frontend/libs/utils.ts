import { type ClassValue, clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'

/**
 * Utility function to combine multiple class names safely.
 *
 * @param inputs - Array of class name values (strings, arrays, objects...)
 * @returns A clean, merged Tailwind CSS class string.
 */
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}
