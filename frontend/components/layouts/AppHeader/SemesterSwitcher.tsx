'use client'

import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, useDisclosure } from '@heroui/modal'
import { useSemesterSwitcher } from './useSemesterSwitcher'
import { Button } from '@heroui/button'
import { Select, Tag } from 'antd'
import { useState } from 'react'

export const SemesterSwitcher = () => {
  const { semesters, currentSemester, changeSemester } = useSemesterSwitcher()

  const { isOpen, onOpen, onOpenChange } = useDisclosure()
  const [value, setValue] = useState<number | null>(null)

  return (
    <>
      <Tag
        className="cursor-pointer px-3 py-1.5"
        onClick={() => {
          setValue(currentSemester?.id ?? null)
          onOpen()
        }}
      >
        {currentSemester?.name || 'Không có kỳ'}
      </Tag>

      <Modal isOpen={isOpen} onOpenChange={onOpenChange} isDismissable={false}>
        <ModalContent>
          {onClose => (
            <>
              <ModalHeader>Chọn học kì tác nghiệp</ModalHeader>
              <ModalBody>
                <Select
                  className="w-full my-2"
                  value={value}
                  size="large"
                  onChange={setValue}
                  options={semesters.map(s => ({
                    label: s.name,
                    value: s.id
                  }))}
                />
              </ModalBody>
              <ModalFooter>
                <Button onPress={onClose}>Huỷ</Button>
                <Button
                  color="primary"
                  disabled={!value}
                  onPress={async () => {
                    await changeSemester(value!)
                    onClose()
                  }}
                >
                  Xác nhận
                </Button>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  )
}
