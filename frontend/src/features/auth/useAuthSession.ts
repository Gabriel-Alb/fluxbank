import { useQuery } from '@tanstack/react-query'
import { getSession } from './authApi'
import { authKeys } from './authKeys'

export function useAuthSession() {
  return useQuery({
    queryKey: authKeys.session(),
    queryFn: getSession,
    retry: false,
    staleTime: 1000 * 60,
  })
}