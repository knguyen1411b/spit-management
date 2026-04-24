'use client'

import { TeamOutlined, CalendarOutlined, InfoCircleOutlined } from '@ant-design/icons'
import { useBoardMembers, useBoards } from '@/hooks/data'
import { Permissions } from '@/constants/permission'
import { Heading } from '@/components/ui/heading'
import { Divider, Flex, Typography } from 'antd'
import { notFound } from 'next/navigation'
import LoadingScreen from '@/app/loading'
import BoardUpdate from './BoardUpdate'
import BoardMember from './BoardMember'
import { useAuth } from '@/contexts'
import { Fragment } from 'react'
import dayjs from 'dayjs'

export default function BoardDetail({ id }: { id: number }) {
  const { hasPermission } = useAuth()
  const { data: boardData, isLoading: boardLoading } = useBoards().useBoardDetail(id)
  const { members, isLoading: memberLoading } = useBoardMembers(id)

  const board = boardData?.data

  if (boardData?.statusCode === 404) {
    notFound()
  }

  if (boardLoading || memberLoading) return <LoadingScreen />

  return (
    <Fragment>
      <Heading title="Chi tiết ban" description="Xem thông tin chi tiết và quản lý ban" />

      <Divider />

      <Flex justify="space-between" align="flex-start" wrap>
        <Flex vertical gap={14} className="w-full md:w-3/4">
          <Typography.Title level={3}>Thông tin ban</Typography.Title>

          <Flex vertical gap={12}>
            <Flex gap={8}>
              <TeamOutlined />
              <b>Tên ban:</b> {board?.name}
            </Flex>

            <Flex gap={8}>
              <InfoCircleOutlined />
              <b>Mô tả:</b> {board?.description || '—'}
            </Flex>

            <Flex gap={8}>
              <CalendarOutlined />
              <b>Ngày tạo:</b> {dayjs(board?.createdAt).format('DD/MM/YYYY')}
            </Flex>

            <Flex gap={8}>
              <CalendarOutlined />
              <b>Ngày cập nhật:</b> {dayjs(board?.updatedAt).format('DD/MM/YYYY')}
            </Flex>
          </Flex>
        </Flex>

        {hasPermission([Permissions.Board.UPDATE]) && (
          <div className="w-full md:w-auto mt-4 md:mt-0">
            <BoardUpdate id={id} board={board} />
          </div>
        )}
      </Flex>

      <Divider />

      {hasPermission([Permissions.BoardMember.READ]) && <BoardMember id={id} members={members} />}
    </Fragment>
  )
}
