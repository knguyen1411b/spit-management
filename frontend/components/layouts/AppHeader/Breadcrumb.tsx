'use client'

import { BREADCRUMB_CONFIG } from '@/constants/breadcrumb-map'
import { EyeOutlined, HomeOutlined } from '@ant-design/icons'
import { Breadcrumb as AntBreadcrumb } from 'antd'
import { usePathname } from 'next/navigation'
import Link from 'next/link'

export const Breadcrumb = () => {
  const pathname = usePathname()
  const segments = pathname.split('/').filter(Boolean)

  const items = [
    {
      title: (
        <Link href="/" className="flex items-center">
          <HomeOutlined className="mr-2" />
          <span>Trang chủ</span>
        </Link>
      )
    },
    ...segments.map((segment, index) => {
      const href = '/' + segments.slice(0, index + 1).join('/')
      const isLast = index === segments.length - 1

      if (/^\d+$/.test(segment)) {
        return {
          key: href,
          title: (
            <span className="flex items-center gap-1 font-semibold">
              <EyeOutlined />
              <span>Chi tiết</span>
            </span>
          )
        }
      }

      const content = (
        <span className={`flex items-center gap-1 ${isLast ? 'font-semibold' : ''}`}>
          {BREADCRUMB_CONFIG[segment].icon}
          <span>{BREADCRUMB_CONFIG[segment].label ?? segment.replace(/-/g, ' ')}</span>
        </span>
      )

      return {
        key: href,
        title: isLast ? content : <Link href={href}>{content}</Link>
      }
    })
  ]

  return <AntBreadcrumb items={items} className="text-sm" />
}
