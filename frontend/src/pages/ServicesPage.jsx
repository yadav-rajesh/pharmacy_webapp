import PageHero from '../components/PageHero.jsx'
import ServiceIcon from '../components/ServiceIcon.jsx'
import usePageMeta from '../hooks/usePageMeta.js'
import { buildBreadcrumbSchema, buildWebPageSchema } from '../seo.js'
import { serviceCategories } from '../siteContent.js'

function ServicesPage() {
  const title = 'Products and Services | Medical Store Ichalkaranji'
  const description =
    'Explore prescription medicines, OTC medicines, supplements, baby care, personal care, diabetic care, and first aid products at Padmavati Medicals in Ichalkaranji.'

  usePageMeta({
    title,
    description,
    path: '/services',
    keywords:
      'Prescription medicines Ichalkaranji, OTC medicines Ichalkaranji, Health supplements Ichalkaranji, Medical store Ichalkaranji',
    schema: [
      buildWebPageSchema({
        title,
        description,
        path: '/services',
        type: 'CollectionPage',
      }),
      buildBreadcrumbSchema([
        { name: 'Home', path: '/' },
        { name: 'Products', path: '/services' },
      ]),
    ],
  })

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
