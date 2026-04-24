import { ModeToggle } from '@/components/ui/ModeToggle'
import { Col, Flex, Row } from 'antd'
import { ReactNode } from 'react'
import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Thay đổi mật khẩu - SPIT Management'
}

export default function AuthLayout({ children }: { children: ReactNode }) {
  return (
    <Flex justify="center" className="min-h-screen flex-col">
      <Row justify="center" align="middle" className="flex-1 px-4">
        <Col xxl={6} xl={8} lg={10} md={12} sm={20} xs={22}>
          {children}
        </Col>
      </Row>

      <div className="py-4 text-center">
        <p className="text-sm text-gray-500 dark:text-gray-400">© {new Date().getFullYear()} SPIT Management</p>
      </div>

      <div className="fixed right-6 bottom-6 z-50">
        <ModeToggle />
      </div>
    </Flex>
  )
}
