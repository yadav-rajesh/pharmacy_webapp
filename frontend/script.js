const whatsappNumber = "919730086267";
const whatsappBase = `https://wa.me/${whatsappNumber}`;

const openWhatsApp = (message) => {
  const url = `${whatsappBase}?text=${encodeURIComponent(message)}`;
  const popup = window.open(url, "_blank", "noopener");
  if (!popup) {
    window.location.href = url;
  }
};

const setBusyState = (form, isBusy) => {
  const submitButton = form.querySelector('button[type="submit"]');
  if (!submitButton) {
    return;
  }

  submitButton.disabled = isBusy;
  submitButton.textContent = isBusy
    ? submitButton.dataset.loadingText || "Please wait..."
    : submitButton.dataset.defaultText || submitButton.textContent;
};

const postFormData = async (url, formData) => {
  const response = await fetch(url, {
    method: "POST",
    body: formData
  });

  const contentType = response.headers.get("content-type") || "";
  const data = contentType.includes("application/json")
    ? await response.json()
    : { message: await response.text() };

  if (!response.ok) {
    throw new Error(data.message || "Request failed");
  }

  return data;
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

document.querySelectorAll('button[type="submit"]').forEach((button) => {
  button.dataset.defaultText = button.textContent;
});

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
  form.addEventListener("submit", async (event) => {
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

    const submitButton = form.querySelector('button[type="submit"]');
    if (submitButton) {
      submitButton.dataset.loadingText = "Saving inquiry...";
    }

    setBusyState(form, true);
    try {
      await postFormData("/api/inquiries", formData);
      openWhatsApp(message);
      form.reset();
      setStatus(form, "Inquiry saved and WhatsApp opened for a quick response.", "success");
    } catch (error) {
      openWhatsApp(message);
      setStatus(
        form,
        `${error.message}. WhatsApp still opened so you can continue the inquiry.`,
        "error"
      );
    } finally {
      setBusyState(form, false);
    }
  });
});

const orderForm = document.querySelector("[data-order-form]");
if (orderForm) {
  orderForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const formData = new FormData(orderForm);
    const prescriptionInput = orderForm.querySelector("[data-file-input]");
    const selectedFile = prescriptionInput && prescriptionInput.files && prescriptionInput.files[0]
      ? prescriptionInput.files[0]
      : null;
    const selectedFileName = selectedFile ? selectedFile.name : "Not uploaded";

    const message = [
      "Hello Padmavati Medicals,",
      "",
      "I would like to request medicines.",
      `Name: ${formData.get("name") || ""}`,
      `Phone number: ${formData.get("phone") || ""}`,
      `Medicine name: ${formData.get("medicine") || ""}`,
      `Address: ${formData.get("address") || ""}`,
      `Prescription file: ${selectedFileName}`,
      "",
      "Please confirm availability, pricing, and pickup or delivery guidance."
    ].join("\n");

    const submitButton = orderForm.querySelector('button[type="submit"]');
    if (submitButton) {
      submitButton.dataset.loadingText = "Saving request...";
    }

    setBusyState(orderForm, true);
    try {
      await postFormData("/api/orders", formData);
      openWhatsApp(message);
      orderForm.reset();
      const fileLabel = document.querySelector('[data-file-name="order-prescription"]');
      if (fileLabel) {
        fileLabel.textContent = "Accepted formats: JPG, PNG, PDF";
      }
      setStatus(
        orderForm,
        "Request saved in the system and WhatsApp opened for store follow-up.",
        "success"
      );
    } catch (error) {
      openWhatsApp(message);
      setStatus(
        orderForm,
        `${error.message}. WhatsApp still opened so you can continue the order.`,
        "error"
      );
    } finally {
      setBusyState(orderForm, false);
    }
  });
}
