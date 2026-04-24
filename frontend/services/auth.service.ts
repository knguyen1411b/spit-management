'use server'

import { IBaseApiResponse, IChangePassword, IDataApiResponse, IFormLogin, IProfile, ITokenResponse } from '@/types'
import { ACCESS_TOKEN_COOKIE_NAME, REFRESH_TOKEN_COOKIE_NAME } from '@/constants/cookies'
import { API_ENDPOINT } from '@/constants/api-endpoints'
import { cookies } from 'next/headers'
import { api } from '@/libs/api'

/**
 * Perform a health check to verify server status.
 *
 * @returns {Promise<IBaseApiResponse>} API response indicating server health.
 *
 */
export const getHealthCheckAction = async () => (await api.get<IBaseApiResponse>('')) as IBaseApiResponse

/**
 * Authenticate user and set authentication cookies.
 *
 * @param {IFormLogin} param0 - Login form containing username and password.
 * @param {boolean} keepMe - If true, creates long-lived refresh token cookie.
 *
 * @returns {Promise<IBaseApiResponse>} API response object containing login status.
 *
 * @description
 * - Calls login API.
 * - If success:
 *   - Stores access token as HttpOnly cookie.
 *   - Stores refresh token only when `keepMe = true`.
 *   - Token expiration is based on API response or fallback value.
 */
export const loginAction = async ({ username, password }: IFormLogin, keepMe: boolean) => {
  const result = await api.post<ITokenResponse>(API_ENDPOINT.AUTH.LOGIN, {
    username,
    password
  })

  if (result.success && result.data) {
    const cookiesStore = await cookies()

    const { accessToken, accessTokenExpiration, refreshToken, refreshTokenExpiration } = result.data

    // Set Access Token cookie
    cookiesStore.set({
      name: ACCESS_TOKEN_COOKIE_NAME,
      value: accessToken,
      httpOnly: true,
      sameSite: 'lax',
      path: '/',
      maxAge: keepMe ? accessTokenExpiration || 60 * 15 : 60 * 60
    })

    // Set Refresh Token cookie only if keepMe = true
    if (keepMe)
      cookiesStore.set({
        name: REFRESH_TOKEN_COOKIE_NAME,
        value: refreshToken,
        httpOnly: true,
        sameSite: 'lax',
        path: '/',
        maxAge: refreshTokenExpiration || 60 * 60 * 24 * 7
      })
  }

  return result as IBaseApiResponse
}

/**
 * Refresh authentication tokens using a refresh token.
 *
 * @param {string} refreshToken - The refresh token used to acquire new tokens.
 *
 * @returns {Promise<IDataApiResponse<ITokenResponse>>}
 *
 * @description
 * - Calls refresh token API to obtain a new pair of access/refresh tokens.
 * - Middleware will handle setting cookies because server actions cannot modify middleware response.
 */
export const refreshAction = async (refreshToken: string) =>
  (await api.post<ITokenResponse>(API_ENDPOINT.AUTH.REFRESH, {
    refreshToken
  })) as IDataApiResponse<ITokenResponse>

/**
 * Fetch authenticated user's profile.
 *
 * @returns {Promise<IDataApiResponse<IProfile>>}
 *
 * @description
 * - Performs a GET request to `/auth/profile`.
 * - Requires authentication; access token must be attached automatically.
 */
export const getProfileAction = async () =>
  (await api.get<IProfile>(API_ENDPOINT.AUTH.PROFILE, {}, { authenticated: true })) as IDataApiResponse<IProfile>

/**
 * Logout user by clearing authentication cookies.
 *
 * @returns {Promise<IBaseApiResponse>} Simple response confirming logout.
 *
 * @description
 * - Deletes Access Token and Refresh Token cookies.
 * - Does NOT call backend logout; purely client-side logout.
 */
export const logoutAction = async () => {
  const cookiesStore = await cookies()
  cookiesStore.delete(ACCESS_TOKEN_COOKIE_NAME)
  cookiesStore.delete(REFRESH_TOKEN_COOKIE_NAME)
  return { success: true, statusCode: 200, message: 'Đăng xuất thành công' } as IBaseApiResponse
}

/**
 * Change the current authenticated user's password.
 *
 * @param {IChangePassword} data - Old + new password payload.
 *
 * @returns {Promise<IBaseApiResponse>}
 *
 * @description
 * - Calls `/auth/change-password`.
 * - Requires authentication.
 */
export const changePasswordAction = async (data: IChangePassword) =>
  (await api.post<IProfile>(
    API_ENDPOINT.AUTH.CHANGE_PASSWORD,
    { ...data },
    { authenticated: true }
  )) as IBaseApiResponse
