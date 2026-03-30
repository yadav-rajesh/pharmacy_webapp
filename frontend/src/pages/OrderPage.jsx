import ActionLink from '../components/ActionLink.jsx'
import OrderForm from '../components/OrderForm.jsx'
import PageHero from '../components/PageHero.jsx'
import SectionHeading from '../components/SectionHeading.jsx'
import usePageMeta from '../hooks/usePageMeta.js'
import { buildBreadcrumbSchema, buildWebPageSchema } from '../seo.js'
import { orderBenefits, pharmacy } from '../siteContent.js'

function OrderPage() {
  const title = 'Order Medicines | Padmavati Medicals Ichalkaranji'
  const description =
    'Request medicines online from Padmavati Medicals with a simple enquiry flow for name, phone number, prescription, medicine name, and address.'

  usePageMeta({
    title,
    description,
    path: '/order',
    keywords:
      'Order medicines Ichalkaranji, WhatsApp medicine order Ichalkaranji, Prescription medicine enquiry Ichalkaranji, Padmavati Medicals order',
    schema: [
      buildWebPageSchema({ title, description, path: '/order' }),
      buildBreadcrumbSchema([
        { name: 'Home', path: '/' },
        { name: 'Order Medicines', path: '/order' },
      ]),
    ],
  })

  return (
    <main className="page">
      <PageHero
        description="Use the form for a structured medicine request or tap WhatsApp now for faster direct ordering and stock confirmation."
        eyebrow="Order Medicines"
        title="Medicine request form with live WhatsApp ordering"
      />

      <section className="section order-layout">
        <div className="form-card">
          <SectionHeading
            description="When the form is submitted, it opens WhatsApp with the medicine request details and also tries to save the request through the backend API."
            eyebrow="Request Medicine"
            title="Order form"
          />
          <OrderForm />
        </div>

        <div className="side-panel-stack">
          <article className="info-card" id="whatsapp-order">
            <p className="eyebrow">WhatsApp Order</p>
            <h3>WhatsApp ordering is live</h3>
            <p>
              Send your medicine list or prescription details directly on WhatsApp for faster
              support from the store.
            </p>
            <ActionLink className="button-link is-secondary" to={pharmacy.whatsappCta}>
              Start WhatsApp Order
            </ActionLink>
          </article>

          <article className="info-card">
            <p className="eyebrow">Medicine Availability Inquiry</p>
            <h3>Why customers use this form</h3>
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
