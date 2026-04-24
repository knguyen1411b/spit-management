'use client'

import { BellOutlined, WarningOutlined, AlertOutlined, InfoCircleOutlined } from '@ant-design/icons'
import { NotificationCreate } from './components/NotificationCreate'
import { useNotifications } from '@/hooks/data/use-notifitions'
import { Accordion, AccordionItem } from '@heroui/accordion'
import relativeTime from 'dayjs/plugin/relativeTime'
import { useIsMobile } from '@/hooks/ui/useIsMobile'
import { Permissions } from '@/constants/permission'
import { parseAsInteger, useQueryState } from 'nuqs'
import { Tag, Empty, Flex, Pagination } from 'antd'
import { Heading } from '@/components/ui/heading'
import LoadingScreen from '@/app/loading'
import { useAuth } from '@/contexts'
import vi from 'dayjs/locale/vi'
import dayjs from 'dayjs'

dayjs.extend(relativeTime)
dayjs.locale(vi)

export default function NotificationMain() {
  const isMobile = useIsMobile()
  const { hasPermission } = useAuth()
  const [page, setPage] = useQueryState('page', parseAsInteger.withDefault(1))
  const [size, setSize] = useQueryState('size', parseAsInteger.withDefault(10))

  const { notifications, isLoading, markAsRead, meta } = useNotifications({
    page,
    size,
    sort: 'notification.createdAt,desc'
  })

  if (!notifications || isLoading) return <LoadingScreen />

  return (
    <Flex vertical gap={10}>
      <Flex justify="space-between" align="center" wrap={isMobile} gap={16}>
        <Flex vertical gap={4}>
          <Heading title="Thông báo" description="Danh sách thông báo trong hệ thống" />
        </Flex>
        {hasPermission([Permissions.Notification.CREATE]) && <NotificationCreate />}
      </Flex>

      <Flex vertical gap={12}>
        {notifications.length === 0 && (
          <Empty
            image={<BellOutlined style={{ fontSize: 64, color: '#d9d9d9' }} />}
            description={
              <Flex vertical gap={8}>
                <span className="text-gray-500 text-lg">Không có thông báo nào</span>
                <span className="text-gray-400 text-sm">Bạn sẽ nhận được thông báo khi có cập nhật mới</span>
              </Flex>
            }
          />
        )}
        <Accordion>
          {notifications.map(item => (
            <AccordionItem
              key={item.id}
              textValue={item.title}
              onPress={() => {
                if (!item.read) markAsRead(item.id)
              }}
              startContent={
                item.type === 'WARNING' ? (
                  <WarningOutlined className="text-yellow-500!" />
                ) : item.type === 'INFO' ? (
                  <InfoCircleOutlined className="text-blue-500!" />
                ) : (
                  <AlertOutlined className="text-green-500!" />
                )
              }
              title={
                <>
                  <span className="text-blue-700">{item.title}</span>{' '}
                  {!item.read && (
                    <Tag className="animate-pulse">
                      <span className="flex items-center gap-1">
                        <span className="w-2 h-2 bg-blue-500 rounded-full"></span>
                        Mới
                      </span>
                    </Tag>
                  )}
                </>
              }
              subtitle={dayjs(item.createdAt).fromNow()}
            >
              {item.content}
            </AccordionItem>
          ))}
        </Accordion>
      </Flex>

      <Flex justify="center" align="center">
        <Pagination
          current={meta.page}
          total={meta.total}
          pageSize={meta.size}
          onChange={(newPage, newSize) => {
            setPage(newPage)
            if (newSize !== size) setSize(newSize)
          }}
          showSizeChanger
          showQuickJumper
          showTotal={(total, range) => `${range[0]}-${range[1]} của ${total} thông báo`}
          pageSizeOptions={['5', '10', '20', '50']}
          responsive={isMobile}
        />
      </Flex>
    </Flex>
  )
}
