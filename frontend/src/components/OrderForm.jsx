import { useState } from 'react'

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
    setSubmitted(true)
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
        </label>
      </div>

      <button className="button-link is-primary submit-button" type="submit">
        Request Medicine
      </button>

      {submitted ? (
        <p className="success-message">
          Basic app note: this form currently demonstrates the user flow in the UI. Next we
          can connect it to WhatsApp, email, or a backend order API.
        </p>
      ) : null}
    </form>
  )
}

export default OrderForm
