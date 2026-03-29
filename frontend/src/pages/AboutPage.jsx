import PageHero from '../components/PageHero.jsx'
import SectionHeading from '../components/SectionHeading.jsx'
import usePageMeta from '../hooks/usePageMeta.js'
import { buildBreadcrumbSchema, buildWebPageSchema } from '../seo.js'
import { aboutPoints, pharmacy } from '../siteContent.js'

function AboutPage() {
  const title = 'About Padmavati Medicals | Trusted Pharmacy in Ichalkaranji'
  const description =
    'Learn about Padmavati Medicals, a trusted local pharmacy in Ichalkaranji focused on genuine medicines, friendly service, and reliable healthcare support.'

  usePageMeta({
    title,
    description,
    path: '/about',
    keywords:
      'About Padmavati Medicals, Pharmacy in Ichalkaranji, Trusted medical store in Ichalkaranji, Genuine medicines Ichalkaranji',
    schema: [
      buildWebPageSchema({
        title,
        description,
        path: '/about',
        type: 'AboutPage',
      }),
      buildBreadcrumbSchema([
        { name: 'Home', path: '/' },
        { name: 'About Us', path: '/about' },
      ]),
    ],
  })

  return (
    <main className="page">
      <PageHero
        description="Padmavati Medicals is built around genuine medicines, customer-friendly service, and dependable local healthcare support for families, senior citizens, and patients with regular medicine needs."
        eyebrow="About Us"
        title="Trusted pharmacy support for Ichalkaranji families"
      />

      <section className="section dual-grid">
        <article className="info-card">
          <p className="eyebrow">Our Focus</p>
          <h2>A neighborhood medical store customers can trust</h2>
          <p>
            Padmavati Medicals serves Ichalkaranji with a clear promise: provide quality
            medicines, useful healthcare essentials, and a service experience that makes
            every interaction straightforward.
          </p>
          <ul className="plain-list">
            <li>Trusted local pharmacy service in Ichalkaranji</li>
            <li>Focus on genuine medicines and confident purchases</li>
            <li>Helpful support for families, seniors, and repeat buyers</li>
            <li>Easy contact by phone, WhatsApp, and walk-in visits</li>
          </ul>
        </article>

        <article className="info-card accent-card">
          <p className="eyebrow">Padmavati Medicals</p>
          <h2>Simple, professional, and helpful every day</h2>
          <p>{pharmacy.description}</p>
          <p>
            A strong local pharmacy should feel both professional and approachable.
            That is the experience this store aims to offer for quick walk-ins, repeat
            prescriptions, wellness needs, and urgent family purchases.
          </p>
        </article>
      </section>

      <section className="section">
        <SectionHeading
          description="These are the strengths that help a nearby medical store earn repeat trust over time."
          eyebrow="Why Customers Return"
          title="Core strengths that build long-term confidence"
        />
        <div className="card-grid two-up">
          {aboutPoints.map((item) => (
            <article className="info-card" key={item.title}>
              <h3>{item.title}</h3>
              <p>{item.text}</p>
            </article>
          ))}
        </div>
      </section>
    </main>
  )
}

export default AboutPage
