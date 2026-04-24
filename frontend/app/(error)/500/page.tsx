'use client'

import { ReloadOutlined, FrownOutlined } from '@ant-design/icons'
import { getHealthCheckAction } from '@/services/auth.service'
import { useQuery } from '@tanstack/react-query'
import Title from 'antd/es/typography/Title'
import { useRouter } from 'next/navigation'
import Text from 'antd/es/typography/Text'
import { Button } from '@heroui/button'
import { motion } from 'framer-motion'
import { useAuth } from '@/contexts'
import { Result, Flex } from 'antd'
import { useEffect } from 'react'

export default function NotFound() {
  const router = useRouter()
  const { refetch } = useAuth()

  const { data, isLoading } = useQuery({
    queryKey: ['health-check-recovery'],
    queryFn: getHealthCheckAction,
    refetchInterval: 3000,
    retry: true,
    retryDelay: 3000
  })

  useEffect(() => {
    if (data?.success) {
      refetch()
      router.push('/')
    }
  }, [data, router, refetch])

  return (
    <Flex vertical justify="center" align="center" className="min-h-screen">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6, ease: 'easeOut' }}
      >
        <Result
          status="500"
          icon={
            <div className="text-9xl font-bold text-gray-200 select-none dark:text-gray-800">
              4<FrownOutlined className="text-blue-500 dark:text-blue-400" />4
            </div>
          }
          title={
            <Title level={2} className="text-5xl font-bold text-gray-800 dark:text-gray-100">
              Oops! Máy chủ gặp sự cố.
            </Title>
          }
          subTitle={
            <Text className="text-lg text-gray-600 dark:text-gray-400">
              {isLoading ? 'Đang thử kết nối lại...' : 'Vui lòng chờ vài giây'}
            </Text>
          }
          extra={
            <Button
              startContent={<ReloadOutlined />}
              onPress={() => {
                router.refresh()
              }}
              color="primary"
            >
              Thử lại
            </Button>
          }
        />
      </motion.div>
    </Flex>
  )
}
