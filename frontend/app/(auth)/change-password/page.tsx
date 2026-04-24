'use client'

import { Form, Input, Button, Card, Typography, Divider } from 'antd'
import { LockOutlined, SafetyOutlined } from '@ant-design/icons'
import { changePasswordAction } from '@/services/auth.service'
import { useAuth, useToast } from '@/contexts'
import type { IChangePassword } from '@/types'
import { useRouter } from 'next/navigation'
import { motion } from 'framer-motion'
import { useState } from 'react'

const { Title, Text } = Typography

export default function ChangePasswordPage() {
  const router = useRouter()
  const { profile } = useAuth()
  const [loading, setLoading] = useState(false)
  const { success, error } = useToast()
  const [form] = Form.useForm<IChangePassword>()

  const onFinish = async (values: IChangePassword) => {
    try {
      setLoading(true)

      const res = await changePasswordAction({
        oldPassword: values.oldPassword,
        newPassword: values.newPassword
      })

      if (res?.success) {
        success('Đổi mật khẩu thành công')
        form.resetFields()
        router.push('/')
      } else {
        error(res?.message || 'Đổi mật khẩu thất bại')
      }
    } catch (err: any) {
      error(err?.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại')
    } finally {
      setLoading(false)
    }
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, ease: 'easeOut' }}
    >
      <Card className="rounded-3xl! shadow-2xl max-w-md!">
        <div className="text-center mb-4">
          <SafetyOutlined className="text-3xl text-blue-600 mb-2" />
          <Title level={3}>Đổi mật khẩu</Title>
          <Text type="secondary">Để bảo mật tài khoản, vui lòng không chia sẻ mật khẩu cho người khác</Text>
        </div>

        <Divider />

        <Form
          form={form}
          layout="vertical"
          size="large"
          onFinish={onFinish}
          initialValues={{
            oldPassword: '',
            newPassword: '',
            confirmNewPassword: ''
          }}
        >
          <Form.Item
            name="oldPassword"
            label="Mật khẩu hiện tại"
            rules={[{ required: true, message: 'Vui lòng nhập mật khẩu hiện tại' }]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="Nhập mật khẩu hiện tại"
              autoComplete="current-password"
            />
          </Form.Item>

          <Form.Item
            name="newPassword"
            label="Mật khẩu mới"
            rules={[
              { required: true, message: 'Vui lòng nhập mật khẩu mới' },
              { min: 6, message: 'Mật khẩu phải có ít nhất 6 ký tự' }
            ]}
            hasFeedback
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Nhập mật khẩu mới" autoComplete="new-password" />
          </Form.Item>

          <Form.Item
            name="confirmNewPassword"
            label="Xác nhận mật khẩu mới"
            dependencies={['newPassword']}
            hasFeedback
            rules={[
              { required: true, message: 'Vui lòng xác nhận mật khẩu mới' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('newPassword') === value) {
                    return Promise.resolve()
                  }
                  return Promise.reject(new Error('Mật khẩu xác nhận không khớp'))
                }
              })
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="Nhập lại mật khẩu mới" autoComplete="new-password" />
          </Form.Item>

          <Form.Item className="mt-10!">
            <Button type="primary" htmlType="submit" loading={loading} block size="large">
              Đổi mật khẩu
            </Button>
          </Form.Item>
        </Form>
        {profile?.passwordChanged && (
          <p className="text-center text-sm text-gray-500 cursor-pointer" onClick={() => router.back()}>
            Quay lại trang trước
          </p>
        )}
      </Card>
    </motion.div>
  )
}
