import { useState } from 'react'
import { pharmacy } from '../siteContent.js'

function getContactInquiriesUrl() {
  const apiBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim().replace(/\/+$/, '') ?? ''

  return `${apiBaseUrl}/api/contact-inquiries`
}

async function storeContactInquiry(formState) {
  const response = await fetch(getContactInquiriesUrl(), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      name: formState.name,
      phone: formState.phone,
      message: formState.message,
    }),
  })
  const contentType = response.headers.get('content-type') ?? ''

  if (!contentType.includes('application/json')) {
    throw new Error('Contact inquiry API is not available right now.')
  }

  const payload = await response.json()

  if (!response.ok) {
    throw new Error(payload.message ?? 'Contact inquiry could not be sent right now.')
  }

  return payload
}

function ContactInquiryForm() {
  const [formState, setFormState] = useState({
    name: '',
    phone: '',
    message: '',
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

  async function handleSubmit(event) {
    event.preventDefault()

    setIsSubmitting(true)
    setSubmissionState(null)

    try {
      const result = await storeContactInquiry(formState)

      setFormState({
        name: '',
        phone: '',
        message: '',
      })
      setSubmissionState({
        tone: 'success',
        message: `Your inquiry was saved with ID ${result.inquiryId}. The store can now follow up directly on ${pharmacy.phoneDisplay}.`,
      })
    } catch (error) {
      setSubmissionState({
        tone: 'error',
        message: `${error.message} If it is urgent, please call or WhatsApp the store directly.`,
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

        <label className="form-field-full">
          <span>Message</span>
          <textarea
            name="message"
            onChange={handleChange}
            placeholder="Tell the store what you need help with"
            required
            rows="5"
            value={formState.message}
          />
        </label>
      </div>

      <button className="button-link is-primary submit-button" disabled={isSubmitting} type="submit">
        {isSubmitting ? 'Sending Inquiry...' : 'Send Inquiry'}
      </button>

      {submissionState ? (
        <p
          className={`success-message${submissionState.tone === 'error' ? ' is-error' : ''}`}
        >
          {submissionState.message}
        </p>
      ) : null}
    </form>
  )
}

export default ContactInquiryForm
