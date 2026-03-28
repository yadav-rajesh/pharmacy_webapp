import ActionLink from './ActionLink.jsx'
import { pharmacy } from '../siteContent.js'

function ActionButtons({ compact = false }) {
  return (
    <div className={`cta-group${compact ? ' is-compact' : ''}`}>
      <ActionLink className="button-link is-primary" to={pharmacy.phoneCta}>
        Call Now
      </ActionLink>
      <ActionLink className="button-link is-secondary" to={pharmacy.whatsappCta}>
        WhatsApp Order
      </ActionLink>
      <ActionLink className="button-link is-ghost" to={pharmacy.directionsUrl}>
        Get Directions
      </ActionLink>
    </div>
  )
}

export default ActionButtons
