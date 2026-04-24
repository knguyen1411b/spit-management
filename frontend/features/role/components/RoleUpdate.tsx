'use client'

import { Button, Col, Form, Input, Modal, Row, Select, Transfer, Typography } from 'antd'
import { Fragment, Key, useCallback, useMemo, useState } from 'react'
import ModalError from '@/components/common/ModalError'
import { usePermissions, useRoles } from '@/hooks/data'
import { IBasePremsDTO, IRoleCreateDTO } from '@/types'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { EditOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { useToast } from '@/contexts'

interface ITransferItem extends IBasePremsDTO {
  key: Key
}

export default function RoleUpdate({ id, role }: { id: number; role: IRoleCreateDTO & { permissionIds?: number[] } }) {
  const isMobile = useIsMobile()
  const { perms } = usePermissions({ size: 2000 })
  const { updateRole } = useRoles()
  const { success } = useToast()

  const [form] = Form.useForm<IRoleCreateDTO>()
  const [loading, setLoading] = useState(false)
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [selectedPermissions, setSelectedPermissions] = useState<Key[]>([])

  const [modalError, setModalError] = useState<string | null>(null)

  const codeValue = Form.useWatch('code', form)
  const nameValue = Form.useWatch('name', form)

  const transferDataSource = useMemo<ITransferItem[]>(() => {
    return (perms || []).map(permission => ({
      ...permission,
      key: permission.id
    }))
  }, [perms])

  const showModal = useCallback(() => {
    setIsModalOpen(true)
    setModalError(null)

    form.setFieldsValue({
      code: role.code,
      name: role.name,
      description: role.description || ''
    })

    setSelectedPermissions(role.permissionIds || [])
  }, [form, role])

  const handleOk = useCallback(async () => {
    try {
      setLoading(true)

      const formData = await form.validateFields()
      formData.description = formData.description || ''
      formData.permissionIds = selectedPermissions.map(key => Number(key))

      const response = await updateRole({ id, data: formData })

      if (!response?.success) {
        setModalError(response?.message || 'Cập nhật vai trò thất bại')
        return
      }

      success('Cập nhật vai trò thành công')
      setIsModalOpen(false)
    } catch (err: any) {
      setModalError(err?.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại.')
    } finally {
      setLoading(false)
    }
  }, [form, updateRole, id, selectedPermissions, success])

  const isSubmitDisabled = loading || !codeValue || !nameValue || selectedPermissions.length === 0

  const handleCancel = useCallback(() => {
    if (!loading) setIsModalOpen(false)
  }, [loading])

  const handleChangeTransfer = useCallback((keys: Key[]) => {
    setSelectedPermissions(keys)
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
      <Button type="primary" icon={<EditOutlined />} onClick={showModal}>
        Chỉnh sửa vai trò
      </Button>

      <Modal
        title={
          <Typography>
            <Title level={3}>Cập nhật vai trò</Title>
          </Typography>
        }
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        centered
        okText="Cập nhật"
        cancelText="Hủy"
        width={700}
        okButtonProps={{ loading, disabled: isSubmitDisabled }}
        cancelButtonProps={{ disabled: loading }}
      >
        <Form form={form} layout="vertical" disabled={loading}>
          <Row gutter={16}>
            <Col span={isMobile ? 24 : 12}>
              <Form.Item name="code" label="Mã vai trò" rules={[{ required: true, message: 'Mã vai trò là bắt buộc' }]}>
                <Input placeholder="Nhập mã vai trò" />
              </Form.Item>
            </Col>

            <Col span={isMobile ? 24 : 12}>
              <Form.Item
                name="name"
                label="Tên vai trò"
                rules={[{ required: true, message: 'Tên vai trò là bắt buộc' }]}
              >
                <Input placeholder="Nhập tên vai trò" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item name="description" label="Mô tả (tuỳ chọn)">
            <Input.TextArea rows={3} placeholder="Nhập mô tả vai trò" />
          </Form.Item>

          <Form.Item
            label="Phân quyền"
            required
            validateStatus={selectedPermissions.length === 0 ? 'error' : ''}
            help={selectedPermissions.length === 0 ? 'Vui lòng chọn ít nhất một quyền' : ''}
          >
            {isMobile ? (
              <Select
                mode="multiple"
                placeholder="Chọn quyền cho vai trò"
                value={selectedPermissions}
                showSearch={{
                  optionFilterProp: 'label'
                }}
                onChange={setSelectedPermissions}
                options={[
                  ...transferDataSource.map(perm => ({
                    label: perm.name,
                    value: perm.id
                  }))
                ]}
              />
            ) : (
              <Transfer
                oneWay
                className="[&_.ant-transfer-section]:w-full! [&_.ant-transfer-section]:h-72!"
                titles={['Tất cả quyền', 'Quyền đã chọn']}
                dataSource={transferDataSource}
                showSearch
                targetKeys={selectedPermissions}
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
