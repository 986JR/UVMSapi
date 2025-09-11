//dashboard New JS
document.addEventListener("DOMContentLoaded", () => {
    const apiBase = "https://uvmsapiv1.onrender.com/api";
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        setTimeout(() => {
            alert("You must login first!");
            window.location.href = "index.html";
        }, 3000);
        return;
    }

    //Logout
    const logoutBtn = document.getElementById('logout-btn');
    logoutBtn.addEventListener('click', () => {
        logout();
    });

    function logout() {
        localStorage.removeItem('jwtToken');
        alert("You have been logged out!");
        window.location.href = 'index.html';
    }

    const authHeaders = {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
    };

    let vendorId = null;

    // 1️⃣ Fetch Vendor Info
    fetch(`${apiBase}/vendors/dashboard`, { headers: authHeaders })
        .then(response => {
            if (!response.ok) throw new Error("Unauthorized or session expired");
            return response.json();
        })
        .then(vendor => {
            vendorId = vendor.vendorId;
            document.querySelector(".navbar-user span").textContent = `${vendor.firstName} ${vendor.lastName}`;

            // After vendor is loaded → fetch stats
            loadVendorStats(vendorId);
        })
        .catch(err => {
            console.error("Error fetching vendor:", err);
            alert("Session expired. Please login again.");
            localStorage.removeItem("jwtToken");
            window.location.href = "index.html";
        });

    // 2️⃣ Function to fetch & update stats
    function loadVendorStats(vendorId) {
        // Licenses
        fetch(`${apiBase}/licenses`, { headers: authHeaders })
            .then(res => res.json())
            .then(data => {
                // Filter licenses for this vendor (handle object or number)
                const myLicenses = data.filter(l => {
                    if (typeof l.vendor === "object") return l.vendor.vendorId === vendorId;
                    return l.vendor === vendorId;
                });
                document.querySelector(".stats-grid .stat-card:nth-child(2) h4").textContent = myLicenses.length;
            });

        // Applications
        fetch(`${apiBase}/applications`, { headers: authHeaders })
            .then(res => res.json())
            .then(data => {
                const myApplications = data.filter(a => {
                    if (typeof a.vendor === "object") return a.vendor.vendorId === vendorId;
                    return a.vendor === vendorId;
                });
                document.querySelector(".stats-grid .stat-card:nth-child(3) h4").textContent = myApplications.length;
            });

        // Renewals
        fetch(`${apiBase}/licenses`, { headers: authHeaders })
            .then(res => res.json())
            .then(data => {
                let renewalCount = 0;
                data.forEach(l => {
                    const vId = typeof l.vendor === "object" ? l.vendor.vendorId : l.vendor;
                    if (vId === vendorId && l.renewalRequests && l.renewalRequests.length > 0) {
                        renewalCount += l.renewalRequests.length;
                    }
                });
                document.querySelector(".stats-grid .stat-card:nth-child(4) h4").textContent = renewalCount;
            });

        // Contracts (if needed)
        fetch(`${apiBase}/applications`, { headers: authHeaders })
            .then(res => res.json())
            .then(data => {
                const myContracts = data.filter(a => {
                    const vId = typeof a.vendor === "object" ? a.vendor.vendorId : a.vendor;
                    return vId === vendorId && a.approvedContractPath;
                });
                document.querySelector(".stats-grid .stat-card:nth-child(1) h4").textContent = myContracts.length;
            });
    }

    // 3️⃣ Fetch Recent Tenders (last 3)
    fetch(`${apiBase}/colleges`, { headers: authHeaders })
        .then(res => res.json())
        .then(colleges => {
            const collegeMap = {};
            colleges.forEach(c => { collegeMap[c.college_id] = c.college_name; });

            return fetch(`${apiBase}/tenders`, { headers: authHeaders })
                .then(res => res.json())
                .then(tenders => {
                    const container = document.querySelector(".tenders-container");
                    container.innerHTML = "";
                    const lastThree = tenders.slice(-3).reverse();
                    lastThree.forEach(t => {
                        const div = document.createElement("div");
                        div.classList.add("tender-item", "mb-2");
                        div.style.cssText = "padding:15px;border-left:4px solid var(--uvms-success-green);background:var(--uvms-surface-light);margin-bottom:10px;";
                        div.innerHTML = `
                            <div class="flex flex-between align-center flex-mobile-stack">
                                <h4 style="margin:0;font-size:1em;">${t.title}</h4>
                                <small style="color: var(--uvms-text-secondary-light);">${t.tenderCode || ""}</small>
                            </div>
                            <p style="margin:5px 0;color: var(--uvms-text-secondary-light);">${collegeMap[t.college] || "N/A"} - Deadline: ${t.deadlineDate}</p>
                            <div class="flex align-center gap-1 flex-mobile-stack">
                                <span class="badge badge-success">${t.status || "open"}</span>
                            </div>
                        `;
                        container.appendChild(div);
                    });
                });
        })
        .catch(err => console.error("Error fetching tenders:", err));

    // 4️⃣ Fetch Policies/Guidelines (last 3)
    fetch(`${apiBase}/policies`, { headers: authHeaders })
        .then(res => res.json())
        .then(policies => {
            const container = document.querySelector(".policies-container");
            container.innerHTML = "";
            const lastThree = policies.slice(-3).reverse();
            lastThree.forEach(p => {
                const div = document.createElement("div");
                div.classList.add("deadline-item");
                div.style.cssText = "padding:12px;border-left:4px solid var(--uvms-blue-primary);background:rgba(30,136,229,0.1);margin-bottom:10px;";
                div.innerHTML = `
                    <div class="flex flex-between align-center">
                        <h4 style="margin:0;font-size:0.9em;">${p.title}</h4>
                        <span class="badge badge-secondary">View</span>
                    </div>
                    <p style="margin:5px 0 0 0;color: var(--uvms-text-secondary-light);font-size:0.8em;">${p.description}</p>
                `;
                container.appendChild(div);
            });
        })
        .catch(err => console.error("Error fetching policies:", err));
});
