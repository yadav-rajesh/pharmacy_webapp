import PageHero from '../components/PageHero.jsx'
import SectionHeading from '../components/SectionHeading.jsx'
import usePageMeta from '../hooks/usePageMeta.js'
import { aboutPoints, pharmacy } from '../siteContent.js'

function AboutPage() {
  usePageMeta(
    'About Padmavati Medicals | Trusted Pharmacy in Ichalkaranji',
    'Learn about Padmavati Medicals, a local pharmacy website foundation focused on genuine medicines, customer-friendly service, and reliable healthcare support in Ichalkaranji.',
  )

  return (
    <main className="page">
      <PageHero
        description="A clean About page helps local customers feel they are dealing with a dependable neighborhood pharmacy, not an anonymous medical store."
        eyebrow="About Us"
        title="Trusted local pharmacy support for Ichalkaranji"
      />

      <section className="section dual-grid">
        <article className="info-card">
          <h2>Why this page matters</h2>
          <p>
            About content is one of the strongest trust sections for a local medical store.
            It reassures customers about authenticity, service quality, and your local
            presence.
          </p>
        </article>

        <article className="info-card accent-card">
          <p className="eyebrow">Padmavati Medicals</p>
          <h2>Customer-first pharmacy positioning</h2>
          <p>{pharmacy.description}</p>
        </article>
      </section>

      <section className="section">
        <SectionHeading
          description="These statements already follow the direction you gave and can be refined into your final brand story later."
          eyebrow="Core Message"
          title="About content foundation"
        />
        <div className="card-grid two-up">
          {aboutPoints.map((item) => (
            <article className="info-card" key={item}>
              <p>{item}</p>
            </article>
          ))}
        </div>
      </section>
    </main>
  )
}

export default AboutPage
