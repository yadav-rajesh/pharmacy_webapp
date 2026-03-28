import ActionLink from '../components/ActionLink.jsx'
import OrderForm from '../components/OrderForm.jsx'
import PageHero from '../components/PageHero.jsx'
import SectionHeading from '../components/SectionHeading.jsx'
import usePageMeta from '../hooks/usePageMeta.js'
import { orderBenefits } from '../siteContent.js'

function OrderPage() {
  usePageMeta(
    'Order Medicines | Padmavati Medicals Ichalkaranji',
    'Request medicines online from Padmavati Medicals with a simple enquiry flow for name, phone number, prescription, medicine name, and address.',
  )

  return (
    <main className="page">
      <PageHero
        description="This first version gives you the exact medicine request flow we can later connect to WhatsApp orders or a proper backend order process."
        eyebrow="Order Medicines"
        title="Medicine request form with WhatsApp-ready flow"
      />

      <section className="section order-layout">
        <div className="form-card">
          <SectionHeading
            description="Simple fields, mobile-friendly layout, and clear action language make this a strong starting point for conversions."
            eyebrow="Request Medicine"
            title="Order form"
          />
          <OrderForm />
        </div>

        <div className="side-panel-stack">
          <article className="info-card" id="whatsapp-order">
            <p className="eyebrow">WhatsApp Order</p>
            <h3>WhatsApp ordering section is ready</h3>
            <p>
              In the next step, we can connect this flow directly to your pharmacy WhatsApp
              number with pre-filled medicine enquiry text.
            </p>
            <ActionLink className="button-link is-secondary" to="/contact#quick-contact">
              Connect WhatsApp Details Next
            </ActionLink>
          </article>

          <article className="info-card">
            <p className="eyebrow">Medicine Availability Inquiry</p>
            <h3>High-intent enquiry prompts</h3>
            <ul className="plain-list">
              {orderBenefits.map((item) => (
                <li key={item}>{item}</li>
              ))}
            </ul>
          </article>
        </div>
      </section>
    </main>
  )
}

export default OrderPage
