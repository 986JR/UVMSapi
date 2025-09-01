document.getElementById('registerForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (password !== confirmPassword) {
        alert("Passwords do not match!");
        return;
    }

    const body = {
        first_name: document.getElementById("firstName").value.trim(),
        last_name: document.getElementById("lastName").value.trim(),
        company_name: document.getElementById("companyName").value.trim(),
        email: document.getElementById("email").value.trim(),
        phone_number: document.getElementById("phone").value.trim(),
        password: password,
        business_address: document.getElementById("address").value.trim(),
        business_type: document.getElementById("businessType").value.trim(),
        // Optional, can send empty if not provided

        tin_number: document.getElementById("tinNumber").value.trim(),
        profile_picture_path: ""
    };

console.log("It is here");

    try {
        const res = await fetch("/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            const errorText = await res.text();
            alert("Registration failed: " + errorText);
            return;
        }

        // If backend returns a token (auto-login flow)
        const data = await res.json().catch(() => ({}));
        if (data.token) {
            localStorage.setItem("jwt", data.token);
            window.location.href = "dashboard2.html";
        } else {
            alert("Registration successful. Please login.");
            window.location.href = "login.html";
        }
    } catch (err) {
        console.error("Network error:", err);
        alert("Network error, please try again later.");
    }
});
