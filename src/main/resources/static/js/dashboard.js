document.addEventListener("DOMContentLoaded", async () => {
    // 1. Get token from localStorage
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        // No token → send user back to login
        alert("No token found. Please login first.");
        setTimeout(() => {
            window.location.href = "login.html";
        }, 1500);
        return;
    }

    try {
        // 2. Validate token + fetch dashboard data
        const response = await fetch("http://localhost:8080/api/dashboard", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`, // Bearer <token>
                "Content-Type": "application/json"
            }
        });

        // 3. If backend says forbidden → logout user
        if (response.status === 403) {
            alert("Forbidden: Token is invalid or expired.");
            localStorage.removeItem("jwtToken");
            setTimeout(() => {
                window.location.href = "login.html";
            }, 1500);
            return;
        }

        // 4. Catch any other error
        if (!response.ok) {
            alert("Unexpected error: " + response.status);
            throw new Error("Request failed with status " + response.status);
        }

        // 5. If everything is fine → get the response JSON
        const data = await response.json();



        // 6. Show welcome message
        document.getElementById("welcomeMsg").innerText =
            ` Welcome, ${data.first_name || data.username}!`;

        // 7. Update navbar user info
        if (data.firstName && data.lastName) {
            const initials =
                data.firstName.charAt(0) + data.lastName.charAt(0);

            // Put initials inside user avatar circle
            const avatarElement = document.querySelector(".user-avatar");
            if (avatarElement) {
                avatarElement.innerText = initials.toUpperCase();
            }

            // Put full name in navbar text
            const navbarName = document.querySelector(".navbar-user span");
            if (navbarName) {
                console.log(data.firstName);
                navbarName.innerText = `${data.firstName} ${data.lastName}`;

            }
        }

        // Optional: You can also display other data dynamically
        console.log("User dashboard data:", data);

    } catch (err) {
        console.error("Access denied:", err);
        alert("Error: " + err.message);
        localStorage.removeItem("jwtToken");
        setTimeout(() => {
            window.location.href = "login.html";
        }, 1500);
    }
});

// 8. Logout function
function logout() {
    localStorage.removeItem("jwtToken");
    window.location.href = "login.html";
}
