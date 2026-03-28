import { Navigate, Route, Routes } from 'react-router-dom'
import './App.css'
import FloatingActions from './components/FloatingActions.jsx'
import ScrollManager from './components/ScrollManager.jsx'
import SiteFooter from './components/SiteFooter.jsx'
import SiteHeader from './components/SiteHeader.jsx'
import AboutPage from './pages/AboutPage.jsx'
import ContactPage from './pages/ContactPage.jsx'
import HomePage from './pages/HomePage.jsx'
import OrderPage from './pages/OrderPage.jsx'
import ServicesPage from './pages/ServicesPage.jsx'

function App() {
  return (
    <div className="site-shell">
      <ScrollManager />
      <SiteHeader />

      <Routes>
        <Route element={<HomePage />} path="/" />
        <Route element={<AboutPage />} path="/about" />
        <Route element={<ServicesPage />} path="/services" />
        <Route element={<OrderPage />} path="/order" />
        <Route element={<ContactPage />} path="/contact" />
        <Route element={<Navigate replace to="/" />} path="*" />
      </Routes>

      <SiteFooter />
      <FloatingActions />
    </div>
  )
}

export default App
