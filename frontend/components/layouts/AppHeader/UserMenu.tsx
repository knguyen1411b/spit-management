'use client'

import { BellOutlined, LogoutOutlined, PieChartOutlined, QuestionCircleOutlined, UserOutlined } from '@ant-design/icons'
import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, useDisclosure } from '@heroui/modal'
import { Dropdown, DropdownTrigger, DropdownMenu, DropdownItem } from '@heroui/dropdown'
import { useNotifications } from '@/hooks/data/use-notifitions'
import { logoutAction } from '@/services/auth.service'
import { useAuth, useToast } from '@/contexts'
import { useRouter } from 'next/navigation'
import { Button } from '@heroui/button'
import { Avatar, Badge } from 'antd'

export const UserMenu = () => {
  const { profile, logOut } = useAuth()
  const { unreadCount } = useNotifications()
  const { success } = useToast()
  const router = useRouter()

  const { isOpen: isLogoutOpen, onOpen: openLogout, onOpenChange: onLogoutChange } = useDisclosure()

  const handleLogout = async () => {
    await logoutAction()
    logOut()
    router.push('/login')
    success('Đăng xuất thành công!')
  }

  return (
    <>
      <Dropdown placement="bottom-end" offset={20}>
        <DropdownTrigger>
          <Badge count={unreadCount}>
            <Avatar size={40} src={profile?.avatar} icon={<UserOutlined />} className="cursor-pointer">
              {!profile?.avatar && profile?.username?.charAt(0).toUpperCase()}
            </Avatar>
          </Badge>
        </DropdownTrigger>

        <DropdownMenu aria-label="Profile Actions">
          <DropdownItem key="profile" isReadOnly>
            <div className="flex items-center gap-3 py-2">
              <Avatar size={40} src={profile?.avatar} icon={<UserOutlined />}>
                {!profile?.avatar && profile?.username?.charAt(0).toUpperCase()}
              </Avatar>

              <div>
                <div className="text-sm font-semibold">{profile?.username}</div>
                <div className="text-xs">{profile?.email}</div>
              </div>
            </div>
          </DropdownItem>

          <DropdownItem key="me" onPress={() => router.push('/profile')}>
            <UserOutlined className="mr-2" /> Thông tin cá nhân
          </DropdownItem>

          <DropdownItem key="notifications" onClick={() => router.push('/notification')}>
            <div className="flex items-center gap-2">
              <Badge count={unreadCount} size="small">
                <BellOutlined />
              </Badge>
              <span>Thông báo</span>
            </div>
          </DropdownItem>

          {profile?.superuser ? (
            <DropdownItem key="stats" onPress={() => router.push('/')}>
              <PieChartOutlined className="mr-2" /> Thống kê
            </DropdownItem>
          ) : null}

          <DropdownItem key="help" isReadOnly>
            <QuestionCircleOutlined className="mr-2" /> Hỗ trợ
          </DropdownItem>

          <DropdownItem
            key="logout"
            color="danger"
            onPress={openLogout}
            className="text-red-500! hover:text-white! transition-all duration-250"
          >
            <LogoutOutlined className="mr-2" /> Đăng xuất
          </DropdownItem>
        </DropdownMenu>
      </Dropdown>

      <Modal isOpen={isLogoutOpen} onOpenChange={onLogoutChange} isDismissable={false}>
        <ModalContent>
          {onClose => (
            <>
              <ModalHeader className="text-red-600">Xác nhận đăng xuất</ModalHeader>

              <ModalBody>
                <p>
                  Bạn có chắc chắn muốn <strong>đăng xuất</strong> khỏi hệ thống không?
                </p>
              </ModalBody>

              <ModalFooter>
                <Button onPress={onClose}>Hủy</Button>

                <Button
                  color="danger"
                  onPress={async () => {
                    await handleLogout()
                    onClose()
                  }}
                >
                  Đăng xuất
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  )
}
