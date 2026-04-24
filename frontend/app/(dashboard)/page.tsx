import HomeMain from '@/features/home'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Trang chủ - Spit Management'
}

export default function HomePage() {
  return <HomeMain />
}
