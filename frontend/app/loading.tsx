import { Flex, Spin } from 'antd'

export default function LoadingScreen() {
  return (
    <Flex align="center" justify="center" className="h-screen">
      <Spin size="large" />
    </Flex>
  )
}
