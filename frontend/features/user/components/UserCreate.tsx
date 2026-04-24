'use client'

import { Button, Col, Form, Input, Modal, Row, Select, Transfer, Typography } from 'antd'
import { Fragment, Key, useCallback, useMemo, useState } from 'react'
import ModalError from '@/components/common/ModalError'
import { IBasePremsDTO, IUserCreateDTO } from '@/types'
import { PlusCircleOutlined } from '@ant-design/icons'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { useRoles, useUsers } from '@/hooks/data'
import Title from 'antd/es/typography/Title'
import { useToast } from '@/contexts'

interface ITransferItem extends IBasePremsDTO {
  key: Key
  name: string
}

export default function UserCreate() {
  const isMobile = useIsMobile()
  const { roles } = useRoles({ size: 2000 })
  const { createUser } = useUsers()
  const { success } = useToast()

  const [form] = Form.useForm<IUserCreateDTO>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [selectedRoles, setSelectedRoles] = useState<Key[]>([])

  const [modalError, setModalError] = useState<string | null>('null')

  const usernameValue = Form.useWatch('username', form)
  const passwordValue = Form.useWatch('password', form)

  const transferDataSource = useMemo<ITransferItem[]>(() => {
    return (roles || []).map(role => ({
      ...role,
      key: role.id,
      name: role.name
    }))
  }, [roles])

  const showModal = useCallback(() => {
    setIsModalOpen(true)
    setModalError(null)
    setSelectedRoles([])
    form.resetFields()
  }, [form])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)

      const formData = await form.validateFields()
      formData.username = formData.username?.trim()
      formData.password = formData.password.trim()
      formData.roleIds = selectedRoles.map(key => Number(key))

      const response = await createUser(formData)

      if (!response?.success) {
        setModalError(response?.message || 'Tạo vai trò thất bại')
        return
      }

      success('Tạo vai trò thành công.')
      setIsModalOpen(false)
      form.resetFields()
      setSelectedRoles([])
    } catch (err: any) {
      setModalError(err?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [form, createUser, success, selectedRoles])

  const isSubmitDisabled = loading || !usernameValue || !passwordValue

  const handleCancel = useCallback(() => {
    setIsModalOpen(false)
  }, [])

  const handleChangeTransfer = useCallback((keys: Key[]) => {
    setSelectedRoles(keys)
  }, [])

  const filterOptionTransfer = useCallback(
    (input: string, option: ITransferItem) =>
      option.code.toLowerCase().includes(input.toLowerCase()) ||
      option.name.toLowerCase().includes(input.toLowerCase()),
    []
  )

  const renderTransferItem = useCallback((perm: ITransferItem) => <span>{perm.name}</span>, [])

  return (
    <Fragment>
      <Button type="primary" icon={<PlusCircleOutlined />} onClick={showModal}>
        Tạo người dùng mới
      </Button>

      <Modal
        title={
          <Typography>
            <Title level={3}>Tạo người dùng mới</Title>
          </Typography>
        }
        centered
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        okText="Tạo người dùng"
        cancelText="Hủy"
        width={700}
        okButtonProps={{ loading, disabled: isSubmitDisabled }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="username"
                label="Tên người dùng"
                rules={[{ required: true, message: 'Tên người dùng là bắt buộc' }]}
              >
                <Input placeholder="Nhập mã người dùng" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="password"
                label="Mật khẩu"
                rules={[
                  {
                    required: true,
                    message: 'Mật khẩu là bắt buộc'
                  },
                  {
                    min: 6,
                    message: 'Mật khẩu phải có ít nhất 6 ký tự'
                  }
                ]}
              >
                <Input placeholder="Nhập mật khẩu" type="password" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item name="roleIds" label="Chọn vai trò">
            {isMobile ? (
              <Select
                mode="multiple"
                placeholder="Chọn vai trò"
                value={selectedRoles}
                showSearch={{
                  optionFilterProp: 'label'
                }}
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
