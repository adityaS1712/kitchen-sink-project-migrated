document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch('/kitchensink/auth/authenticate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.token) {
            localStorage.setItem('jwtToken', data.token);
            document.getElementById('registrationSection').style.display = 'block';
            document.getElementById('loginForm').style.display = 'none';
        } else {
            document.getElementById('messageSection').innerText = 'Authentication failed!';
        }
    })
    .catch(error => {
        document.getElementById('messageSection').innerText = 'Error: ' + error.message;
    });
});

document.getElementById('registrationForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const phoneNumber = document.getElementById('phoneNumber').value;
    const token = localStorage.getItem('jwtToken');

    fetch('/kitchensink/members/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token,
        },
        body: JSON.stringify({ name, email, phoneNumber }),
    })
   .then(response => response.text())
       .then(data => {
           if (data === 'success') {
               document.getElementById('messageSection').innerText = 'Registration successful!';
           } else {
               document.getElementById('messageSection').innerText = 'Registration failed!';
           }
       })
       .catch(error => {
           document.getElementById('messageSection').innerText = 'Error: ' + error.message;
       });
   });
