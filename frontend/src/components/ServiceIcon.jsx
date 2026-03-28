function ServiceIcon({ name }) {
  switch (name) {
    case 'prescription':
      return (
        <svg aria-hidden="true" viewBox="0 0 48 48">
          <path d="M15 8h14l8 8v24H15z" fill="none" stroke="currentColor" strokeWidth="3" />
          <path d="M29 8v8h8" fill="none" stroke="currentColor" strokeWidth="3" />
          <path d="M24 22v10M19 27h10" fill="none" stroke="currentColor" strokeWidth="3" />
        </svg>
      )
    case 'otc':
      return (
        <svg aria-hidden="true" viewBox="0 0 48 48">
          <rect x="8" y="18" width="32" height="12" rx="6" fill="none" stroke="currentColor" strokeWidth="3" />
          <path d="M24 18v12" fill="none" stroke="currentColor" strokeWidth="3" />
        </svg>
      )
    case 'supplements':
      return (
        <svg aria-hidden="true" viewBox="0 0 48 48">
          <path d="M13 31c0-9 6-15 15-15h7c0 9-6 15-15 15z" fill="none" stroke="currentColor" strokeWidth="3" />
          <path d="M18 33c2-5 7-10 14-14" fill="none" stroke="currentColor" strokeWidth="3" />
        </svg>
      )
    case 'baby':
      return (
        <svg aria-hidden="true" viewBox="0 0 48 48">
          <path d="M18 10h12v8H18z" fill="none" stroke="currentColor" strokeWidth="3" />
          <path d="M16 18h16v18a4 4 0 0 1-4 4H20a4 4 0 0 1-4-4z" fill="none" stroke="currentColor" strokeWidth="3" />
          <path d="M20 24h8" fill="none" stroke="currentColor" strokeWidth="3" />
        </svg>
      )
    case 'personal':
      return (
        <svg aria-hidden="true" viewBox="0 0 48 48">
          <path d="M24 10c6 8 9 13 9 18a9 9 0 1 1-18 0c0-5 3-10 9-18z" fill="none" stroke="currentColor" strokeWidth="3" />
        </svg>
      )
    case 'diabetic':
      return (
        <svg aria-hidden="true" viewBox="0 0 48 48">
          <path d="M18 14h12v20H18z" fill="none" stroke="currentColor" strokeWidth="3" />
          <path d="M21 22h6M21 27h4" fill="none" stroke="currentColor" strokeWidth="3" />
          <path d="M34 18l4 4-4 4" fill="none" stroke="currentColor" strokeWidth="3" />
        </svg>
      )
    case 'firstaid':
      return (
        <svg aria-hidden="true" viewBox="0 0 48 48">
          <rect x="9" y="15" width="30" height="22" rx="4" fill="none" stroke="currentColor" strokeWidth="3" />
          <path d="M20 9h8v6h-8zM24 20v12M18 26h12" fill="none" stroke="currentColor" strokeWidth="3" />
        </svg>
      )
    default:
      return null
  }
}

export default ServiceIcon
