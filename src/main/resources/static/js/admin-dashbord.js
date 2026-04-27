function filterRole(role) {

    fetch("/api/utilisateurs?role=" + role)
        .then(res => res.json())
        .then(data => {

            let html = "<table border='1'>";
            html += "<tr><th>Nom</th><th>Email</th><th>Rôle</th><th>Action</th></tr>";

            data.forEach(u => {
                html += `
                    <tr>
                        <td>${u.nom}</td>
                        <td>${u.email}</td>
                        <td>${u.role}</td>
                        <td>
                            <button onclick="editUser(${u.id})">Modifier</button>
                        </td>
                    </tr>
                `;
            });

            html += "</table>";

            document.getElementById("tableZone").innerHTML = html;
        });
}

function editUser(id) {
    window.location.href = "/admin/utilisateurs/edit/" + id;
}