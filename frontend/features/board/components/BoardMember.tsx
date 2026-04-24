'use client'

import { Avatar, Button, Flex, Table, Tag, Tooltip } from 'antd'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { Permissions } from '@/constants/permission'
import BoardMemberDelete from './BoardMemberDelete'
import { UserOutlined } from '@ant-design/icons'
import BoardMemberView from './BoardMemberView'
import BoardMemberAdd from './BoardMemberAdd'
import Title from 'antd/es/typography/Title'
import { useRouter } from 'next/navigation'
import { Fragment, useMemo } from 'react'
import { IBoardMember } from '@/types'
import { useAuth } from '@/contexts'

export default function BoardMember({ id, members }: { id: number; members: IBoardMember[] }) {
  const isMobile = useIsMobile()
  const { hasPermission } = useAuth()
  const { push } = useRouter()
  const columns = useMemo(
    () => [
      {
        title: 'Thành viên',
        key: 'member',
        render: (_: any, bm: IBoardMember) => {
          const m = bm.member
          return (
            <Flex align="center" gap={10}>
              <Avatar icon={<UserOutlined />} src={m.avatar} />
              <span>
                <b>
                  {m.lastName} {m.firstName}
                </b>
              </span>
            </Flex>
          )
        }
      },
      {
        title: 'Giới tính',
        dataIndex: ['member', 'gender'],
        key: 'gender',
        render: (g: any) => (g ? <Tag color="blue">Nam</Tag> : <Tag color="magenta">Nữ</Tag>)
      },
      {
        title: 'Lớp',
        dataIndex: ['member', 'className'],
        key: 'className'
      },
      {
        title: 'Vị trí',
        dataIndex: 'position',
        key: 'position',
        render: (pos: any) => <Tag color="purple">{pos}</Tag>
      },
      {
        title: 'Mô tả nhiệm vụ',
        dataIndex: 'descriptionBoard',
        key: 'descriptionBoard'
      },
      {
        title: 'Hành động',
        key: 'actions',
        render: (_: any, bm: IBoardMember) => (
          <Flex gap={8}>
            <Tooltip title="Xem thông tin thành viên">
              <BoardMemberView id={id} member={bm} />
            </Tooltip>
            {hasPermission([Permissions.Member.READ]) && (
              <Tooltip title="Xem trang cá nhân">
                <Button
                  type="primary"
                  size="small"
                  icon={<UserOutlined />}
                  onClick={() => push(`/member/${bm.member.id}`)}
                />
              </Tooltip>
            )}
            {hasPermission([Permissions.BoardMember.DELETE]) ? (
              <BoardMemberDelete
                boardId={id}
                memberId={bm.member.id}
                name={`${bm.member.lastName} ${bm.member.firstName}`}
              />
            ) : null}
          </Flex>
        )
      }
    ],
    [hasPermission, id, push]
  )
  return (
    <Fragment>
      <Flex justify="space-between" align="center" wrap={isMobile}>
        <Title level={3}>Thành viên trong ban</Title>
        {hasPermission([Permissions.BoardMember.CREATE]) && <BoardMemberAdd id={id} />}
      </Flex>

      <Table<IBoardMember>
        rowKey="id"
        columns={columns}
        dataSource={members.map(m => ({ ...m, key: m.id }))}
        pagination={false}
        scroll={{ x: 900 }}
      />
    </Fragment>
  )
}
