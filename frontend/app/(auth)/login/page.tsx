'use client'

import { Card, Checkbox, Divider, Form, Input, Typography } from 'antd'
import { useRouter, useSearchParams } from 'next/navigation'
import Password from 'antd/es/input/Password'
import { useCallback, useState } from 'react'
import Title from 'antd/es/typography/Title'
import FormItem from 'antd/es/form/FormItem'
import { motion } from 'framer-motion'

import { LockOutlined, LoginOutlined, UserOutlined } from '@ant-design/icons'
import { getProfileAction, loginAction } from '@/services/auth.service'
import { useAuth, useToast } from '@/contexts'
import { Button } from '@heroui/button'
import { IFormLogin } from '@/types'

export default function LoginPage() {
  const searchParams = useSearchParams()
  const redirectTo = searchParams.get('redirectTo')
  const { logIn } = useAuth()
  const [loading, setLoading] = useState<boolean>(false)
  const [keepMe, setKeepMe] = useState<boolean>(true)
  const [form] = Form.useForm<IFormLogin>()
  const { success, error } = useToast()
  const router = useRouter()

  /**
   * Handles the login in process when the form is submitted.
   *
   * @param loginForm - The form data containing user credentials for login.
   */
  const handleSignIn = useCallback(
    async (loginForm: IFormLogin) => {
      setLoading(true)
      const data = await loginAction(
        {
          username: loginForm.username.trim(),
          password: loginForm.password.trim()
        },
        keepMe
      )
      if (data.success) {
        success('Đăng nhập thành công')
        const profileData = await getProfileAction()
        logIn(profileData.data)
        router.push(redirectTo || '/')
      } else if (data.statusCode === 401) {
        error('Tên đăng nhập hoặc mật khẩu không đúng')
      } else {
        error('Đã xảy ra lỗi trong quá trình đăng nhập')
      }

      form.resetFields(['password'])
      setLoading(false)
    },
    [error, form, success, keepMe, logIn, router, redirectTo]
  )

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6, ease: 'easeOut' }}
    >
      <Card className="rounded-3xl! shadow-2xl max-w-md!">
        <Typography style={{ textAlign: 'center' }}>
          <Title level={2}>Đăng nhập</Title>
          <Divider />
        </Typography>

        <Form name="form_sign_in" form={form} layout="vertical" onFinish={handleSignIn}>
          <FormItem
            name="username"
            rules={[
              { required: true, message: 'Tên đăng nhập không được để trống' },
              { min: 3, message: 'Tên đăng nhập phải có ít nhất 3 ký tự' },
              { max: 30, message: 'Tên đăng nhập phải có tối đa 30 ký tự' }
            ]}
          >
            <Input
              prefix={<UserOutlined style={{ margin: '0 5px' }} />}
              placeholder="Tên đăng nhập"
              size="large"
              disabled={loading}
            />
          </FormItem>

          <FormItem
            name="password"
            rules={[
              { required: true, message: 'Mật khẩu không được để trống' },
              { min: 3, message: 'Mật khẩu phải có ít nhất 3 ký tự' },
              { max: 50, message: 'Mật khẩu phải có tối đa 50 ký tự' }
            ]}
          >
            <Password
              prefix={<LockOutlined style={{ margin: '0 5px' }} />}
              placeholder="Mật khẩu"
              size="large"
              disabled={loading}
            />
          </FormItem>

          <FormItem>
            <Checkbox checked={keepMe} onChange={e => setKeepMe(e.target.checked)} disabled={loading}>
              Lưu đăng nhập
            </Checkbox>
          </FormItem>

          <FormItem className="mb-0!">
            <Button endContent={<LoginOutlined />} isLoading={loading} fullWidth color="primary" type="submit">
              Đăng nhập
            </Button>
          </FormItem>
        </Form>
      </Card>
    </motion.div>
  )
}
