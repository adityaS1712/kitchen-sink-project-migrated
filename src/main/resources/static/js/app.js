document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const loginHeading = document.getElementById('loginHeading');
    const mainContent = document.getElementById('mainContent');

    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
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
                    mainContent.style.display = 'flex';
                    loginForm.style.display = 'none';
                    loginHeading.style.display = 'none';
                } else {
                    document.getElementById('messageSection').innerText = 'Authentication failed!';
                }
            })
            .catch(error => {
                document.getElementById('messageSection').innerText = 'Error: ' + error.message;
            });
        });
    }

    document.getElementById('fetchAllButton').addEventListener('click', function() {
        fetchDataAndRenderTable('/kitchensink/fetch/all');
    });

    document.getElementById('fetchByIdButton').addEventListener('click', function() {
        const id = document.getElementById('memberId').value;
        if (id) {
            fetchDataAndRenderTable(`/kitchensink/fetch/${id}`);
        } else {
            document.getElementById('messageSection').innerText = 'Error: Member ID is required';
        }
    });

    document.getElementById('fetchByEmailButton').addEventListener('click', function() {
        const email = document.getElementById('memberEmail').value;
        if (email) {
            fetchDataAndRenderTable(`/kitchensink/fetch/email/${email}`);
        } else {
            document.getElementById('messageSection').innerText = 'Error: Member Email is required';
        }
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
                document.getElementById('registrationMessage').innerText = 'Registration successful!';
            } else {
                document.getElementById('registrationMessage').innerText = 'Registration failed! email is already registered';
            }
        })
        .catch(error => {
            document.getElementById('registrationMessage').innerText = 'Error: ' + error.message;
        });
    });

    document.getElementById('toggleFetchSectionButton').addEventListener('click', function() {
        const fetchSection = document.getElementById('fetchSection');
        if (fetchSection.style.display === 'none') {
            fetchSection.style.display = 'block';
            this.innerText = 'Hide Data Table and Fetch Functionality';
        } else {
            fetchSection.style.display = 'none';
            this.innerText = 'Show Data Table and Fetch Functionality';
        }
    });

    document.getElementById('memberId').addEventListener('input', function(event) {
        this.value = this.value.replace(/\D/g, '');
        document.getElementById('messageSection').innerText = '';
    });

    document.getElementById('phoneNumber').addEventListener('input', function(event) {
        this.value = this.value.replace(/\D/g, '');
    });

    document.getElementById('memberEmail').addEventListener('input', function(event) {
        document.getElementById('messageSection').innerText = '';
    });
});

function fetchDataAndRenderTable(url) {
    const token = localStorage.getItem('jwtToken');
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token,
        }
    })
    .then(response => response.text())
    .then(text => {
        let data;
        try {
            data = JSON.parse(text);
        } catch (error) {
            throw new Error('User not registered yet');
        }
        if (!data || (Array.isArray(data) && data.length === 0)) {
            document.getElementById('messageSection').innerText = 'User is not registered yet, please register';
            data = [{}];
        } else if (!Array.isArray(data)) {
            data = [data];
        }
        const tableContainer = document.getElementById('tableContainer');
        tableContainer.innerHTML = generateTableHTML(data);
    })
    .catch(error => {
        document.getElementById('messageSection').innerText = 'Error fetching data: ' + error.message;
        console.error('Error fetching data:', error);
    });
}

function generateTableHTML(data) {
    let tableHTML = '<table><thead><tr>';
    for (const key in data[0]) {
        tableHTML += `<th>${key}</th>`;
    }
    tableHTML += '</tr></thead><tbody>';
    data.forEach(row => {
        tableHTML += '<tr>';
        for (const key in row) {
            tableHTML += `<td>${row[key] || ''}</td>`;
        }
        tableHTML += '</tr>';
    });
    tableHTML += '</tbody></table>';
    return tableHTML;
}