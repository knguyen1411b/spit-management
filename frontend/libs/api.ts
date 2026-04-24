import { IBaseApiResponse, IDataApiResponse, IPageableApiResponse, IPageableMeta, ISearchParams } from '@/types'
import { ACCESS_TOKEN_COOKIE_NAME } from '@/constants/cookies'
import { cookies } from 'next/headers'
import { cache } from 'react'

type HttpMethod = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'

/**
 * Represents the normalized API response returned by request<T>().
 */
interface ApiResponse<T> {
  success: boolean
  statusCode: number
  message?: string
  data?: T
  meta?: IPageableMeta
}

/**
 * Additional configuration options for API requests.
 */
interface ApiOptions extends RequestInit {
  /** Automatically attach Authorization header from cookies */
  authenticated?: boolean
  /** Cache tags (Next.js fetch cache) */
  tags?: string[]
  /** Revalidate (Next.js fetch revalidation control) */
  revalidate?: number | false
  /** Cache strategy for fetch request */
  cache?: 'force-cache' | 'no-store' | 'reload'
}

const API_BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL!

/**
 * Get Authorization header from HttpOnly cookies.
 *
 * @description
 * - Cached using React's `cache()` to avoid re-reading cookies on every fetch.
 * - Reads the access token stored in NEXT cookies.
 */
const getAuthHeader = cache(async () => {
  const token = (await cookies()).get(ACCESS_TOKEN_COOKIE_NAME)?.value
  return token ? { Authorization: `Bearer ${token}` } : {}
})

/**
 * Build URL with query parameters.
 *
 * @param endpoint - Base API endpoint
 * @param params - Key-value map that will be appended as query params
 *
 * @description
 * Supports:
 * - primitive values
 * - array values (`?key[]=a&key[]=b`)
 * - skip null/undefined/empty values
 */
function buildUrl(endpoint: string, params?: Record<string, any>): string {
  if (!params) return endpoint

  const url = endpoint.startsWith('/') ? endpoint : `/${endpoint}`
  const searchParams = new URLSearchParams()

  Object.entries(params).forEach(([key, value]) => {
    if (value === undefined || value === null || value === '') return

    if (Array.isArray(value)) {
      value.forEach(v => searchParams.append(`${key}[]`, String(v)))
    } else {
      searchParams.append(key, String(value))
    }
  })

  const queryString = searchParams.toString()
  return queryString ? `${url}?${queryString}` : url
}

/**
 * Core HTTP request handler used by all api.<method>() calls.
 *
 * @template T - Expected response data type
 *
 * @param method - HTTP method ("GET" | "POST" | ...)
 * @param endpoint - Full API path (should already include prefix)
 * @param body - Optional JSON body for POST/PUT/PATCH
 * @param options - Additional fetch/Next.js settings
 *
 * @returns Promise<ApiResponse<T>>
 *
 * @description
 * Automatically:
 * - attaches Authorization header when authenticated
 * - handles Next.js caching/revalidate/tags
 * - normalizes API errors to consistent response format
 * - handles both JSON + text fallback
 */
async function request<T>(
  method: HttpMethod,
  endpoint: string,
  body?: any,
  options: ApiOptions = {}
): Promise<ApiResponse<T>> {
  const { authenticated = false, tags, revalidate, cache: cacheMode, headers: customHeaders, ...rest } = options

  const url = `${API_BASE_URL}${endpoint.startsWith('/') ? endpoint : `/${endpoint}`}`

  const authHeaders = authenticated ? await getAuthHeader() : {}

  const isFormData = body instanceof FormData

  const fetchOptions: RequestInit = {
    method,
    headers: Object.assign(!isFormData && { 'Content-Type': 'application/json' }, authHeaders, customHeaders),
    body: body === undefined ? undefined : isFormData ? body : JSON.stringify(body),
    next: tags || revalidate !== undefined ? { tags, revalidate } : undefined,
    cache: cacheMode,
    ...rest
  }

  try {
    const res = await fetch(url, fetchOptions)

    let payload: any
    try {
      payload = await res.json()
    } catch {
      payload = { message: await res.text() }
    }

    // Normalized success response
    if (payload?.success === true) {
      return {
        success: true,
        statusCode: res.status,
        message: payload.message,
        data: payload.data as T,
        meta: payload.meta as IPageableMeta | undefined
      }
    }

    // Normalized API error response (success: false)
    if (payload?.success === false) {
      return {
        success: false,
        statusCode: res.status,
        message: payload.message || 'Unknown API error',
        data: payload.data as T | undefined,
        meta: payload.meta as IPageableMeta | undefined
      }
    }

    // Fallback for unexpected server errors
    return {
      success: false,
      statusCode: res.status,
      message: payload?.message || res.statusText || 'Server Error',
      data: undefined
    }
  } catch (error: any) {
    console.error(`[API ${method} ${endpoint}]`, error)
    return {
      success: false,
      statusCode: 500,
      message: error?.message || 'Network failure or unreachable server',
      data: undefined
    }
  }
}

/**
 * Public API wrapper for performing HTTP requests.
 *
 * @description
 * Each method automatically forwards to `request<T>()`.
 */
export const api = {
  /** Perform GET request with query params */
  get: <T>(endpoint: string, params?: Record<string, any>, options?: ApiOptions) =>
    request<T>('GET', buildUrl(endpoint, params), undefined, options),

  /** Perform POST request */
  post: <T>(endpoint: string, body?: any, options?: ApiOptions) => request<T>('POST', endpoint, body, options),

  /** Perform PUT request */
  put: <T>(endpoint: string, body?: any, options?: ApiOptions) => request<T>('PUT', endpoint, body, options),

  /** Perform PATCH request */
  patch: <T>(endpoint: string, body?: any, options?: ApiOptions) => request<T>('PATCH', endpoint, body, options),

  /** Perform DELETE request */
  delete: <T>(endpoint: string, options?: ApiOptions) => request<T>('DELETE', endpoint, undefined, options)
}

/**
 * Factory for auto-generating server-side CRUD API functions.
 *
 * @template T - Type of resource object (e.g., IUser, IBoard)
 *
 * @param baseEndpoint - Base API endpoint for this resource (e.g. `/users`)
 *
 * @returns Object containing CRUD methods
 *
 * @description
 * Generated methods:
 * - list(params)
 * - detail(id)
 * - create(data)
 * - update(id, data)
 * - patch(id, data)
 * - delete(id)
 */
export const createServerApi = <T = any>(baseEndpoint: string) => ({
  /** List items with pagination */
  list: (params: ISearchParams = {}, options = { authenticated: true }): Promise<IPageableApiResponse<T>> =>
    api.get<T[]>(baseEndpoint, { ...params, page: params.page ? Number(params.page) - 1 : 0 }, options) as any,

  /** Retrieve a single item by ID */
  detail: (id: number | string): Promise<IDataApiResponse<T>> =>
    api.get<T>(`${baseEndpoint}/${id}`, {}, { authenticated: true }) as any,

  /** Create a new item */
  create: (data: Partial<T>): Promise<IBaseApiResponse> =>
    api.post<T>(baseEndpoint, data, { authenticated: true }) as any,

  /** Update entire item by ID */
  update: (id: number | string, data: Partial<T>): Promise<IBaseApiResponse> =>
    api.put<T>(`${baseEndpoint}/${id}`, data, { authenticated: true }) as any,

  put: (): Promise<IBaseApiResponse> => api.put<T>(baseEndpoint, {}, { authenticated: true }) as any,

  /** Partially update item by ID */
  patch: (id: number | string, data: Partial<T>): Promise<IBaseApiResponse> =>
    api.patch<T>(`${baseEndpoint}/${id}`, data, { authenticated: true }) as any,

  /** Delete item by ID */
  delete: (id: number | string): Promise<IBaseApiResponse> =>
    api.delete(`${baseEndpoint}/${id}`, { authenticated: true }) as any
})
