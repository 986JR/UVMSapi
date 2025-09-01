// A wrapper around fetch that automatically adds Authorization header
async function authFetch(url, options = {}) {
    const token = localStorage.getItem("jwt");

    const headers = {
        ...(options.headers || {}),
        "Content-Type": "application/json"
    };

    if (token) {
        headers["Authorization"] = `Bearer ${token}`;
    }

    return fetch(url, { ...options, headers });
}
