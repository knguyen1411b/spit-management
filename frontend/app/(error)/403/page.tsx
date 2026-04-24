'use client'

import { ArrowLeftOutlined, FrownOutlined } from '@ant-design/icons'
import Title from 'antd/es/typography/Title'
import { useRouter } from 'next/navigation'
import Text from 'antd/es/typography/Text'
import { Button } from '@heroui/button'
import { motion } from 'framer-motion'
import { Result, Flex } from 'antd'

export default function NotFound() {
  const { back } = useRouter()
  return (
    <Flex vertical justify="center" align="center" className="min-h-screen">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6, ease: 'easeOut' }}
      >
        <Result
          status="403"
          icon={
            <div className="text-9xl font-bold text-gray-200 select-none dark:text-gray-800">
              4<FrownOutlined className="text-blue-500 dark:text-blue-400" />4
            </div>
          }
          title={
            <Title level={2} className="text-5xl font-bold text-gray-800 dark:text-gray-100">
              Không có quyền truy cập
            </Title>
          }
          subTitle={
            <Text className="text-lg text-gray-600 dark:text-gray-400">
              Có vẻ như bạn không có quyền truy cập vào trang này.
            </Text>
          }
          extra={
            <Button startContent={<ArrowLeftOutlined />} onPress={() => back()} color="primary">
              Trở lại
            </Button>
          }
        />
      </motion.div>
    </Flex>
  )
}
