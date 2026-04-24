'use server'

import { API_ENDPOINT } from '@/constants/api-endpoints'
import { api, createServerApi } from '@/libs/api'
import { ISearchParams } from '@/types'

/**
 * Permission API
 */
const permissionApi = createServerApi(API_ENDPOINT.PERMISSION)

/** Get all permissions */
export const getAllPermissions = permissionApi.list

/** Get permission detail */
export const getPermissionsDetailAction = permissionApi.detail

/**
 * Role API
 */
const roleApi = createServerApi(API_ENDPOINT.ROLE)

/** Get list of roles */
export const getListRolesAction = roleApi.list

/** Create new role */
export const createRoleAction = roleApi.create

/** Update role by ID */
export const updateRoleAction = roleApi.update

/** Delete role by ID */
export const deleteRoleAction = roleApi.delete

/** Get role detail by ID */
export const getRoleDetailAction = roleApi.detail

/**
 * User API
 */
const userApi = createServerApi(API_ENDPOINT.USER)

/** Get list of users */
export const getListUsersAction = userApi.list

/** Create new user */
export const createUserAction = userApi.create

/** Update user by ID */
export const updateUserAction = userApi.patch

/** Delete user by ID */
export const deleteUserAction = userApi.delete

/** Get user detail by ID */
export const getUserDetailAction = userApi.detail

/**
 * Semester API
 */
const semesterApi = createServerApi(API_ENDPOINT.SEMESTER)

/** Get list of semesters */
export const getListSemestersAction = semesterApi.list

/** Create new semester */
export const createSemesterAction = semesterApi.create

/** Update semester by ID */
export const updateSemesterAction = semesterApi.update

/** Delete semester by ID */
export const deleteSemesterAction = semesterApi.delete

/** Get semester detail by ID */
export const getSemesterDetailAction = semesterApi.detail

/** Copy semester by ID */
export const copySemesterAction = async (data: any) => createServerApi(`${API_ENDPOINT.SEMESTER}/copy`).create(data)

/**
 * Member API
 */
const memberApi = createServerApi(API_ENDPOINT.MEMBER)

/** Get list of members */
export const getListMembersAction = memberApi.list

/** Create new member */
export const createMemberAction = memberApi.create

/** Update member by ID */
export const updateMemberAction = memberApi.update

/** Delete member by ID */
export const deleteMemberAction = memberApi.delete

/** Get member detail by ID */
export const getMemberDetailAction = memberApi.detail

/**
 * Board API
 */
const boardApi = createServerApi(API_ENDPOINT.BOARD)

/** Get list of boards */
export const getListBoardsAction = boardApi.list

/** Create new board */
export const createBoardAction = boardApi.create

/** Update board by ID */
export const updateBoardAction = boardApi.update

/** Delete board by ID */
export const deleteBoardAction = boardApi.delete

/** Get board detail by ID */
export const getBoardDetailAction = boardApi.detail

/**
 * Task API
 */
const taskApi = createServerApi(API_ENDPOINT.TASK)
const myTaskApi = createServerApi(API_ENDPOINT.MY_TASK)

/** Get list of tasks */
export const getListTasksAction = taskApi.list

/** Create new task */
export const createTaskAction = taskApi.create

/** Update task by ID */
export const updateTaskAction = taskApi.update

/** Delete task by ID */
export const deleteTaskAction = taskApi.delete

/** Get task detail by ID */
export const getTaskDetailAction = taskApi.detail

/** Get my tasks */
export const getMyTasksAction = myTaskApi.list

/**
 * Check Request API
 */
const checkRequestApi = createServerApi(API_ENDPOINT.TASK_REQUEST)

/** Get list of check requests */
export const getListTaskRequestsAction = checkRequestApi.list

/** Get static task data */
export const getStatisticTasksAction = async () => createServerApi(API_ENDPOINT.STATISTIC.TASK).list()

/** Get statistic member data */
export const getStatisticMembersAction = async () => createServerApi(API_ENDPOINT.STATISTIC.MEMBER).list()

/* eslint-disable require-await */

export const updateUserSemesterAction = async (userId: number, semesterId: number) =>
  await api.patch(`${API_ENDPOINT.USER}/${userId}/semester`, { semesterId }, { authenticated: true })

/**
 * Check Request API
 */

/** Create a new check request */
export const createTaskRequestAction = async (requestId: number, description: string) =>
  await createServerApi(`${API_ENDPOINT.CHECK_REQUEST_ID(requestId)}/pending`).create({ description })

export const rejectTaskRequestAction = async (requestId: number) =>
  createServerApi(`${API_ENDPOINT.CHECK_REQUEST_ID(requestId)}/reject`).create({})

/** Approve a check request */
export const approveTaskRequestAction = async (requestId: number) =>
  createServerApi(`${API_ENDPOINT.CHECK_REQUEST_ID(requestId)}/approve`).create({})

/**
 * Board Members API
 */

/** Get list of board members */
export const getListBoardMembersAction = async (boardId: number, params: ISearchParams) =>
  createServerApi(API_ENDPOINT.BOARD_MEMBER(boardId)).list(params)

/** Add member to a board */
export const addBoardMemberAction = async (boardId: number, data: any) =>
  createServerApi(API_ENDPOINT.BOARD_MEMBER(boardId)).create(data)

/** Remove member from a board */
export const removeBoardMemberAction = async (boardId: number, memberId: number) =>
  createServerApi(API_ENDPOINT.BOARD_MEMBER(boardId)).delete(memberId)

/** Update board member details */
export const updateBoardMemberAction = async (boardId: number, memberId: number, data: any) =>
  createServerApi(API_ENDPOINT.BOARD_MEMBER(boardId)).update(memberId, data)

/** Get detail of a board member */
export const getBoardMemberDetailAction = async (boardId: number, memberId: number) =>
  createServerApi(API_ENDPOINT.BOARD_MEMBER(boardId)).detail(memberId)

const notificationApi = createServerApi(API_ENDPOINT.NOTIFICATION)

export const getListNotifications = notificationApi.list

export const createNotification = notificationApi.create

export const readNotificationAction = async (id: number) =>
  createServerApi(`${API_ENDPOINT.NOTIFICATION}/${id}/read`).put()

export const getUnreadNotifications = async () => createServerApi(`${API_ENDPOINT.NOTIFICATION}/unread-count`).list()
