// login.js
document.getElementById('loginForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const body = {
        email: document.getElementById("email").value.trim(),
        password: document.getElementById("password").value.trim()
    };

    try {
        //Send login request
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        if (!response.ok) {
            throw new Error("Invalid login credentials");
        }

        //Extract token from backend response
        const data = await response.json();
        const token = data.token; // backend should return { token: "..." }

        if (!token) {
            throw new Error("No token received from server.");
        }

        //Store token in localStorage
        localStorage.setItem("jwtToken", token);

        //Validate token by hitting protected endpoint
        const validateRes = await fetch("http://localhost:8080/api/dashboard", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        });

        if (validateRes.ok) {
            //Redirect if token is valid
            window.location.href = "dashboard2.html";
        } else if (validateRes.status === 403 || validateRes.status === 401) {
            alert("Unauthorized! Please login again.");
            localStorage.removeItem("jwtToken"); // clear invalid token
        } else {
            throw new Error("Unexpected error: " + validateRes.status);
        }

    } catch (err) {
        console.error("Login error:", err);
        alert("Login failed: " + err.message);
    }
});

// Optional: Auto-check if already logged in
window.addEventListener("DOMContentLoaded", async () => {
    const token = localStorage.getItem("jwtToken");
    if (token) {
        try {
            const res = await fetch("http://localhost:8080/api/dashboard", {
                method: "GET",
                headers: { "Authorization": "Bearer " + token }
            });
            if (res.ok) {
            alert("gone!")
                window.location.href = "dashboard2.html";
            }
        } catch (err) {
            console.warn("Auto-login failed", err);
            localStorage.removeItem("jwtToken");
        }
    }
});
