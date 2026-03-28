import ActionLink from './ActionLink.jsx'
import { pharmacy } from '../siteContent.js'

function FloatingActions() {
  return (
    <div className="floating-actions">
      <ActionLink className="floating-action is-call" to={pharmacy.phoneCta}>
        Call
      </ActionLink>
      <ActionLink className="floating-action is-whatsapp" to={pharmacy.whatsappCta}>
        WhatsApp
      </ActionLink>
    </div>
  )
}

export default FloatingActions
