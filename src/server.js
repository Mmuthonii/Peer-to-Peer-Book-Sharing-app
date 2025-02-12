// Required modules
const express = require('express');
const app = express();
const path = require('path');
const fs = require('fs');

// Define paths to data files
const dataPath = path.join(__dirname, 'data', 'books.json');
const usersPath = path.join(__dirname, 'data', 'users.json');

// Server port
const port = 8088;

// Serve static files from the "views" folder
app.use(express.static(path.join(__dirname, 'views')));

// Serve the Currently Reading HTML page
app.get('/currently_reading', (req, res) => {
    const user = req.query.user;  // Get the logged-in user from the query parameter

    if (!user) {
        return res.status(400).send('User not logged in');  // If no user is provided, show error
    }

    // Read the users.json to check if the user exists
    fs.readFile(usersPath, 'utf8', (err, data) => {
        if (err) {
            console.log('Error reading users file:', err);
            return res.status(500).send('Error reading users file');
        }

        const users = JSON.parse(data);  // Parse users.json
        const userExists = users.some(u => u.username === user);  // Check if the user exists in the file

        if (userExists) {
            // Send the Currently Reading page if the user is valid
            res.sendFile(path.join(__dirname, 'views', 'currently_reading.html'));
        } else {
            return res.status(400).send('User not found');  // If the user doesn’t exist
        }
    });
});

// Serve the login page
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'views', 'Login.html'));
});

// Fetch books for the logged-in user
app.get('/api/currently_reading', (req, res) => {
    const user = req.query.user;  // Get the user from the query string

    if (!user) {
        return res.status(400).send('User not logged in');
    }

    // Read books.json and send books that belong to the logged-in user
    fs.readFile(dataPath, 'utf8', (err, data) => {
        if (err) {
            console.error('Error reading the JSON file:', err);
            return res.status(500).send('Internal Server Error');
        }

        const books = JSON.parse(data);  // Parse books.json
        const userBooks = books.filter(book => book.username === user);  // Filter books by logged-in user

        res.json(userBooks);  // Send the books of the logged-in user as JSON
    });
});

// Handle the return of a book (delete request)
app.delete('/api/return_book/:id', (req, res) => {
    const bookId = req.params.id;  // Get the book ID from the URL

    // Read books.json to find and remove the book
    fs.readFile(dataPath, 'utf8', (err, data) => {
        if (err) {
            console.error('Error reading JSON file:', err);
            return res.status(500).send('Error reading file');
        }

        let books = JSON.parse(data);  // Parse books.json

        // Remove the book with the given ID
        books = books.filter(book => book.id !== parseInt(bookId));

        // Write the updated book list back to books.json
        fs.writeFile(dataPath, JSON.stringify(books, null, 2), 'utf8', (err) => {
            if (err) {
                console.log('Error writing to JSON file:', err);
                return res.status(500).send('Error writing file');
            }

            // Send a success response after the book is removed
            res.json({ message: 'Book returned successfully' });
        });
    });
});

// Start the server
app.listen(port, () => {
    console.log(`Server running on http://localhost:8088`);
});
