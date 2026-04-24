import { BasePremsDTO } from './auth'

export interface IPageableMeta {
  page: number
  size: number
  total: number
  pages: number
  sort: string
}
export interface IBaseApiResponse {
  success: boolean
  statusCode: number
  message: string
}

export interface IDataApiResponse<T> extends IBaseApiResponse {
  data: T
}

export interface IPageableApiResponse<T> extends IBaseApiResponse {
  data: T[]
  meta: IPageableMeta
}

export interface ISearchParams {
  page?: number
  size?: number
  keyword?: string
  sort?: string
  [key: string]: any
}

export interface IUser {
  id: number
  username: string
  enabled: boolean
  superuser: boolean
  roles: BasePremsDTO[]
  createdAt: string
  updatedAt: string
}

export interface IUserCreateDTO {
  username?: string
  password: string
  roleIds: number[]
  enabled?: boolean
}

export interface ISemester {
  id: number
  code: string
  name: string
  description: string
  startDate: string
  endDate: string
  semesterOrder: number
  current: boolean
  createdAt: string
  updatedAt: string
}

export interface IMember {
  id: number
  lastName: string
  firstName: string
  gender: boolean
  birthday: string
  email: string
  phone: string
  className: string
  avatar: string
  generation: string
  description: string
  username: string
  createdAt: string
  updatedAt: string
}

export interface IBoard {
  id: number
  name: string
  description: string
  createdAt: string
  updatedAt: string
}

export interface IBoardMember {
  id: number
  member: IMember
  position: string
  descriptionBoard: string
  createdAt: string
  updatedAt: string
}

export interface IAddBoardMemberDTO {
  position: string
  descriptionBoard: string
  memberId: number
}

export enum TaskType {
  TASK = 'TASK',
  EVENT = 'EVENT',
  OTHER = 'OTHER'
}

export interface IMemberTask {
  id: number
  lastName: string
  firstName: string
  email: string
  phone: string
  className: string
  avatar: string
  status: TaskRequestStatus
  description: string
  requestedAt: string | null
  approvedAt: string | null
}

export interface ITask {
  id: number
  title: string
  description: string
  type: TaskType
  date: string
  members: IMemberTask[]
  createdAt: string
  updatedAt: string
}

export interface IMyTask {
  taskId: number
  title: string
  description: string
  type: TaskType
  status: TaskRequestStatus
  date: string
  joined: boolean
  joinedAt: string
  taskMemberId: number
  taskMemberDescription: string
}

export interface ITaskCreate {
  title: string
  description: string
  type: TaskType
  date: string
  memberIds: number[]
}

export enum TaskRequestStatus {
  NONE = 'NONE',
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

export interface ICheckRequest {
  id: number
  taskId: number
  memberId: number
  memberName: string
  taskTitle: string
  taskDate: string
  status: TaskRequestStatus
  description: string
  requestedAt: string | null
  approvedAt: string | null
  createdAt: string
  updatedAt: string
}

export interface ITaskRequestGrouped {
  pending: ICheckRequest[]
  approved: ICheckRequest[]
  rejected: ICheckRequest[]
}

export enum NotificationType {
  INFO = 'INFO',
  WARNING = 'WARNING',
  ALERT = 'ALERT'
}

export interface INotification {
  id: number
  title: string
  content: string
  type: NotificationType
  read: boolean
  createdAt: string
}

export interface ICreateNotificationDTO {
  title: string
  content: string
  type: NotificationType
  receiverIds: number[]
}
