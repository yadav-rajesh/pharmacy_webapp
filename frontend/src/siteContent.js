import counterVisual from './assets/gallery-pharmacist.svg'
import storeVisual from './assets/gallery-store.svg'
import wellnessVisual from './assets/gallery-wellness.svg'

export const pharmacy = {
  name: 'Padmavati Medicals',
  city: 'Ichalkaranji',
  state: 'Maharashtra',
  postalCode: '416115',
  headline: 'Trusted Pharmacy in Ichalkaranji - Padmavati Medicals',
  subheading:
    'Quality medicines, healthcare products, and trusted service for your family.',
  launchNote:
    'Call, WhatsApp ordering, directions, and local trust signals are live so nearby customers can take action quickly.',
  description:
    'Padmavati Medicals is a local pharmacy in Ichalkaranji focused on genuine medicines, customer-friendly guidance, and dependable support for families, senior citizens, and patients with regular medicine needs.',
  addressLine:
    'Tin Batti Chat Rasta Chowk, Chandur Road, Kagwade Mala, Ichalkaranji, Maharashtra 416115',
  phoneDisplay: '+91 97300 86267',
  whatsappNumber: '919730086267',
  phoneCta: 'tel:+919730086267',
  whatsappCta:
    'https://wa.me/919730086267?text=Hello%20Padmavati%20Medicals%2C%20I%20want%20to%20order%20medicines.',
  directionsUrl:
    'https://www.google.com/maps/dir/?api=1&destination=Tin%20Batti%20Chat%20Rasta%20Chowk%2C%20Chandur%20Road%2C%20Kagwade%20Mala%2C%20Ichalkaranji%2C%20Maharashtra%20416115',
  mapEmbedUrl:
    'https://www.google.com/maps?q=Tin%20Batti%20Chat%20Rasta%20Chowk%2C%20Chandur%20Road%2C%20Kagwade%20Mala%2C%20Ichalkaranji%2C%20Maharashtra%20416115&output=embed',
  mapsShareUrl: 'https://share.google/rXqUrAADNEmVeLZZI',
}

export const navigation = [
  { label: 'Home', to: '/' },
  { label: 'About Us', to: '/about' },
  { label: 'Products', to: '/services' },
  { label: 'Order Medicines', to: '/order' },
  { label: 'Contact', to: '/contact' },
]

export const heroHighlights = [
  'Genuine medicines',
  'Prescription medicines available',
  'Health products and supplements',
  'Friendly local service',
]

export const trustBadges = [
  'Genuine medicines',
  'Open daily',
  'WhatsApp orders available',
  'Friendly local service',
]

export const quickStats = [
  {
    title: 'Open daily',
    text: 'Morning to 10:45 PM for repeat medicines, family essentials, and convenient evening visits.',
  },
  {
    title: 'Prescription-ready support',
    text: 'Customers can call or WhatsApp to check medicine needs before visiting the store.',
  },
  {
    title: 'Easy local access',
    text: 'Located near Tin Batti Chat Rasta Chowk on Chandur Road for quick walk-ins and repeat pickups.',
  },
]

export const reviewHighlights = [
  {
    eyebrow: 'Latest public review',
    title: '"Best way to get your prescription"',
    detail: 'Shared by omkar chougule on Justdial on 21 Jan 2025.',
    linkLabel: 'View review source',
    linkUrl: 'https://www.justdial.com/Ichalkaranji/Chemists-in-Shirdhon/nct-10096237/page-4',
  },
  {
    eyebrow: 'Local rating signal',
    title: '4.4 rating on Justdial',
    detail: 'Padmavati Medicals appears with a 4.4 listing rating in public Justdial pharmacy results for Ichalkaranji.',
    linkLabel: 'Open listing',
    linkUrl: 'https://www.justdial.com/Ichalkaranji/Chemists-in-Vishwakarma-Nagar/nct-10096231',
  },
  {
    eyebrow: 'Quick verification',
    title: 'Check location, directions, and contact details',
    detail: 'Customers can verify the store location before calling, messaging on WhatsApp, or walking in.',
    linkLabel: 'Open Google Maps',
    linkUrl: pharmacy.mapsShareUrl,
  },
]

export const galleryCards = [
  {
    title: 'Storefront Visual',
    text: 'A clean storefront-style visual that supports recognition before customers arrive at the shop.',
    image: storeVisual,
    alt: 'Illustrated storefront visual for Padmavati Medicals',
    linkLabel: 'Get Directions',
    linkUrl: pharmacy.directionsUrl,
  },
  {
    title: 'Counter Support Visual',
    text: 'Shows the service-led feel of prescription guidance, medicine queries, and helpful local assistance.',
    image: counterVisual,
    alt: 'Illustrated pharmacy counter support visual for Padmavati Medicals',
    linkLabel: 'Call the Store',
    linkUrl: pharmacy.phoneCta,
  },
  {
    title: 'Wellness Shelves Visual',
    text: 'Highlights the wider product mix customers expect, from supplements to personal care and daily essentials.',
    image: wellnessVisual,
    alt: 'Illustrated wellness shelves visual for Padmavati Medicals',
    linkLabel: 'WhatsApp Order',
    linkUrl: pharmacy.whatsappCta,
  },
]

export const aboutPoints = [
  {
    title: 'Trusted local service',
    text: 'Serving Ichalkaranji with daily accessibility and a location that is easy for nearby families to reach.',
  },
  {
    title: 'Genuine medicine focus',
    text: 'Customers should feel confident that their pharmacy support starts with dependable medicines and clear guidance.',
  },
  {
    title: 'Friendly customer care',
    text: 'Families, senior citizens, and regular medicine buyers benefit from simple, respectful, and helpful service.',
  },
  {
    title: 'Reliable healthcare support',
    text: 'Phone, WhatsApp, and walk-in assistance make it easier to handle routine purchases and urgent medicine needs.',
  },
]

export const serviceCategories = [
  {
    icon: 'prescription',
    title: 'Prescription Medicines',
    text: 'Support for doctor-prescribed medicines with a customer-friendly local pharmacy experience.',
  },
  {
    icon: 'otc',
    title: 'OTC Medicines',
    text: 'Day-to-day medicine options for common cold, fever, digestive issues, and general wellness needs.',
  },
  {
    icon: 'supplements',
    title: 'Health Supplements',
    text: 'Vitamins, minerals, immunity support, protein, and wellness products for daily health routines.',
  },
  {
    icon: 'baby',
    title: 'Baby Care Products',
    text: 'Basic baby health and care essentials for families looking for trusted local availability.',
  },
  {
    icon: 'personal',
    title: 'Personal Care Products',
    text: 'Skin care, hygiene, grooming, and daily-use products arranged in a clean medical retail flow.',
  },
  {
    icon: 'diabetic',
    title: 'Diabetic Care Products',
    text: 'Products for regular diabetic management and repeat purchase support for ongoing care.',
  },
  {
    icon: 'firstaid',
    title: 'First Aid and Medical Supplies',
    text: 'Bandages, antiseptic items, home-care essentials, and practical medical supplies for emergencies.',
  },
]

export const orderBenefits = [
  'Medicine availability inquiry ready',
  'Prescription upload field included',
  'Address capture for future delivery flow',
  'Form submission now opens a WhatsApp order message',
]

export const hours = [
  { label: 'Open Daily', value: 'Morning to 10:45 PM' },
]

export const serviceAreas = [
  'Kagwade Mala',
  'Tin Batti Chowk',
  'Chandur Road',
  'Central Ichalkaranji',
  'Nearby residential areas',
]

export const contactCards = [
  {
    title: 'Address',
    value: 'Tin Batti Chat Rasta Chowk, Chandur Road',
    detail: 'Kagwade Mala, Ichalkaranji, Maharashtra 416115',
  },
  {
    title: 'Phone',
    value: '+91 97300 86267',
    detail: 'Call directly or use WhatsApp for fast medicine enquiries and order requests.',
  },
  {
    title: 'Business Hours',
    value: 'Open daily - Morning to 10:45 PM',
    detail: 'Convenient for regular medicine purchases, local walk-ins, and urgent support.',
  },
]
