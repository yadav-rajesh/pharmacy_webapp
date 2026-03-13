const whatsappNumber = "919730086267";
const whatsappBase = `https://wa.me/${whatsappNumber}`;

const openWhatsApp = (message) => {
  const url = `${whatsappBase}?text=${encodeURIComponent(message)}`;
  const popup = window.open(url, "_blank", "noopener");
  if (!popup) {
    window.location.href = url;
  }
};

const navToggle = document.querySelector("[data-nav-toggle]");
const siteNav = document.querySelector("[data-site-nav]");

if (navToggle && siteNav) {
  navToggle.addEventListener("click", () => {
    const isOpen = navToggle.getAttribute("aria-expanded") === "true";
    navToggle.setAttribute("aria-expanded", String(!isOpen));
    siteNav.classList.toggle("is-open", !isOpen);
  });

  siteNav.querySelectorAll("a").forEach((link) => {
    link.addEventListener("click", () => {
      navToggle.setAttribute("aria-expanded", "false");
      siteNav.classList.remove("is-open");
    });
  });
}

const currentPage = document.body.dataset.page;
document.querySelectorAll("[data-page-link]").forEach((link) => {
  if (link.dataset.pageLink === currentPage) {
    link.classList.add("is-active");
  }
});

document.querySelectorAll("[data-year]").forEach((yearNode) => {
  yearNode.textContent = new Date().getFullYear();
});

const revealElements = document.querySelectorAll(".reveal");
if ("IntersectionObserver" in window && revealElements.length) {
  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("is-visible");
          observer.unobserve(entry.target);
        }
      });
    },
    { threshold: 0.12 }
  );

  revealElements.forEach((element) => observer.observe(element));
} else {
  revealElements.forEach((element) => element.classList.add("is-visible"));
}

const setStatus = (form, message, type) => {
  const status = form.querySelector("[data-form-status]");
  if (!status) {
    return;
  }

  status.textContent = message;
  status.className = `status-message ${type} is-visible`;
};

document.querySelectorAll("[data-file-input]").forEach((input) => {
  input.addEventListener("change", () => {
    const label = document.querySelector(`[data-file-name="${input.id}"]`);
    if (!label) {
      return;
    }

    label.textContent = input.files && input.files[0]
      ? `Selected file: ${input.files[0].name}`
      : "Accepted formats: JPG, PNG, PDF";
  });
});

document.querySelectorAll("[data-inquiry-form]").forEach((form) => {
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const formData = new FormData(form);
    const medicine = formData.get("medicine");
    const phone = formData.get("phone");
    const message = [
      "Hello Padmavati Medicals,",
      "",
      "I would like to check medicine availability.",
      `Medicine name: ${medicine || "Not provided"}`,
      `Phone number: ${phone || "Not provided"}`,
      "",
      "Please confirm stock and next steps."
    ].join("\n");

    openWhatsApp(message);
    setStatus(form, "WhatsApp opened for a quick availability inquiry.", "info");
  });
});

const orderForm = document.querySelector("[data-order-form]");
if (orderForm) {
  orderForm.addEventListener("submit", (event) => {
    event.preventDefault();
    const formData = new FormData(orderForm);
    const prescriptionInput = orderForm.querySelector("[data-file-input]");
    const selectedFile = prescriptionInput && prescriptionInput.files && prescriptionInput.files[0]
      ? prescriptionInput.files[0].name
      : "Will share in WhatsApp";

    const message = [
      "Hello Padmavati Medicals,",
      "",
      "I would like to request medicines.",
      `Name: ${formData.get("name") || ""}`,
      `Phone number: ${formData.get("phone") || ""}`,
      `Medicine name: ${formData.get("medicine") || ""}`,
      `Address: ${formData.get("address") || ""}`,
      `Prescription file: ${selectedFile}`,
      "",
      "Please confirm availability, pricing, and pickup or delivery guidance."
    ].join("\n");

    openWhatsApp(message);
    setStatus(
      orderForm,
      "WhatsApp opened. Attach the prescription image or PDF in the chat before sending.",
      "success"
    );
  });
}
