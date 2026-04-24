'use client'

import {
  UserOutlined,
  MailOutlined,
  PhoneOutlined,
  IdcardOutlined,
  InfoCircleOutlined,
  CalendarOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  LockOutlined,
  CameraOutlined
} from '@ant-design/icons'
import { Avatar, Button, Card, Col, Divider, Flex, Row, Tag, Typography } from 'antd'
import { uploadAvatarUser } from '@/services/avatar.service'
import { useQueryClient } from '@tanstack/react-query'
import { Heading } from '@/components/ui/heading'
import { useAuth, useToast } from '@/contexts'
import { useRouter } from 'next/navigation'
import LoadingScreen from '@/app/loading'
import { Fragment, useRef } from 'react'
import dayjs from 'dayjs'

function ProfileRow({ icon, label, value }: { icon: React.ReactNode; label: string; value: any }) {
  return (
    <Flex gap={8} className="text-[15px]">
      {icon} <b>{label}:</b> {value}
    </Flex>
  )
}

export default function ProfileView() {
  const { profile, refetch } = useAuth()
  const { push } = useRouter()
  const queryClient = useQueryClient()
  const { success, error } = useToast()
  const inputRef = useRef<HTMLInputElement>(null)

  const handleChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (!file) return

    if (!file.type.startsWith('image/')) {
      error('Chỉ được upload ảnh')
      return
    }

    try {
      await uploadAvatarUser(file)
      success('Cập nhật avatar thành công')
      await refetch()
      queryClient.invalidateQueries({ queryKey: ['members'] })
    } catch {
      error('Upload avatar thất bại')
    }
  }
  if (!profile) return <LoadingScreen />

  const fullName = profile.lastName && profile.firstName ? `${profile.lastName} ${profile.firstName}` : profile.username

  return (
    <Fragment>
      <Flex justify="space-between" align="center">
        <Heading title="Thông tin cá nhân" description="Hồ sơ tài khoản của bạn" />
        <Button type="primary" onClick={() => push('/change-password')} icon={<LockOutlined />}>
          Thay đổi mật khẩu
        </Button>
      </Flex>

      <Divider />

      <Flex vertical align="center" className="mb-6!">
        <>
          <div className="relative inline-block cursor-pointer group" onClick={() => inputRef.current?.click()}>
            <Avatar size={100} src={profile.avatar} icon={<UserOutlined />} />

            <div className="absolute inset-0 rounded-full bg-black/40 opacity-0 group-hover:opacity-100 flex items-center justify-center transition">
              <CameraOutlined className="text-white text-xl" />
            </div>
          </div>

          <input ref={inputRef} type="file" accept="image/*" hidden onChange={handleChange} />
        </>

        <Typography.Title level={3} style={{ marginTop: 16 }}>
          {fullName}
        </Typography.Title>

        <Tag color={profile.enabled ? 'green' : 'red'}>{profile.enabled ? 'Đang hoạt động' : 'Bị khoá'}</Tag>
      </Flex>

      <Card title="Thông tin cá nhân" className="mb-6!">
        <Row gutter={[16, 16]}>
          <Col xs={24} md={12}>
            <ProfileRow icon={<IdcardOutlined />} label="Họ và tên" value={fullName} />
          </Col>

          <Col xs={24} md={12}>
            <Flex gap={8}>
              <InfoCircleOutlined />
              <b>Giới tính:</b>{' '}
              {profile.gender === true ? (
                <Tag color="blue">Nam</Tag>
              ) : profile.gender === false ? (
                <Tag color="magenta">Nữ</Tag>
              ) : (
                '—'
              )}
            </Flex>
          </Col>

          <Col xs={24} md={12}>
            <ProfileRow
              icon={<CalendarOutlined />}
              label="Ngày sinh"
              value={profile.birthday ? dayjs(profile.birthday).format('DD/MM/YYYY') : '—'}
            />
          </Col>

          <Col xs={24} md={12}>
            <ProfileRow icon={<IdcardOutlined />} label="Lớp" value={profile.className || '—'} />
          </Col>

          <Col xs={24} md={12}>
            <ProfileRow icon={<IdcardOutlined />} label="Thế hệ" value={profile.generation || '—'} />
          </Col>

          <Col span={24}>
            <ProfileRow icon={<InfoCircleOutlined />} label="Mô tả" value={profile.description || '—'} />
          </Col>
        </Row>
      </Card>

      <Card title="Thông tin liên hệ" className="mb-6!">
        <Row gutter={[16, 16]}>
          <Col xs={24} md={12}>
            <ProfileRow icon={<MailOutlined />} label="Email" value={profile.email || '—'} />
          </Col>

          <Col xs={24} md={12}>
            <ProfileRow icon={<PhoneOutlined />} label="Số điện thoại" value={profile.phone || '—'} />
          </Col>
        </Row>
      </Card>

      <Card title="Thông tin tài khoản hệ thống" className="shadow-md">
        <Row gutter={[16, 16]}>
          <Col xs={24} md={12}>
            <ProfileRow icon={<UserOutlined />} label="Tên đăng nhập" value={profile.username} />
          </Col>

          <Col xs={24} md={12}>
            <Flex gap={8}>
              {profile.superuser ? (
                <CheckCircleOutlined style={{ color: 'green' }} />
              ) : (
                <CloseCircleOutlined style={{ color: 'red' }} />
              )}
              <b>Superuser:</b> {profile.superuser ? 'Có' : 'Không'}
            </Flex>
          </Col>

          <Col xs={24} md={12}>
            <ProfileRow icon={<CalendarOutlined />} label="Kỳ hiện tại" value={profile.semesterId} />
          </Col>

          <Col xs={24} md={12}>
            <ProfileRow
              icon={<CalendarOutlined />}
              label="Ngày tạo"
              value={dayjs(profile.createdAt).format('DD/MM/YYYY HH:mm')}
            />
          </Col>

          <Col xs={24} md={12}>
            <ProfileRow
              icon={<CalendarOutlined />}
              label="Cập nhật"
              value={dayjs(profile.updatedAt).format('DD/MM/YYYY HH:mm')}
            />
          </Col>
        </Row>
      </Card>
    </Fragment>
  )
}
