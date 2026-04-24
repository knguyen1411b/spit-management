'use client'

import Title from 'antd/es/typography/Title'
import { useRouter } from 'next/navigation'
import Text from 'antd/es/typography/Text'
import { motion } from 'framer-motion'
import { Flex, Result } from 'antd'

import { ArrowLeftOutlined, FrownOutlined, HomeOutlined } from '@ant-design/icons'
import { Button } from '@heroui/button'

export default function NotFound() {
  const router = useRouter()

  return (
    <Flex vertical justify="center" align="center" className="min-h-screen">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6, ease: 'easeOut' }}
      >
        <Result
          status="404"
          icon={
            <div className="text-9xl font-bold text-gray-200 select-none dark:text-gray-800">
              4<FrownOutlined className="text-blue-500 dark:text-blue-400" />4
            </div>
          }
          title={
            <Title level={2} className="text-5xl font-bold text-gray-800 dark:text-gray-100">
              Oops! Trang không tồn tại
            </Title>
          }
          subTitle={
            <Text className="text-lg text-gray-600 dark:text-gray-400">
              Có thể đường dẫn bị sai hoặc trang đã được di chuyển.
            </Text>
          }
          extra={
            <Flex gap={16} justify="center">
              <Button startContent={<HomeOutlined />} onPress={() => router.push('/')} color="primary">
                Về trang chủ
              </Button>
              <Button startContent={<ArrowLeftOutlined />} onPress={() => router.back()}>
                Quay lại
              </Button>
            </Flex>
          }
        />
      </motion.div>
    </Flex>
  )
}
