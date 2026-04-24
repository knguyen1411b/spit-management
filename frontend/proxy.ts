import { ACCESS_TOKEN_COOKIE_NAME, REFRESH_TOKEN_COOKIE_NAME } from '@/constants/cookies'
import type { IBaseApiResponse, IProfile } from './types'
import { API_ENDPOINT } from './constants/api-endpoints'
import { refreshAction } from './services/auth.service'
import { NextRequest, NextResponse } from 'next/server'
import { api } from './libs/api'

/* Middleware to protect routes and handle authentication */
const PUBLIC_PATHS = ['/login', '/500', '/403', '/404']
const SKIP_PATHS = PUBLIC_PATHS.concat([
  '/profile',
  '/',
  '/notification',
  '/schedule',
  '/change-password',
  '/task-request'
])

/* Safe method to get user profile with given access token */
const safeGetProfile = async (accessToken: string) => {
  return await api.get<IProfile>(
    API_ENDPOINT.AUTH.PROFILE,
    {},
    {
      headers: { Authorization: `Bearer ${accessToken}` }
    }
  )
}

const checkPermission = (profile: IProfile | null, pathname: string): boolean => {
  if (SKIP_PATHS.includes(pathname)) return true
  if (!profile || !profile.permissions) return false
  if (profile.permissions.includes('all:all')) return true
  return profile.permissions.includes(pathname.split('/')[1] + ':read')
}

/* Redirect helper functions */
const redirectTo = (url: string, request: NextRequest) => NextResponse.redirect(new URL(url, request.url))

/* Redirect to login and clear auth cookies */
const redirectToLogin = (request: NextRequest, pathname: string) => {
  const res = redirectTo(`/login${pathname !== '/' ? `?redirectTo=${pathname}` : ''}`, request)
  res.cookies.delete(ACCESS_TOKEN_COOKIE_NAME)
  res.cookies.delete(REFRESH_TOKEN_COOKIE_NAME)
  return res
}

/* Main middleware function */
export default async function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl

  /* Allow public paths */
  if (PUBLIC_PATHS.includes(pathname)) {
    return NextResponse.next()
  }

  /* Check backend health */
  const isBackendAlive = await api.get<IBaseApiResponse>('')
  if (isBackendAlive.statusCode !== 200) {
    return redirectTo('/500', request)
  }

  /* Get tokens from cookies */
  const accessToken = request.cookies.get(ACCESS_TOKEN_COOKIE_NAME)?.value
  const refreshToken = request.cookies.get(REFRESH_TOKEN_COOKIE_NAME)?.value

  /* If no tokens, redirect to login */
  if (!accessToken && !refreshToken) {
    return redirectToLogin(request, pathname)
  }

  /* Validate access token and refresh if necessary */
  if (accessToken) {
    const profile = await safeGetProfile(accessToken)

    if (profile.success && profile.statusCode === 200) {
      if (!profile?.data?.passwordChanged && pathname !== '/change-password')
        return redirectTo('/change-password', request)

      if (!checkPermission(profile.data ?? null, pathname)) return redirectTo('/403', request)

      return NextResponse.next()
    }

    if (profile.statusCode !== 401) {
      return redirectTo('/500', request)
    }

    /* Access token invalid, try to refresh */
    if (refreshToken) {
      const refreshRes = await refreshAction(refreshToken)

      if (refreshRes.statusCode === 500) {
        return redirectTo('/500', request)
      }

      if (refreshRes.success && refreshRes.data) {
        const response = NextResponse.next()

        response.cookies.set(ACCESS_TOKEN_COOKIE_NAME, refreshRes.data.accessToken, {
          httpOnly: true,
          secure: true,
          path: '/',
          sameSite: 'lax'
        })

        response.cookies.set(REFRESH_TOKEN_COOKIE_NAME, refreshRes.data.refreshToken, {
          httpOnly: true,
          secure: true,
          path: '/',
          sameSite: 'lax'
        })

        return response
      }
    }
  }

  return redirectToLogin(request, pathname)
}

export const config = {
  matcher: [
    '/((?!_next/static|_next/image|favicon.ico|sitemap.xml|robots.txt|.*\\.(?:svg|png|jpg|jpeg|gif|webp|ico)$).*)'
  ]
}
