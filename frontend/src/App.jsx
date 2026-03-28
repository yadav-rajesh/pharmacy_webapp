import { useEffect, useState } from 'react'
import './App.css'

async function requestHealth(signal) {
  const response = await fetch('/api/health', { signal })

  if (!response.ok) {
    throw new Error(`Backend returned ${response.status}`)
  }

  return response.json()
}

function App() {
  const [health, setHealth] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const controller = new AbortController()

    async function loadHealth() {
      try {
        setLoading(true)
        setError('')
        const payload = await requestHealth(controller.signal)
        setHealth(payload)
      } catch (requestError) {
        if (requestError.name !== 'AbortError') {
          setError(
            'Could not reach the Spring Boot API. Start the backend on http://localhost:8080 and try again.',
          )
        }
      } finally {
        setLoading(false)
      }
    }

    loadHealth()

    return () => controller.abort()
  }, [])

  async function handleRefresh() {
    try {
      setLoading(true)
      setError('')
      const payload = await requestHealth()
      setHealth(payload)
    } catch (requestError) {
      setError(
        requestError.message ||
          'Could not refresh backend status. Make sure the API is running.',
      )
    } finally {
      setLoading(false)
    }
  }

  const formattedTimestamp = health?.timestamp
    ? new Date(health.timestamp).toLocaleString()
    : 'Pending'

  let statusLabel = 'checking'
  let statusClass = 'is-checking'

  if (error) {
    statusLabel = 'offline'
    statusClass = 'is-down'
  } else if (!loading && health) {
    statusLabel = 'online'
    statusClass = 'is-up'
  }

  return (
    <main className="app-shell">
      <section className="hero-panel">
        <p className="eyebrow">Minimal Working Setup</p>
        <h1>Pharmacy full-stack starter</h1>
        <p className="hero-copy">
          React runs in <code>frontend/</code>, Spring Boot runs in{' '}
          <code>backend/</code>, and this page checks the API through the Vite
          proxy.
        </p>
        <button className="refresh-button" onClick={handleRefresh} type="button">
          Refresh API status
        </button>
      </section>

      <section className="status-card">
        <div className="status-header">
          <div>
            <p className="section-label">Backend health</p>
            <h2>{loading ? 'Checking API...' : 'Connection snapshot'}</h2>
          </div>
          <span className={`status-pill ${statusClass}`}>{statusLabel}</span>
        </div>

        {error ? (
          <p className="error-text">{error}</p>
        ) : (
          <div className="meta-grid">
            <article>
              <span>Status</span>
              <strong>{health?.status ?? 'Loading'}</strong>
            </article>
            <article>
              <span>Service</span>
              <strong>{health?.service ?? 'Connecting'}</strong>
            </article>
            <article>
              <span>Message</span>
              <strong>{health?.message ?? 'Waiting for response'}</strong>
            </article>
            <article>
              <span>Timestamp</span>
              <strong>{formattedTimestamp}</strong>
            </article>
          </div>
        )}
      </section>

      <section className="steps-panel">
        <h2>Run it locally</h2>
        <div className="step-list">
          <div>
            <span>1</span>
            <p>
              In <code>backend/</code>, run <code>.\mvnw.cmd spring-boot:run</code>
            </p>
          </div>
          <div>
            <span>2</span>
            <p>
              In <code>frontend/</code>, run <code>npm run dev</code>
            </p>
          </div>
          <div>
            <span>3</span>
            <p>
              Open <code>http://localhost:5173</code> and confirm this card turns
              online
            </p>
          </div>
        </div>
      </section>
    </main>
  )
}

export default App
