import { useState } from 'react'
import { pharmacy } from '../siteContent.js'

function buildWhatsAppOrderUrl(formState) {
  const messageLines = [
    `Hello ${pharmacy.name}, I want to order medicines.`,
    '',
    `Name: ${formState.name}`,
    `Phone: ${formState.phone}`,
    `Medicine Name: ${formState.medicineName}`,
    `Address: ${formState.address}`,
    `Prescription: ${formState.prescriptionName || 'Will share on WhatsApp'}`,
    '',
    'Please confirm availability.',
  ]

  return `https://wa.me/${pharmacy.whatsappNumber}?text=${encodeURIComponent(
    messageLines.join('\n'),
  )}`
}

function getMedicineRequestsUrl() {
  const apiBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim().replace(/\/+$/, '') ?? ''

  return `${apiBaseUrl}/api/medicine-requests`
}

async function storeMedicineRequest(formState) {
  const formData = new FormData()

  formData.append('name', formState.name)
  formData.append('phone', formState.phone)
  formData.append('medicineName', formState.medicineName)
  formData.append('address', formState.address)

  if (formState.prescriptionFile) {
    formData.append('prescription', formState.prescriptionFile)
  }

  const response = await fetch(getMedicineRequestsUrl(), {
    method: 'POST',
    body: formData,
  })
  const contentType = response.headers.get('content-type') ?? ''

  if (!contentType.includes('application/json')) {
    throw new Error('Medicine request API is not available right now.')
  }

  const payload = await response.json()

  if (!response.ok) {
    throw new Error(payload.message ?? 'Medicine request could not be stored right now.')
  }

  return payload
}

function OrderForm() {
  const [formState, setFormState] = useState({
    name: '',
    phone: '',
    medicineName: '',
    address: '',
    prescriptionName: '',
    prescriptionFile: null,
  })
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [submissionState, setSubmissionState] = useState(null)

  function handleChange(event) {
    const { name, value } = event.target

    setFormState((current) => ({
      ...current,
      [name]: value,
    }))
  }

  function handleFileChange(event) {
    const selectedFile = event.target.files?.[0]

    setFormState((current) => ({
      ...current,
      prescriptionName: selectedFile ? selectedFile.name : '',
      prescriptionFile: selectedFile ?? null,
    }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    const whatsappUrl = buildWhatsAppOrderUrl(formState)

    setIsSubmitting(true)
    setSubmissionState(null)
    window.open(whatsappUrl, '_blank', 'noopener,noreferrer')

    try {
      const result = await storeMedicineRequest(formState)

      setSubmissionState({
        tone: 'success',
        message: `Your request was saved with ID ${result.requestId}. WhatsApp opened with the same details for quick confirmation.`,
      })
    } catch (error) {
      setSubmissionState({
        tone: 'error',
        message: `${error.message} WhatsApp opened so you can still send the request directly.`,
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <form className="order-form" onSubmit={handleSubmit}>
      <div className="form-grid">
        <label>
          <span>Name</span>
          <input
            name="name"
            onChange={handleChange}
            placeholder="Your name"
            required
            type="text"
            value={formState.name}
          />
        </label>

        <label>
          <span>Phone Number</span>
          <input
            name="phone"
            onChange={handleChange}
            placeholder="Your phone number"
            required
            type="tel"
            value={formState.phone}
          />
        </label>

        <label>
          <span>Medicine Name</span>
          <input
            name="medicineName"
            onChange={handleChange}
            placeholder="Required medicine"
            required
            type="text"
            value={formState.medicineName}
          />
        </label>

        <label>
          <span>Address</span>
          <textarea
            name="address"
            onChange={handleChange}
            placeholder="Delivery or pickup address"
            required
            rows="4"
            value={formState.address}
          />
        </label>

        <label>
          <span>Upload Prescription</span>
          <input accept=".jpg,.jpeg,.png,.pdf" onChange={handleFileChange} type="file" />
          <small>{formState.prescriptionName || 'No file selected yet'}</small>
          <small>After WhatsApp opens, attach the prescription file in the chat if needed.</small>
        </label>
      </div>

      <button className="button-link is-primary submit-button" disabled={isSubmitting} type="submit">
        {isSubmitting ? 'Submitting Request...' : 'Request Medicine'}
      </button>

      {submissionState ? (
        <p
          className={`success-message${submissionState.tone === 'error' ? ' is-error' : ''}`}
        >
          {submissionState.message} If the chat did not open automatically, use the
          WhatsApp button on this page and send the same prescription details there.
        </p>
      ) : null}
    </form>
  )
}

export default OrderForm
