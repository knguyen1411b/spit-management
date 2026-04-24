export interface IFormLogin {
  username: string
  password: string
}

export interface ITokenResponse {
  accessToken: string
  accessTokenExpiration: number
  refreshToken: string
  refreshTokenExpiration: number
}

export interface IChangePassword {
  oldPassword: string
  newPassword: string
  confirmNewPassword?: string
}

export interface IBasePremsDTO {
  id: number
  code: string
  name: string
  description: string
  createdAt: string
  updatedAt: string
}

export interface IRoleCreateDTO {
  code: string
  name: string
  description: string
  permissionIds: number[]
}

export interface IProfile {
  id: number
  username: string
  passwordChanged: boolean
  lastName?: string
  firstName?: string
  gender?: boolean
  birthday?: string
  email?: string
  phone?: string
  className?: string
  avatar?: string
  generation?: string
  description?: string
  permissions: string[]
  enabled: boolean
  superuser: boolean
  semesterId: number
  createdAt: string
  updatedAt: string
}
