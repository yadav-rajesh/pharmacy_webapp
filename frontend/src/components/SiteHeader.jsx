import { Link, NavLink } from 'react-router-dom'
import ActionLink from './ActionLink.jsx'
import { navigation, pharmacy } from '../siteContent.js'

function SiteHeader() {
  return (
    <header className="site-header">
      <div className="top-banner">
        <p>{pharmacy.announcement}</p>
      </div>

      <div className="header-shell">
        <Link className="brand-lockup" to="/">
          <span className="brand-mark">PM</span>
          <span>
            <strong>{pharmacy.name}</strong>
            <small>Trusted Pharmacy in Ichalkaranji</small>
          </span>
        </Link>

        <nav aria-label="Primary" className="site-nav">
          {navigation.map((item) => (
            <NavLink
              className={({ isActive }) => (isActive ? 'nav-link is-active' : 'nav-link')}
              key={item.to}
              to={item.to}
            >
              {item.label}
            </NavLink>
          ))}
        </nav>

        <ActionLink className="button-link is-primary is-small header-button" to="/order">
          Request Medicine
        </ActionLink>
      </div>
    </header>
  )
}

export default SiteHeader
