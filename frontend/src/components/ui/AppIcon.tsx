import { Icon } from '@mdi/react'

interface AppIconProps {
  path: string
  size?: number | string
  color?: string
  className?: string
  title?: string
  spin?: boolean
}

export function AppIcon({ path, size = 1, color, className, title, spin }: AppIconProps) {
  return (
    <Icon
      path={path}
      size={size}
      color={color}
      className={className}
      title={title}
      spin={spin}
    />
  )
}