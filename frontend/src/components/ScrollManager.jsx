import { useEffect } from 'react'
import { useLocation } from 'react-router-dom'

function ScrollManager() {
  const location = useLocation()

  useEffect(() => {
    if (location.hash) {
      requestAnimationFrame(() => {
        const target = document.querySelector(location.hash)

        if (target) {
          target.scrollIntoView({ behavior: 'smooth', block: 'start' })
          return
        }

        window.scrollTo({ top: 0, left: 0 })
      })
      return
    }

    window.scrollTo({ top: 0, left: 0 })
  }, [location])

  return null
}

export default ScrollManager
