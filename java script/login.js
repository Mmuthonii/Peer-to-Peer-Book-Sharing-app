async function login() {
    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value.trim();
    const messageBox = document.getElementById("loginMessage");

    messageBox.textContent = "";

    if (!email || !password) {
        messageBox.textContent = "Please enter both email and password.";
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/users/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const responseBody = await response.json(); // ✅ Now parsing as JSON

        if (response.ok) {
            console.log("Login Response:", responseBody);

            sessionStorage.setItem("userId", String(responseBody.id));  // ✅ Store userId as a string
            localStorage.setItem("userId", String(responseBody.id));

            sessionStorage.setItem("userEmail", responseBody.email);
            sessionStorage.setItem("username", responseBody.username);

            alert("Login successful! Redirecting to home...");
            window.location.href = "home.html";
        } else {
            messageBox.textContent = responseBody.error || "Login failed.";
        }
    } catch (error) {
        console.error("Login error:", error);
        messageBox.textContent = "Login failed. Please try again.";
    }
}
