'use client'

import { Button, Col, Form, Input, Modal, Row, Select, Switch, Transfer, Typography } from 'antd'
import { Fragment, Key, useCallback, useMemo, useState } from 'react'
import ModalError from '@/components/common/ModalError'
import { IBasePremsDTO, IUserCreateDTO } from '@/types'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { useRoles, useUsers } from '@/hooks/data'
import { EditOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { useToast } from '@/contexts'

interface ITransferItem extends IBasePremsDTO {
  key: Key
}

export default function UserUpdate({
  id,
  user,
  superuser
}: {
  id: number
  user: IUserCreateDTO & { roleIds?: number[] }
  superuser: boolean
}) {
  const isMobile = useIsMobile()
  const { roles } = useRoles({ size: 2000 })
  const { updateUser } = useUsers()
  const { success } = useToast()

  const [form] = Form.useForm<Partial<IUserCreateDTO>>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [selectedRoles, setSelectedRoles] = useState<Key[]>([])
  const [modalError, setModalError] = useState<string | null>(null)

  const transferDataSource = useMemo<ITransferItem[]>(() => {
    return (roles || []).map(role => ({
      ...role,
      key: role.id
    }))
  }, [roles])

  const showModal = useCallback(() => {
    setIsModalOpen(true)
    setModalError(null)

    form.setFieldsValue({
      password: '',
      enabled: user.enabled,
      roleIds: user.roleIds
    })

    setSelectedRoles(user.roleIds || [])
  }, [form, user.enabled, user.roleIds])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)

      const formData = await form.validateFields()

      const payload = {
        password: formData.password?.trim() ? formData.password : null,
        enabled: formData.enabled,
        roleIds: selectedRoles.map(id => Number(id))
      }

      const response = await updateUser({ id, data: payload })

      if (!response?.success) {
        setModalError(response?.message || 'Cập nhật người dùng thất bại')
        return
      }

      success('Cập nhật người dùng thành công')
      setIsModalOpen(false)
    } catch (err: any) {
      setModalError(err?.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [form, id, selectedRoles, success, updateUser])

  const handleCancel = useCallback(() => {
    if (!loading) setIsModalOpen(false)
  }, [loading])

  const handleChangeTransfer = useCallback((keys: Key[]) => {
    setSelectedRoles(keys)
  }, [])

  const filterOptionTransfer = (input: string, option: ITransferItem) =>
    option.code.toLowerCase().includes(input.toLowerCase()) || option.name.toLowerCase().includes(input.toLowerCase())

  const renderTransferItem = (item: ITransferItem) => <span>{item.name}</span>

  return (
    <Fragment>
      <Button type="primary" icon={<EditOutlined />} onClick={showModal}>
        Chỉnh sửa người dùng
      </Button>

      <Modal
        title={
          <Typography>
            <Title level={3}>Cập nhật người dùng</Title>
          </Typography>
        }
        open={isModalOpen}
        onOk={handleOk}
        centered
        onCancel={handleCancel}
        okText="Cập nhật"
        cancelText="Hủy"
        width={700}
        okButtonProps={{ loading }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="password" label="Mật khẩu (nếu muốn đổi)">
                <Input placeholder="Nhập mật khẩu mới" type="password" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="enabled" label="Kích hoạt" valuePropName="checked">
                <Switch disabled={superuser} />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item label="Vai trò">
            {isMobile ? (
              <Select
                mode="multiple"
                placeholder="Chọn vai trò"
                value={selectedRoles}
                showSearch={{
                  optionFilterProp: 'label'
                }}
                disabled={superuser}
                onChange={setSelectedRoles}
                options={[
                  ...transferDataSource.map(role => ({
                    label: role.name,
                    value: role.id
                  }))
                ]}
              />
            ) : (
              <Transfer
                oneWay
                className="[&_.ant-transfer-section]:w-full! [&_.ant-transfer-section]:h-72!"
                titles={['Tất cả chức vụ', 'Chức vụ đã chọn']}
                dataSource={transferDataSource}
                showSearch
                disabled={superuser}
                targetKeys={selectedRoles}
                onChange={handleChangeTransfer}
                filterOption={filterOptionTransfer}
                render={renderTransferItem}
              />
            )}
          </Form.Item>
        </Form>
        {modalError && <ModalError title={modalError} onClose={() => setModalError(null)} />}
      </Modal>
    </Fragment>
  )
}
