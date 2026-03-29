import ActionButtons from '../components/ActionButtons.jsx'
import ActionLink from '../components/ActionLink.jsx'
import SectionHeading from '../components/SectionHeading.jsx'
import usePageMeta from '../hooks/usePageMeta.js'
import { buildBreadcrumbSchema, buildWebPageSchema } from '../seo.js'
import {
  galleryCards,
  heroHighlights,
  pharmacy,
  quickStats,
  reviewHighlights,
  serviceAreas,
  trustBadges,
} from '../siteContent.js'

function HomePage() {
  const title = 'Trusted Pharmacy in Ichalkaranji | Padmavati Medicals'
  const description =
    'Trusted pharmacy in Ichalkaranji for genuine medicines, prescription support, health products, and family healthcare essentials.'

  usePageMeta({
    title,
    description,
    path: '/',
    schema: [
      buildWebPageSchema({ title, description, path: '/' }),
      buildBreadcrumbSchema([{ name: 'Home', path: '/' }]),
    ],
  })

  return (
    <main className="page">
      <section className="hero-section section">
        <div className="hero-grid">
          <div className="hero-copy">
            <p className="eyebrow">Pharmacy in Ichalkaranji</p>
            <h1>{pharmacy.headline}</h1>
            <p className="lead-text">{pharmacy.subheading}</p>
            <ActionButtons />
            <div className="highlight-list">
              {heroHighlights.map((item) => (
                <div className="highlight-item" key={item}>
                  <span className="highlight-mark">+</span>
                  <span>{item}</span>
                </div>
              ))}
            </div>
          </div>

          <aside className="hero-panel-card">
            <p className="eyebrow">Open Daily For Local Families</p>
            <h2>Medicines, daily health products, and helpful support</h2>
            <p>
              Padmavati Medicals supports nearby customers with prescription medicines,
              healthcare products, and quick contact paths for calls, WhatsApp orders,
              and store visits.
            </p>

            <div className="mini-stat-list">
              {trustBadges.map((badge) => (
                <div className="mini-stat" key={badge}>
                  <span className="mini-stat-dot" />
                  <p>{badge}</p>
                </div>
              ))}
            </div>
          </aside>
        </div>
      </section>

      <section className="section note-panel">
        <p>{pharmacy.launchNote}</p>
      </section>

      <section className="section">
        <SectionHeading
          description="Nearby customers usually want three things quickly: clear store timings, confidence in medicines, and a fast way to contact the pharmacy."
          eyebrow="Why Customers Choose Us"
          title="Local trust cues visitors can understand fast"
        />
        <div className="card-grid three-up">
          {quickStats.map((item) => (
            <article className="info-card" key={item.title}>
              <h3>{item.title}</h3>
              <p>{item.text}</p>
            </article>
          ))}
        </div>
      </section>

      <section className="section dual-grid">
        <div className="map-card">
          <SectionHeading
            description="A clear location, live directions, and a familiar landmark help turn local searches into walk-ins."
            eyebrow="Find The Store"
            title="Easy to reach from central Ichalkaranji"
          />
          <iframe
            allowFullScreen=""
            className="map-frame"
            loading="lazy"
            referrerPolicy="no-referrer-when-downgrade"
            src={pharmacy.mapEmbedUrl}
            title="Padmavati Medicals map"
          />
        </div>

        <div className="info-card stack-card">
          <h3>What nearby customers usually need</h3>
          <p>
            Families, senior citizens, and repeat-medicine customers want fast answers
            before they leave home. Clear contact options and a visible location make
            that easier.
          </p>
          <ul className="plain-list">
            <li>Prescription medicine enquiries before visiting</li>
            <li>Quick WhatsApp support for medicine availability</li>
            <li>Convenient walk-ins for daily healthcare essentials</li>
            <li>Friendly local service for repeat customers</li>
          </ul>
          <ActionButtons compact />
        </div>
      </section>

      <section className="section">
        <SectionHeading
          description="These trust cards use public listing proof so visitors can quickly verify the store before they call or visit."
          eyebrow="Reviews and Trust Signals"
          title="Public proof that supports local confidence"
        />
        <div className="card-grid three-up">
          {reviewHighlights.map((item) => (
            <article className="review-card" key={item.title}>
              <p className="review-tag">{item.eyebrow}</p>
              <h3>{item.title}</h3>
              <p className="review-text">{item.detail}</p>
              <ActionLink className="review-link" to={item.linkUrl}>
                {item.linkLabel}
              </ActionLink>
            </article>
          ))}
        </div>
      </section>

      <section className="section">
        <SectionHeading
          description="These visuals help communicate the storefront, customer support, and daily healthcare range while live directions stay one tap away."
          eyebrow="Store Visuals"
          title="A cleaner presentation of the pharmacy experience"
        />
        <div className="card-grid three-up">
          {galleryCards.map((item) => (
            <article className="gallery-card" key={item.title}>
              <img
                alt={item.alt}
                className="gallery-image"
                decoding="async"
                loading="lazy"
                src={item.image}
              />
              <h3>{item.title}</h3>
              <p>{item.text}</p>
              <ActionLink className="review-link" to={item.linkUrl}>
                {item.linkLabel}
              </ActionLink>
            </article>
          ))}
        </div>
      </section>

      <section className="section">
        <SectionHeading
          description="Nearby coverage language helps local SEO and also makes residents feel the store is close and familiar."
          eyebrow="Nearby Service Areas"
          title="Local service area cues"
        />
        <div className="pill-row">
          {serviceAreas.map((item) => (
            <span className="pill" key={item}>
              {item}
            </span>
          ))}
        </div>
      </section>

      <section className="section emergency-panel">
        <div>
          <p className="eyebrow">Emergency Contact</p>
          <h2>Need medicines urgently?</h2>
          <p>
            Call the store directly for urgent medicine support or use WhatsApp to ask
            about availability before you travel.
          </p>
        </div>
        <ActionButtons compact />
      </section>
    </main>
  )
}

export default HomePage
