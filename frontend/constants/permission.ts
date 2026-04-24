enum NotificationPermission {
  CREATE = 'notification:create',
  DELETE = 'notification:delete',
  READ = 'notification:read',
  UPDATE = 'notification:update'
}

enum BoardPermission {
  CREATE = 'board:create',
  DELETE = 'board:delete',
  READ = 'board:read',
  UPDATE = 'board:update'
}

enum BoardMemberPermission {
  CREATE = 'board_member:create',
  DELETE = 'board_member:delete',
  READ = 'board_member:read',
  UPDATE = 'board_member:update'
}

enum CheckRequestPermission {
  APPROVE = 'check_request:approve',
  CREATE = 'check_request:create',
  READ = 'check_request:read'
}

enum MemberPermission {
  CREATE = 'member:create',
  DELETE = 'member:delete',
  READ = 'member:read',
  UPDATE = 'member:update'
}

enum PermissionPermission {
  READ = 'permission:read'
}

enum RolePermission {
  CREATE = 'role:create',
  DELETE = 'role:delete',
  READ = 'role:read',
  UPDATE = 'role:update'
}

enum SemesterPermission {
  CREATE = 'semester:create',
  DELETE = 'semester:delete',
  READ = 'semester:read',
  UPDATE = 'semester:update'
}

enum TaskPermission {
  CREATE = 'task:create',
  DELETE = 'task:delete',
  READ = 'task:read',
  UPDATE = 'task:update'
}

enum TaskMemberPermission {
  ASSIGN = 'task_member:assign',
  READ = 'task_member:read',
  UPDATE = 'task_member:update'
}

enum UserPermission {
  CREATE = 'user:create',
  DELETE = 'user:delete',
  READ = 'user:read',
  UPDATE = 'user:update'
}

export const Permissions = {
  ALL: 'all:all',

  Board: BoardPermission,
  BoardMember: BoardMemberPermission,
  CheckRequest: CheckRequestPermission,
  Member: MemberPermission,
  Permission: PermissionPermission,
  Role: RolePermission,
  Semester: SemesterPermission,
  Task: TaskPermission,
  TaskMember: TaskMemberPermission,
  User: UserPermission,
  Notification: NotificationPermission
} as const
