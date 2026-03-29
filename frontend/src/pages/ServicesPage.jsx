import PageHero from '../components/PageHero.jsx'
import ServiceIcon from '../components/ServiceIcon.jsx'
import usePageMeta from '../hooks/usePageMeta.js'
import { serviceCategories } from '../siteContent.js'

function ServicesPage() {
  usePageMeta(
    'Products and Services | Medical Store Ichalkaranji',
    'Explore prescription medicines, OTC medicines, supplements, baby care, personal care, diabetic care, and first aid products at Padmavati Medicals in Ichalkaranji.',
  )

  return (
    <main className="page">
      <PageHero
        description="Explore the main medicine and healthcare categories available for local families, senior citizens, and repeat customers."
        eyebrow="Products and Services"
        title="Medicine categories and healthcare essentials"
      />

      <section className="section">
        <div className="service-grid">
          {serviceCategories.map((item) => (
            <article className="service-card" key={item.title}>
              <div className="service-icon">
                <ServiceIcon name={item.icon} />
              </div>
              <h3>{item.title}</h3>
              <p>{item.text}</p>
            </article>
          ))}
        </div>
      </section>

      <section className="section note-panel">
        <p>
          Need to confirm stock before visiting? Use WhatsApp or call the store for
          quick medicine availability support.
        </p>
      </section>
    </main>
  )
}

export default ServicesPage
