import { createSearchParamsCache, createSerializer, parseAsInteger, parseAsString } from 'nuqs/server'

/**
 * Configuration map describing how each query parameter
 * should be parsed and validated when used with nuqs/server.
 *
 * @property page    - Integer page index, defaults to 1
 * @property size    - Integer page size, defaults to 10
 * @property sort    - Optional string sort expression
 * @property keyword - Optional string search keyword
 *
 * @description
 * This schema is used to:
 * - parse incoming URL query parameters
 * - normalize values
 * - provide type-safe search param handling
 */
export const searchParams = {
  page: parseAsInteger.withDefault(1),
  size: parseAsInteger.withDefault(10),
  sort: parseAsString,
  keyword: parseAsString
}

/**
 * Server-side cache for parsed search params.
 *
 * @description
 * - nuqs will parse the query parameters using `searchParams`
 * - then cache the normalized result in memory
 * - prevents re-parsing on each server component load
 */
export const searchParamsCache = createSearchParamsCache(searchParams)

/**
 * Serializer used to convert normalized search params
 * back into a URL query string.
 *
 * @description
 * Useful when generating links/pagination URLs like:
 *   `router.push(serialize({ page: 2, keyword: "abc" }))`
 */
export const serialize = createSerializer(searchParams)
