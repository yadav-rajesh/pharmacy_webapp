import { Suspense, lazy } from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import './App.css'
import FloatingActions from './components/FloatingActions.jsx'
import ScrollManager from './components/ScrollManager.jsx'
import SiteFooter from './components/SiteFooter.jsx'
import SiteHeader from './components/SiteHeader.jsx'
import HomePage from './pages/HomePage.jsx'

const AboutPage = lazy(() => import('./pages/AboutPage.jsx'))
const ContactPage = lazy(() => import('./pages/ContactPage.jsx'))
const OrderPage = lazy(() => import('./pages/OrderPage.jsx'))
const ServicesPage = lazy(() => import('./pages/ServicesPage.jsx'))

function App() {
  return (
    <div className="site-shell">
      <ScrollManager />
      <SiteHeader />

      <Suspense
        fallback={
          <div className="route-loading" role="status">
            <div className="route-loading-card">
              <span className="route-loading-dot" />
              <p>Loading page...</p>
            </div>
          </div>
        }
      >
        <Routes>
          <Route element={<HomePage />} path="/" />
          <Route element={<AboutPage />} path="/about" />
          <Route element={<ServicesPage />} path="/services" />
          <Route element={<OrderPage />} path="/order" />
          <Route element={<ContactPage />} path="/contact" />
          <Route element={<Navigate replace to="/" />} path="*" />
        </Routes>
      </Suspense>

      <SiteFooter />
      <FloatingActions />
    </div>
  )
}

export default App
