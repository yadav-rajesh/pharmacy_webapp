import ActionButtons from '../components/ActionButtons.jsx'
import SectionHeading from '../components/SectionHeading.jsx'
import usePageMeta from '../hooks/usePageMeta.js'
import {
  galleryCards,
  heroHighlights,
  pharmacy,
  quickStats,
  serviceAreas,
  testimonials,
  trustBadges,
} from '../siteContent.js'

function HomePage() {
  usePageMeta(
    'Pharmacy in Ichalkaranji | Padmavati Medicals',
    'Trusted pharmacy in Ichalkaranji for genuine medicines, prescription support, health products, and family healthcare essentials.',
  )

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
            <p className="eyebrow">Built For Local Conversion</p>
            <h2>Phone calls, WhatsApp orders, and walk-ins</h2>
            <p>
              This basic version already places trust, nearby relevance, and clear action
              buttons at the center of the experience.
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
          description="A clean first version of the pharmacy website should quickly answer what you offer, why local families can trust you, and how they should contact you."
          eyebrow="Trust Builders"
          title="Core conversion features are already mapped"
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
            description="Maps help nearby visitors move from online search to in-store visit, especially when they need medicines quickly."
            eyebrow="Find The Store"
            title="Google Maps section ready"
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
          <h3>Quick local reassurance</h3>
          <p>
            This home page foundation is aimed at families, senior citizens, and
            regular-medicine patients who want fast confidence before calling or visiting.
          </p>
          <ul className="plain-list">
            <li>Prescription support visibility</li>
            <li>Medical and wellness product discovery</li>
            <li>Friendly local-service positioning</li>
            <li>Strong mobile-first call-to-action placement</li>
          </ul>
          <ActionButtons compact />
        </div>
      </section>

      <section className="section">
        <SectionHeading
          description="The layout is ready for real social proof. For now, these cards mark where verified customer feedback should go."
          eyebrow="Testimonials"
          title="Customer review section scaffold"
        />
        <div className="card-grid three-up">
          {testimonials.map((item) => (
            <article className="review-card" key={item.audience}>
              <p className="review-tag">{item.audience}</p>
              <p className="review-text">{item.quote}</p>
            </article>
          ))}
        </div>
      </section>

      <section className="section">
        <SectionHeading
          description="The image gallery is in place so we can replace these cards with real pharmacy photos in the next pass."
          eyebrow="Pharmacy Images"
          title="Store photo section ready"
        />
        <div className="card-grid three-up">
          {galleryCards.map((item) => (
            <article className="gallery-card" key={item.title}>
              <div className="gallery-art" />
              <h3>{item.title}</h3>
              <p>{item.text}</p>
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
            This section is ready for an emergency call or quick-response medicine inquiry
            flow once your live number is added.
          </p>
        </div>
        <ActionButtons compact />
      </section>
    </main>
  )
}

export default HomePage
