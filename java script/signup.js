function signUp() {
    const username = document.getElementById("username").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const dob = document.getElementById("dob").value.trim();
    const gender = document.getElementById("gender").value.trim();
    const location = document.getElementById("city").value.trim();

    if (!username || !email || !password || !dob || !gender || !location) {
        alert("Please fill all fields.");
        return;
    }

    const user = { username, email, password, dob, gender, location };

    fetch("http://localhost:8080/users/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(user),
    })
        .then(response => response.text())
        .then(data => {
        alert(data);
        if (data.includes("User registered successfully")) {
            window.location.href = "index.html"; // Redirect to login
        }
    })
        .catch(error => console.error("Signup failed:", error));
}
