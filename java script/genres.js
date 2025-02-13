document.addEventListener("DOMContentLoaded", () => {
    const genres = document.querySelectorAll(".genre-button");
    const continueButton = document.querySelector(".continue-button");
    let selectedGenres = [];

    genres.forEach((genre) => {
        genre.addEventListener("click", () => {
            const genreName = genre.textContent;

            if (selectedGenres.includes(genreName)) {
                // Deselect genre
                selectedGenres = selectedGenres.filter((g) => g !== genreName);
                genre.classList.remove("selected");
            } else if (selectedGenres.length < 5) {
                // Select genre
                selectedGenres.push(genreName);
                genre.classList.add("selected");
            }

            // Enable or disable the continue button
            continueButton.disabled = selectedGenres.length === 0;
        });
    });

    continueButton.addEventListener("click", () => {
        // Store selected genres in sessionStorage
        sessionStorage.setItem("selectedGenres", JSON.stringify(selectedGenres));
        window.location.href = "homepage.html"; // Redirect to homepage
    });
});
