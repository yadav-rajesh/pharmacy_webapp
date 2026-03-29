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

function OrderForm() {
  const [formState, setFormState] = useState({
    name: '',
    phone: '',
    medicineName: '',
    address: '',
    prescriptionName: '',
  })
  const [submitted, setSubmitted] = useState(false)

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
    }))
  }

  function handleSubmit(event) {
    event.preventDefault()
    const whatsappUrl = buildWhatsAppOrderUrl(formState)
    setSubmitted(true)
    window.open(whatsappUrl, '_blank', 'noopener,noreferrer')
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

      <button className="button-link is-primary submit-button" type="submit">
        Request Medicine
      </button>

      {submitted ? (
        <p className="success-message">
          Your details were prepared for WhatsApp. If the chat did not open automatically,
          use the WhatsApp button on this page and send the same prescription details there.
        </p>
      ) : null}
    </form>
  )
}

export default OrderForm
