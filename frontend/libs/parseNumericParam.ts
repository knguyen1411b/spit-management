import { notFound } from 'next/navigation'

export const parseNumericParam = (value: string): number => {
  if (!/^\d+$/.test(value)) {
    notFound()
  }

  const num = Number(value)
  if (num <= 0) {
    notFound()
  }

  return num
}
