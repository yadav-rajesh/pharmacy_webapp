import { Link } from 'react-router-dom'

function ActionLink({ to, children, className }) {
  const isInternal = to.startsWith('/')

  if (isInternal) {
    return (
      <Link className={className} to={to}>
        {children}
      </Link>
    )
  }

  return (
    <a className={className} href={to} rel="noreferrer" target="_blank">
      {children}
    </a>
  )
}

export default ActionLink
