function PageHero({ eyebrow, title, description }) {
  return (
    <section className="page-hero section">
      <div className="page-hero-card">
        <p className="eyebrow">{eyebrow}</p>
        <h1>{title}</h1>
        <p>{description}</p>
      </div>
    </section>
  )
}

export default PageHero
