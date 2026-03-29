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
        description="The contact page is designed to remove hesitation and guide people toward a call, WhatsApp enquiry, or store visit."
        eyebrow="Contact"
        title="Contact Padmavati Medicals"
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
            title="Location visibility"
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
          description="This section helps tell nearby residents that your pharmacy is relevant to their area and accessible for quick support."
          eyebrow="Service Areas"
          title="Nearby customers we want to attract"
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
