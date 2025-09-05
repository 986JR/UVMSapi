
//dashboard New JS
document.addEventListener("DOMContentLoaded", () => {
    const apiBase = "https://uvmsapiv1.onrender.com/api";
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        setTimeout(() => {
            alert("You must login first!");
            window.location.href = "index.html";
        }, 3000);
    }

    //Logout
    const logoutBtn = document.getElementById('logout-btn');
    logoutBtn.addEventListener('click', () => {
        //alert()
        logout();
        return;
        alert("Actioned returned here");

        //window.location.href = '/index.html';
    });
    function logout() {
        localStorage.removeItem('jwtToken');
        console.log('🚪 User logged out');
      alert("This function is exceuted");
        window.location.href = 'index.html';
        throw new Error("Script stopped after logout");
    }




    const authHeaders = {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
    };


    //Fetch Vendor Info
    fetch(`${apiBase}/vendors/dashboard`, { headers: authHeaders })
        .then(response => {
            if (!response.ok) throw new Error("Unauthorized or session expired");
            return response.json();
        })
        .then(vendor => {
            document.querySelector(".navbar-user span").textContent = `${vendor.firstName} ${vendor.lastName}`;
            document.querySelector(".stats-grid .stat-card:nth-child(1) h4").textContent = vendor.contracts?.length || 0;
            document.querySelector(".stats-grid .stat-card:nth-child(2) h4").textContent = vendor.licenses?.length || 0;
            document.querySelector(".stats-grid .stat-card:nth-child(3) h4").textContent = vendor.applications?.length || 0;
            document.querySelector(".stats-grid .stat-card:nth-child(4) h4").textContent = vendor.renewalRequests?.length || 0;
        })
        .catch(err => {
            console.error("Error fetching vendor:", err);
            alert("Session expired. Please login again.");
            localStorage.removeItem("jwtToken");
            window.location.href = "index.html";
        });

    //Fetch Recent Tenders (last 3)
    fetch(`${apiBase}/tenders`, { headers: authHeaders })
        .then(response => response.json())
        .then(tenders => {
            const tendersContainer = document.querySelector(".tenders-container");
            tendersContainer.innerHTML = "";
            const lastThree = tenders.slice(-3).reverse();
            lastThree.forEach(t => {
                const div = document.createElement("div");
                div.classList.add("tender-item", "mb-2");
                div.style.cssText = "padding: 15px; border-left: 4px solid var(--uvms-success-green); background: var(--uvms-surface-light); margin-bottom: 10px;";
                div.innerHTML = `
                        <div class="flex flex-between align-center flex-mobile-stack">
                            <h4 style="margin: 0; font-size: 1em;">${t.title}</h4>
                            <small style="color: var(--uvms-text-secondary-light);">${t.tenderCode}</small>
                        </div>
                        <p style="margin: 5px 0; color: var(--uvms-text-secondary-light);">${t.college?.college_name || "N/A"} - Deadline: ${t.deadlineDate}</p>
                        <div class="flex align-center gap-1 flex-mobile-stack">
                            <span class="badge badge-success">${t.status || "open"}</span>
                        </div>
                    `;
                tendersContainer.appendChild(div);
            });
        })
        .catch(err => console.error("Error fetching tenders:", err));

    //Fetch Policies/Guidelines (last 3)
    fetch(`${apiBase}/policies`, { headers: authHeaders })
        .then(response => response.json())
        .then(policies => {
            const policiesContainer = document.querySelector(".policies-container");
            policiesContainer.innerHTML = "";
            const lastThree = policies.slice(-3).reverse();
            lastThree.forEach(p => {
                const div = document.createElement("div");
                div.classList.add("deadline-item");
                div.style.cssText = "padding: 12px; border-left: 4px solid var(--uvms-blue-primary); background: rgba(30, 136, 229, 0.1); margin-bottom: 10px;";
                div.innerHTML = `
                        <div class="flex flex-between align-center">
                            <h4 style="margin: 0; font-size: 0.9em;">${p.title}</h4>
                            <span class="badge badge-secondary">View</span>
                        </div>
                        <p style="margin: 5px 0 0 0; color: var(--uvms-text-secondary-light); font-size: 0.8em;">${p.description}</p>
                    `;
                policiesContainer.appendChild(div);
            });
        })
        .catch(err => console.error("Error fetching policies:", err));
});




