import ProfileMain from '@/features/profile'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Hồ sơ người dùng - Spit Management'
}

export default function ProfilePage() {
  return <ProfileMain />
}
