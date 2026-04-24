'use client'

import { Search } from 'lucide-react'
import { Input } from 'antd'

export const GlobalSearch = () => {
  return (
    <div className="w-full max-w-[250px] hidden lg:flex">
      <Input
        size="large"
        placeholder="Tìm kiếm..."
        prefix={<Search size={18} className="text-gray-400" />}
        suffix={<span className="rounded-md border border-gray-200/20 px-2 py-0.5 text-xs text-gray-500">⌘ K</span>}
      />
    </div>
  )
}
