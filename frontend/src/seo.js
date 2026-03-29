import { pharmacy } from './siteContent.js'

export const defaultKeywords =
  'Pharmacy in Ichalkaranji, Medical Store Ichalkaranji, Medicine Shop Near Me, Padmavati Medicals'

export const defaultSocialImage = '/og-storefront.svg'

export function getSiteUrl() {
  const configuredSiteUrl = import.meta.env.VITE_SITE_URL?.trim().replace(/\/+$/, '')

  if (configuredSiteUrl) {
    return configuredSiteUrl
  }

  if (typeof window !== 'undefined') {
    return window.location.origin
  }

  return ''
}

export function getAbsoluteUrl(path = '/') {
  const siteUrl = getSiteUrl()
  const normalizedPath = path.startsWith('/') ? path : `/${path}`

  if (!siteUrl) {
    return normalizedPath
  }

  return new URL(normalizedPath, `${siteUrl}/`).toString()
}

export function buildWebPageSchema({ description, path, title, type = 'WebPage' }) {
  return {
    '@context': 'https://schema.org',
    '@type': type,
    name: title,
    description,
    url: getAbsoluteUrl(path),
    isPartOf: {
      '@type': 'WebSite',
      name: pharmacy.name,
      url: getAbsoluteUrl('/'),
    },
  }
}

export function buildBreadcrumbSchema(items) {
  return {
    '@context': 'https://schema.org',
    '@type': 'BreadcrumbList',
    itemListElement: items.map((item, index) => ({
      '@type': 'ListItem',
      position: index + 1,
      name: item.name,
      item: getAbsoluteUrl(item.path),
    })),
  }
}
