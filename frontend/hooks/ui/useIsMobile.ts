import { Grid } from 'antd'

export const useIsMobile = (): boolean => {
  return Grid.useBreakpoint()?.lg === false
}
