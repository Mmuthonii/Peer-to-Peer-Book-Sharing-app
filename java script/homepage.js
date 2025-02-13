document.addEventListener("DOMContentLoaded", () => {
    // Load recommended books on page load
    fetchRecommendedBooks();
});

// Function to handle Enter key press in search input
function handleSearch(event) {
    if (event.key === "Enter") {
        searchBooksFromNav();
    }
}

// Function to execute book search from the navigation bar
function searchBooksFromNav() {
    const query = document.querySelector(".search-popup input").value.trim();
    if (!query) {
        alert("Please enter a search query!");
        return;
    }
    window.location.href = `search-results.html?query=${encodeURIComponent(query)}`;
}

// Function to toggle the search popup visibility
function toggleSearchPopup() {
    const popup = document.querySelector(".search-popup");
    popup.style.display = popup.style.display === "block" ? "none" : "block";
}

// Function to toggle the dropdown visibility
function toggleDropdown() {
    const dropdown = document.querySelector(".dropdown");
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
}

// Function to handle language toggling
function toggleLanguage() {
    alert("Language toggled!");
}

// Fetch recommended books based on genres
async function fetchRecommendedBooks() {
    const genres = JSON.parse(sessionStorage.getItem("selectedGenres")) || ["Fiction", "Mystery", "Fantasy", "Romance", "Horror"];
    const recommendedReads = document.querySelector(".recommended-reads");
    recommendedReads.innerHTML = ""; // Clear previous content

    for (const genre of genres) {
        try {
            const randomStartIndex = Math.floor(Math.random() * 20); // Randomize start index for variety
            const response = await fetch(
                `https://www.googleapis.com/books/v1/volumes?q=subject:${encodeURIComponent(genre)}&startIndex=${randomStartIndex}&maxResults=1`
            );
            const data = await response.json();

            if (data.items) {
                data.items.forEach((book) => {
                    const bookDiv = document.createElement("div");
                    bookDiv.classList.add("book-placeholder");

                    bookDiv.innerHTML = `
                        <img src="${book.volumeInfo.imageLinks?.thumbnail || 'https://via.placeholder.com/100x150'}" alt="${book.volumeInfo.title}" />
                        <div class="book-details">
                            <p><strong>${book.volumeInfo.title || "Unknown Title"}</strong></p>
                            <p>${book.volumeInfo.authors?.join(", ") || "Unknown Author"}</p>
                        </div>
                    `;
                    recommendedReads.appendChild(bookDiv);
                });
            }
        } catch (error) {
            console.error(`Error fetching books for genre: ${genre}`, error);
        }
    }
}

// Redirect to profile page
function redirectToProfile() {
    window.location.href = "profile.html";
}

// Logout function to clear session and redirect
function logout() {
    sessionStorage.clear();
    localStorage.clear();
    alert("You have been logged out.");
    window.location.href = "index.html";
}

// Book search from the search bar in shelves section
async function searchBooks() {
    const searchInput = document.querySelector(".search-bar input");
    const query = searchInput.value.trim();

    if (!query) {
        alert("Please enter a search query!");
        return;
    }

    // Redirect to search results page with query as a parameter
    window.location.href = `search-results.html?query=${encodeURIComponent(query)}`;
}

// Ensure search button executes `searchBooks()` on click
document.addEventListener("DOMContentLoaded", () => {
    const searchButton = document.querySelector(".search-bar button");
    if (searchButton) {
        searchButton.addEventListener("click", (event) => {
            event.preventDefault(); // Prevents default form submission
            searchBooks();
        });
    }
});

// Display search results dynamically
async function displaySearchResults() {
    const query = document.querySelector(".search-bar input").value.trim().toLowerCase();
    const searchResults = document.querySelector(".search-results");

    searchResults.innerHTML = ""; // Clear previous results

    if (!query) {
        searchResults.innerHTML = "<p>Please enter a search term</p>";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/books/search?query=${query}`);
        if (!response.ok) throw new Error("Failed to fetch books");

        const books = await response.json();

        if (books.length === 0) {
            searchResults.innerHTML = "<p>No books found</p>";
        } else {
            books.forEach(book => {
                const bookCard = document.createElement("div");
                bookCard.className = "book-card";
                bookCard.innerHTML = `
                    <img src="${book.image || 'https://via.placeholder.com/100'}" alt="${book.title}" class="book-thumbnail">
                    <div class="book-details">
                        <h3>${book.title}</h3>
                        <p>Author: ${book.author || "Unknown"}</p>
                        <p>Genre: ${book.genre || "Unknown"}</p>
                        <button onclick="viewBookDetails(${book.id})">View Details</button>
                    </div>
                `;
                searchResults.appendChild(bookCard);
            });
        }
    } catch (error) {
        console.error("Error fetching books:", error);
    }
}

// Redirect to book details page
function viewBookDetails(bookId) {
    window.location.href = `book-details.html?bookId=${bookId}`;
}

// Hide dropdown when clicking outside
document.addEventListener("click", (event) => {
    const dropdown = document.querySelector(".dropdown");
    if (dropdown && !dropdown.contains(event.target)) {
        dropdown.style.display = "none";
    }
});
