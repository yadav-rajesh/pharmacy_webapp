import { useEffect } from 'react'
import {
  defaultKeywords,
  defaultSocialImage,
  getAbsoluteUrl,
} from '../seo.js'

function upsertMeta(selector, attributes) {
  let element = document.head.querySelector(selector)

  if (!element) {
    element = document.createElement('meta')
    document.head.appendChild(element)
  }

  Object.entries(attributes).forEach(([key, value]) => {
    element.setAttribute(key, value)
  })
}

function upsertLink(rel, href) {
  let element = document.head.querySelector(`link[rel="${rel}"]`)

  if (!element) {
    element = document.createElement('link')
    element.setAttribute('rel', rel)
    document.head.appendChild(element)
  }

  element.setAttribute('href', href)
}

function usePageMeta({
  description,
  image = defaultSocialImage,
  keywords = defaultKeywords,
  path = '/',
  robots = 'index, follow',
  schema = [],
  title,
  type = 'website',
}) {
  const pageUrl = getAbsoluteUrl(path)
  const imageUrl = image.startsWith('http') ? image : getAbsoluteUrl(image)
  const schemaMarkup = schema.length ? JSON.stringify(schema) : ''

  useEffect(() => {
    document.title = title
    document.documentElement.lang = 'en-IN'

    upsertMeta('meta[name="description"]', {
      name: 'description',
      content: description,
    })
    upsertMeta('meta[name="keywords"]', {
      name: 'keywords',
      content: keywords,
    })
    upsertMeta('meta[name="robots"]', {
      name: 'robots',
      content: robots,
    })
    upsertMeta('meta[property="og:title"]', {
      property: 'og:title',
      content: title,
    })
    upsertMeta('meta[property="og:description"]', {
      property: 'og:description',
      content: description,
    })
    upsertMeta('meta[property="og:type"]', {
      property: 'og:type',
      content: type,
    })
    upsertMeta('meta[property="og:url"]', {
      property: 'og:url',
      content: pageUrl,
    })
    upsertMeta('meta[property="og:image"]', {
      property: 'og:image',
      content: imageUrl,
    })
    upsertMeta('meta[property="og:site_name"]', {
      property: 'og:site_name',
      content: 'Padmavati Medicals',
    })
    upsertMeta('meta[name="twitter:card"]', {
      name: 'twitter:card',
      content: 'summary_large_image',
    })
    upsertMeta('meta[name="twitter:title"]', {
      name: 'twitter:title',
      content: title,
    })
    upsertMeta('meta[name="twitter:description"]', {
      name: 'twitter:description',
      content: description,
    })
    upsertMeta('meta[name="twitter:image"]', {
      name: 'twitter:image',
      content: imageUrl,
    })
    upsertLink('canonical', pageUrl)

    const existingSchema = document.head.querySelector('script[data-page-schema="true"]')

    if (schemaMarkup) {
      const schemaScript = existingSchema ?? document.createElement('script')

      schemaScript.setAttribute('type', 'application/ld+json')
      schemaScript.setAttribute('data-page-schema', 'true')
      schemaScript.textContent = schemaMarkup

      if (!existingSchema) {
        document.head.appendChild(schemaScript)
      }
    } else if (existingSchema) {
      existingSchema.remove()
    }
  }, [description, imageUrl, keywords, pageUrl, robots, schemaMarkup, title, type])
}

export default usePageMeta
