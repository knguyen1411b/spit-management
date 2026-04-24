import {
  UserOutlined,
  TeamOutlined,
  ApartmentOutlined,
  ProfileOutlined,
  ProjectOutlined,
  CheckCircleOutlined,
  IdcardOutlined,
  CalendarOutlined,
  BellOutlined
} from '@ant-design/icons'

export const BREADCRUMB_CONFIG: Record<string, { label: string; icon?: React.ReactNode }> = {
  user: { label: 'Người dùng', icon: <UserOutlined /> },
  schedule: { label: 'Lịch công tác', icon: <CalendarOutlined /> },
  role: { label: 'Vai trò', icon: <IdcardOutlined /> },
  member: { label: 'Thành viên', icon: <TeamOutlined /> },
  board: { label: 'Ban quản lý', icon: <ApartmentOutlined /> },
  semester: { label: 'Kỳ học', icon: <ProjectOutlined /> },
  task: { label: 'Công việc', icon: <ProfileOutlined /> },
  'task-request': { label: 'Kiểm tra công việc', icon: <CheckCircleOutlined /> },
  profile: { label: 'Thông tin cá nhân', icon: <IdcardOutlined /> },
  notification: { label: 'Thông báo', icon: <BellOutlined /> }
}
