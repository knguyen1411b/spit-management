'use client'

import { Button, Modal, Descriptions, Form, Typography, Input, Space } from 'antd'
import { IAddBoardMemberDTO, IBoardMember } from '@/types'
import { Fragment, useCallback, useState } from 'react'
import { Permissions } from '@/constants/permission'
import { EyeOutlined } from '@ant-design/icons'
import { useBoardMembers } from '@/hooks/data'
import { useToast, useAuth } from '@/contexts'

interface Props {
  id: number
  member: IBoardMember
}

export default function BoardMemberView({ id, member }: Props) {
  const toast = useToast()
  const { hasPermission } = useAuth()
  const { updateMember } = useBoardMembers(id)

  const [open, setOpen] = useState(false)
  const [form] = Form.useForm<IAddBoardMemberDTO>()
  const [loading, setLoading] = useState(false)

  const hasPermissionToUpdate = hasPermission([Permissions.BoardMember.UPDATE])

  const showModal = useCallback(() => {
    form.setFieldsValue({
      position: member.position,
      descriptionBoard: member.descriptionBoard || ''
    })
    setOpen(true)
  }, [member, form])

  const handleCancel = () => {
    if (!loading) setOpen(false)
  }

  const handleSubmit = async (values: IAddBoardMemberDTO) => {
    try {
      setLoading(true)
      await updateMember({ memberId: member.member.id, data: values })
      toast.success('Cập nhật thông tin thành viên ban thành công')
      setOpen(false)
    } catch (err: any) {
      toast.error(err?.response?.data?.message || 'Cập nhật thất bại')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Fragment>
      <Button type="primary" icon={<EyeOutlined />} size="small" onClick={showModal} />

      <Modal
        open={open}
        onCancel={handleCancel}
        footer={null}
        centered
        width={600}
        title={<Typography.Title level={3}>Chi tiết thành viên ban</Typography.Title>}
      >
        <Descriptions bordered column={1} size="middle" className="mb-4">
          <Descriptions.Item label="Họ tên">
            {member.member.lastName} {member.member.firstName}
          </Descriptions.Item>

          <Descriptions.Item label="Giới tính">{member.member.gender ? 'Nam' : 'Nữ'}</Descriptions.Item>

          <Descriptions.Item label="Email">{member.member.email}</Descriptions.Item>

          <Descriptions.Item label="Số điện thoại">{member.member.phone}</Descriptions.Item>

          <Descriptions.Item label="Ngày tham gia">
            {new Date(member.createdAt).toLocaleString('vi-VN')}
          </Descriptions.Item>

          <Descriptions.Item label="Cập nhật gần nhất">
            {new Date(member.updatedAt).toLocaleString('vi-VN')}
          </Descriptions.Item>
        </Descriptions>

        <Form
          form={form}
          layout="vertical"
          disabled={loading}
          onFinish={handleSubmit}
          initialValues={{
            position: member.position,
            descriptionBoard: member.descriptionBoard
          }}
        >
          <Form.Item
            name="position"
            label="Chức vụ trong ban"
            rules={[{ required: true, message: 'Chức vụ là bắt buộc' }]}
          >
            <Input placeholder="Nhập chức vụ" disabled={!hasPermissionToUpdate} />
          </Form.Item>

          <Form.Item name="descriptionBoard" label="Mô tả">
            <Input.TextArea rows={3} placeholder="Nhập mô tả (tuỳ chọn)" disabled={!hasPermissionToUpdate} />
          </Form.Item>

          {hasPermissionToUpdate && (
            <Space className="flex justify-end w-full mt-2">
              <Button onClick={handleCancel}>Hủy</Button>
              <Button type="primary" loading={loading} onClick={() => form.submit()}>
                Cập nhật
              </Button>
            </Space>
          )}
        </Form>
      </Modal>
    </Fragment>
  )
}
