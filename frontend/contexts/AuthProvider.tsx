'use client'

import { createContext, JSX, ReactNode, useCallback, useContext, useEffect, useState } from 'react'
import { getProfileAction } from '@/services/auth.service'
import { usePathname, useRouter } from 'next/navigation'
import { useQueryClient } from '@tanstack/react-query'
import LoadingScreen from '@/app/loading'
import { IProfile } from '@/types'

/* Auth Context Type */
interface AuthContextType {
  isAuthenticated: boolean
  profile: IProfile | null
  hasPermission: (permission: string[]) => boolean
  requirePermission: (permission: string[]) => void
  logIn: (data: IProfile) => void
  logOut: () => void
  refetch: () => Promise<void>
}

/* Create Auth Context */
const AuthContext = createContext<AuthContextType | undefined>(undefined)

/* Auth Provider Component */
export const AuthProvider = ({ children }: { children: ReactNode }): JSX.Element => {
  const queryClient = useQueryClient()
  const pathname = usePathname()
  const router = useRouter()

  const [profile, setProfile] = useState<IProfile | null>(null)
  const [isLoading, setIsLoading] = useState<boolean>(true)

  /* Fetch User Profile */
  const fetchProfile = useCallback(async () => {
    if (['/500', '/404', '/403'].includes(pathname)) {
      setIsLoading(false)
      return
    }

    setIsLoading(true)
    try {
      const res = await getProfileAction()

      if (res.success && res.data) {
        setProfile(res.data)
      } else {
        setProfile(null)
      }
    } catch (err) {
      console.error('Auto fetch profile failed:', err)
      setProfile(null)
    } finally {
      setIsLoading(false)
    }
  }, [pathname])

  /* Permission Check Function */
  const hasPermission = useCallback(
    (permissions: string[]): boolean => {
      if (!profile) return false

      if (profile?.permissions.includes('all:all')) return true

      return permissions.every(perm => profile.permissions.includes(perm))
    },
    [profile]
  )

  /* Require Permission Function */
  const requirePermission = useCallback(
    (permissions: string[]): boolean => {
      const ok = hasPermission(permissions)
      if (!ok) {
        router.replace('/403')
      }
      return ok
    },
    [hasPermission, router]
  )

  /* Login Function */
  const logIn = useCallback((data: IProfile) => {
    queryClient.clear()
    setProfile(data)
  }, [])

  /* Logout Function */
  const logOut = useCallback(() => {
    setProfile(null)
  }, [])

  useEffect(() => {
    fetchProfile()
  }, [fetchProfile])

  if (isLoading && !profile) {
    return <LoadingScreen />
  }

  /* Provide Auth Context to children */
  return (
    <AuthContext.Provider
      value={{
        profile,
        isAuthenticated: !!profile,
        requirePermission,
        hasPermission,
        logIn,
        logOut,
        refetch: fetchProfile
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

/* Custom Hook to use Auth Context */
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return context
}
