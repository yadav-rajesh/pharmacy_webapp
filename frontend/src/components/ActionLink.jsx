import { Link } from 'react-router-dom'

function ActionLink({ to, children, className }) {
  const isInternal = to.startsWith('/')
  const isDirectAction =
    to.startsWith('tel:') || to.startsWith('mailto:') || to.startsWith('sms:')

  if (isInternal) {
    return (
      <Link className={className} to={to}>
        {children}
      </Link>
    )
  }

  if (isDirectAction) {
    return (
      <a className={className} href={to}>
        {children}
      </a>
    )
  }

  return (
    <a className={className} href={to} rel="noreferrer" target="_blank">
      {children}
    </a>
  )
}

export default ActionLink
