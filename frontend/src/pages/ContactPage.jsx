import ActionButtons from '../components/ActionButtons.jsx'
import PageHero from '../components/PageHero.jsx'
import SectionHeading from '../components/SectionHeading.jsx'
import usePageMeta from '../hooks/usePageMeta.js'
import { contactCards, hours, pharmacy, serviceAreas } from '../siteContent.js'

function ContactPage() {
  usePageMeta(
    'Contact Padmavati Medicals | Pharmacy Near Me in Ichalkaranji',
    'Contact Padmavati Medicals for medicines, pharmacy directions, local support, and healthcare product enquiries in Ichalkaranji.',
  )

  return (
    <main className="page">
      <PageHero
        description="Whether you want to call, place a WhatsApp order, get directions, or visit the medical store directly, every contact path is kept simple and immediate."
        eyebrow="Contact"
        title="Reach Padmavati Medicals in the way that suits you best"
      />

      <section className="section dual-grid" id="quick-contact">
        <div className="contact-stack">
          {contactCards.map((item) => (
            <article className="info-card" key={item.title}>
              <p className="eyebrow">{item.title}</p>
              <h3>{item.value}</h3>
              <p>{item.detail}</p>
            </article>
          ))}

          <article className="info-card">
            <p className="eyebrow">Business Hours</p>
            <div className="hours-list">
              {hours.map((item) => (
                <div className="hours-row" key={item.label}>
                  <span>{item.label}</span>
                  <strong>{item.value}</strong>
                </div>
              ))}
            </div>
            <ActionButtons compact />
          </article>
        </div>

        <div className="map-card">
          <SectionHeading
            description="Use the live details below to call, message on WhatsApp, or visit the store directly."
            eyebrow="Map and Directions"
            title="Visit the store with quick directions"
          />
          <iframe
            allowFullScreen=""
            className="map-frame"
            loading="lazy"
            referrerPolicy="no-referrer-when-downgrade"
            src={pharmacy.mapEmbedUrl}
            title="Padmavati Medicals contact map"
          />
          <p className="map-note">
            Store address: {pharmacy.addressLine}
          </p>
        </div>
      </section>

      <section className="section">
        <SectionHeading
          description="Padmavati Medicals is positioned for customers around central Ichalkaranji who want a nearby and dependable pharmacy."
          eyebrow="Service Areas"
          title="Serving local residents around central Ichalkaranji"
        />
        <div className="pill-row">
          {serviceAreas.map((item) => (
            <span className="pill" key={item}>
              {item}
            </span>
          ))}
        </div>
      </section>
    </main>
  )
}

export default ContactPage
