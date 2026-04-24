export const API_ENDPOINT = {
  AUTH: {
    LOGIN: '/api/v1/auth/login',
    REFRESH: '/api/v1/auth/refresh-token',
    CHANGE_PASSWORD: '/api/v1/auth/change-password',
    PROFILE: '/api/v1/auth/profile'
  },
  PERMISSION: '/api/v1/permissions',
  ROLE: '/api/v1/roles',
  USER: '/api/v1/users',
  SEMESTER: '/api/v1/semesters',
  MEMBER: '/api/v1/members',
  BOARD: '/api/v1/boards',
  TASK: '/api/v1/tasks',
  MY_TASK: '/api/v1/tasks/me',
  TASK_REQUEST: '/api/v1/task-requests',
  NOTIFICATION: '/api/v1/notifications',
  STATISTIC: {
    TASK: '/api/v1/statistics/tasks',
    MEMBER: '/api/v1/statistics/members'
  },
  BOARD_MEMBER: (boardId: number) => `/api/v1/board/${boardId}/members`,
  CHECK_REQUEST_ID: (taskId: number) => `/api/v1/task-requests/${taskId}`
}
