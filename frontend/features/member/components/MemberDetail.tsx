'use client'

import {
  HistoryOutlined,
  IdcardOutlined,
  InfoCircleOutlined,
  PhoneOutlined,
  MailOutlined,
  CalendarOutlined,
  ArrowLeftOutlined
} from '@ant-design/icons'
import { Divider, Flex, Tag, Row, Col, Typography, Button } from 'antd'
import MemberAvatarUpload from './MemberAvatarUpload'
import { notFound, useRouter } from 'next/navigation'
import { Permissions } from '@/constants/permission'
import { Heading } from '@/components/ui/heading'
import MemberUpdate from './MemberUpdate'
import LoadingScreen from '@/app/loading'
import { useMembers } from '@/hooks/data'
import { useAuth } from '@/contexts'
import { Fragment } from 'react'
import dayjs from 'dayjs'

function DetailItem({ icon, label, value }: { icon: any; label: string; value: any }) {
  return (
    <Flex gap={8}>
      {icon} <b>{label}:</b> {value}
    </Flex>
  )
}

export default function MemberDetail({ id }: { id: number }) {
  const { data: memberData, isLoading } = useMembers().useMemberDetail(id)
  const { back } = useRouter()
  const { hasPermission } = useAuth()
  const data = memberData?.data

  if (memberData?.statusCode === 404) {
    notFound()
  }

  if (isLoading) {
    return <LoadingScreen />
  }

  return (
    <Fragment>
      <Heading title="Chi tiết thành viên" description="Xem thông tin chi tiết của thành viên" />

      <Divider />

      <Row gutter={[32, 32]}>
        <Col xs={24} md={10}>
          <Flex vertical align="center" gap={14}>
            <MemberAvatarUpload memberId={id} avatarUrl={data?.avatar} />

            <Typography.Title level={4} style={{ marginTop: 10 }}>
              {data?.lastName} {data?.firstName}
            </Typography.Title>

            <Flex vertical gap={10} className="w-full max-w-[260px] text-center">
              <Flex justify="center" gap={6}>
                <InfoCircleOutlined /> {data?.gender ? <Tag color="blue">Nam</Tag> : <Tag color="magenta">Nữ</Tag>}
              </Flex>

              <Flex justify="center" gap={6}>
                <CalendarOutlined /> {dayjs(data?.birthday).format('DD/MM/YYYY')}
              </Flex>

              <Flex justify="center" gap={6}>
                <IdcardOutlined /> <b>MSSV:</b> {data?.username}
              </Flex>
            </Flex>
          </Flex>
        </Col>

        <Col xs={24} md={14}>
          <Flex vertical gap={18}>
            <DetailItem icon={<MailOutlined />} label="Email" value={data?.email} />
            <DetailItem icon={<PhoneOutlined />} label="Số điện thoại" value={data?.phone} />
            <DetailItem icon={<IdcardOutlined />} label="Lớp" value={data?.className} />
            <DetailItem icon={<IdcardOutlined />} label="Thế hệ" value={data?.generation} />
            <DetailItem icon={<InfoCircleOutlined />} label="Mô tả" value={data?.description || '—'} />

            <DetailItem
              icon={<HistoryOutlined />}
              label="Ngày tạo"
              value={dayjs(data?.createdAt).format('DD/MM/YYYY HH:mm')}
            />
            <DetailItem
              icon={<HistoryOutlined />}
              label="Ngày cập nhật"
              value={dayjs(data?.updatedAt).format('DD/MM/YYYY HH:mm')}
            />
          </Flex>
        </Col>
      </Row>

      <Divider />

      <Flex justify="space-between" align="center">
        <Button icon={<ArrowLeftOutlined />} onClick={() => back()}>
          Quay lại
        </Button>
        {hasPermission([Permissions.Member.UPDATE]) && <MemberUpdate id={id} member={data} />}
      </Flex>
    </Fragment>
  )
}
