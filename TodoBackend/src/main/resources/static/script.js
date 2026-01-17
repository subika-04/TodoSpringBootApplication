// ================= CONFIG =================
const SERVER_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

// ================= LOGIN =================
function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch(`${SERVER_URL}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => {
                throw new Error(data.message || "Login failed");
            });
        }
        return response.json();
    })
    .then(data => {
        localStorage.setItem("token", data.token);
        window.location.href = "todos.html";
    })
    .catch(error => {
        alert(error.message);
    });
}

// ================= REGISTER =================
function register() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch(`${SERVER_URL}/auth/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => {
                throw new Error(data.message || "Registration failed");
            });
        }
        alert("Registration successful. Please login.");
        window.location.href = "login.html";
    })
    .catch(error => {
        alert(error.message);
    });
}

// ================= LOAD TODOS =================
function loadTodos() {
    if (!token) {
        alert("Please login first");
        window.location.href = "login.html";
        return;
    }

    fetch(`${SERVER_URL}/api/v1/todo`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to load todos");
        }
        return response.json();
    })
    .then(todos => {
        const todoList = document.getElementById("todo-list");
        todoList.innerHTML = "";

        if (!todos || todos.length === 0) {
            todoList.innerHTML = "<p>No Todos</p>";
            return;
        }

        todos.forEach(todo => {
            todoList.appendChild(createTodoCard(todo));
        });
    })
    .catch(error => {
        alert(error.message);
    });
}

// ================= CREATE TODO CARD =================
function createTodoCard(todo) {
    const card = document.createElement("div");
    card.className = "todo-card";

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = todo.isCompleted;

    checkbox.addEventListener("change", () => {
        updateTodoStatus({
            ...todo,
            isCompleted: checkbox.checked
        });
    });

    const span = document.createElement("span");
    span.textContent = todo.title;

    if (todo.isCompleted) {
        span.style.textDecoration = "line-through";
        span.style.color = "#aaa";
    }

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.onclick = () => deleteTodo(todo.id);

    card.appendChild(checkbox);
    card.appendChild(span);
    card.appendChild(deleteBtn);

    return card;
}

// ================= ADD TODO =================
function addTodo() {
    const input = document.getElementById("new-todo");
    const todoText = input.value.trim();

    if (!todoText) {
        alert("Todo cannot be empty");
        return;
    }

    fetch(`${SERVER_URL}/api/v1/todo/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            title: todoText,
            isCompleted: false
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to add todo");
        }
        return response.json();
    })
    .then(() => {
        input.value = "";
        loadTodos();
    })
    .catch(error => {
        alert(error.message);
    });
}

// ================= UPDATE TODO =================
function updateTodoStatus(todo) {
    fetch(`${SERVER_URL}/api/v1/todo`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(todo)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to update todo");
        }
        return response.json();
    })
    .then(() => loadTodos())
    .catch(error => {
        alert(error.message);
    });
}

// ================= DELETE TODO =================
function deleteTodo(id) {
    fetch(`${SERVER_URL}/api/v1/todo/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to delete todo");
        }
        loadTodos();
    })
    .catch(error => {
        alert(error.message);
    });
}

// ================= PAGE LOAD =================
document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("todo-list")) {
        loadTodos();
    }
});
