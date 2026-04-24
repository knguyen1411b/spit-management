import {
  ApartmentOutlined,
  AppstoreOutlined,
  BellOutlined,
  CalendarOutlined,
  CheckSquareOutlined,
  ClusterOutlined,
  CrownOutlined,
  FormOutlined,
  ProfileOutlined,
  SolutionOutlined,
  TeamOutlined
} from '@ant-design/icons'
import { useAuth } from '@/contexts'

export type NavItem = {
  name: string
  icon: React.ReactNode
  path: string
}

export const personalItems: NavItem[] = [
  {
    icon: <AppstoreOutlined />,
    name: 'Trang chủ',
    path: '/'
  },
  {
    icon: <CalendarOutlined />,
    name: 'Lịch công tác',
    path: '/schedule'
  },
  {
    icon: <BellOutlined />,
    name: 'Thông báo',
    path: '/notification'
  },
  {
    icon: <ProfileOutlined />,
    name: 'Hồ sơ người dùng',
    path: '/profile'
  }
]

export const GetItemsSidebar = () => {
  const { profile } = useAuth()

  const hasPermission = (perm: string) =>
    profile?.superuser || profile?.permissions?.includes('all:all') || profile?.permissions?.includes(perm)

  const managementItems: NavItem[] = []

  if (hasPermission('user:read'))
    managementItems.push({ icon: <TeamOutlined />, name: 'Quản lý người dùng', path: '/user' })

  if (hasPermission('role:read') && hasPermission('permission:read'))
    managementItems.push({ name: 'Quản lí chức vụ', icon: <CrownOutlined />, path: '/role' })

  if (hasPermission('member:read'))
    managementItems.push({ icon: <SolutionOutlined />, name: 'Quản lý thành viên', path: '/member' })

  if (hasPermission('semester:read'))
    managementItems.push({ icon: <ClusterOutlined />, name: 'Quản lý kỳ học', path: '/semester' })

  if (hasPermission('board:read'))
    managementItems.push({ icon: <ApartmentOutlined />, name: 'Quản lý ban', path: '/board' })

  if (hasPermission('task:read'))
    managementItems.push({ name: 'Quản lý công việc', path: '/task', icon: <FormOutlined /> })

  if (hasPermission('task_request:read'))
    managementItems.push({ name: 'Kiểm tra công việc', path: '/task-request', icon: <CheckSquareOutlined /> })

  return {
    personalItems,
    managementItems
  }
}
