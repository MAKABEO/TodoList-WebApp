const SELECTOR_SIDEBAR_WRAPPER = '.sidebar-wrapper';
const Default = {
    scrollbarTheme: 'os-theme-light',
    scrollbarAutoHide: 'leave',
    scrollbarClickScroll: true,
};
document.addEventListener('DOMContentLoaded', function () {
const sidebarWrapper = document.querySelector(SELECTOR_SIDEBAR_WRAPPER);
if (sidebarWrapper && typeof OverlayScrollbarsGlobal?.OverlayScrollbars !== 'undefined') {
  OverlayScrollbarsGlobal.OverlayScrollbars(sidebarWrapper, {
    scrollbars: {
      theme: Default.scrollbarTheme,
      autoHide: Default.scrollbarAutoHide,
      clickScroll: Default.scrollbarClickScroll,
    },
  });
}
});

document.addEventListener("DOMContentLoaded", function() {
  flatpickr("input[type='date']", {
      dateFormat: "Y-m-d",
      allowInput: true
  });
});

document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("taskModal");
    const modalTitle = modal.querySelector(".modal-title");
    const form = modal.querySelector("form");

    const titleInput = form.querySelector("input[name='title']");
    const descriptionInput = form.querySelector("textarea[name='description']");
    const statusSelect = form.querySelector("select[name='status']");
    const startDateInput = form.querySelector("input[name='startDate']");
    const targetDateInput = form.querySelector("input[name='targetDate']");
    const actionInput = form.querySelector("input[name='id']");

    document.querySelectorAll(".edit-task-btn").forEach(button => {
        button.addEventListener("click", function () {
            modalTitle.textContent = "Edit Task";
            form.action = urls.api;
            document.getElementById("formMethod").value = "put";

            actionInput.value = this.dataset.id;
            titleInput.value = this.dataset.title;
            descriptionInput.value = this.dataset.description;
            statusSelect.value = this.dataset.status;
            startDateInput.value = this.dataset.startdate;
            targetDateInput.value = this.dataset.targetdate;
        });
    });

    document.querySelector(".new-task-btn").addEventListener("click", function () {
        modalTitle.textContent = "New Task";
        form.action = urls.api;
        form.method = "post";

        actionInput.value = "";
        titleInput.value = "";
        descriptionInput.value = "";
        statusSelect.value = "PENDING";
        startDateInput.value = "";
        targetDateInput.value = "";
    });

    document.querySelector(".nav-link[data-bs-target='#taskModal']").addEventListener("click", function () {
        modalTitle.textContent = "New Task";
        form.action = "TodoList";
        form.method = "post";

        actionInput.value = "";
        titleInput.value = "";
        descriptionInput.value = "";
        statusSelect.value = "PENDING";
        startDateInput.value = "";
        targetDateInput.value = "";
    });
});

function save() {
    event.preventDefault();
    const modalId = "taskModal";
    const form = document.getElementById('taskModalForm');

    const formData = new FormData(form);
    let jsonObject = {};
    let isCreating = false;
    formData.forEach((value, key) => {
            if (key === "id" && !value.trim()){
                isCreating = true;
                return;
            }
            jsonObject[key] = value;
    });

    const url = form.action;
    const method = form.querySelector("#formMethod").value || form.method;
    delete jsonObject["_method"];

    fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(jsonObject),
    })
    .then(response => response.json())
    .then(task => {
        const modal = document.getElementById(modalId);
        const modalInstance = bootstrap.Modal.getInstance(modal);
        modalInstance.hide();

        if(isCreating){
            openToast('Task ' + task.title + ' created successfully!', 'success');
        }
        else{
            openToast('Task ' + task.title + ' updated successfully!', 'success');
        }
        updateTable(task);
    })
    .catch(error => {
        console.error("Error:", error);
        if(isCreating){
            openToast('Error creating task', 'danger');
        }
        else{
            openToast('Error updating task', 'danger');
        }
    });
}

function updateTable(task) {

    const tbody = document.querySelector("table tbody");

    const selector = "tr[data-task-id='" + task.id + "']";
    const row = tbody.querySelector(selector);

    if (row) {
        row.querySelector("td:nth-child(1)").textContent = task.id;
        row.querySelector("td:nth-child(2)").textContent = task.title;
        row.querySelector("td:nth-child(3)").textContent = task.description;
        row.querySelector("td:nth-child(4)").textContent = task.status;
        row.querySelector("td:nth-child(5)").textContent = task.startDate;
        row.querySelector("td:nth-child(6)").textContent = task.targetDate;

        const editButton = row.querySelector(".edit-task-btn");
        editButton.setAttribute("data-title", task.title);
        editButton.setAttribute("data-description", task.description);
        editButton.setAttribute("data-status", task.status);
        editButton.setAttribute("data-startdate", task.startDate);
        editButton.setAttribute("data-targetdate", task.targetDate);
    } else {
        const table = document.getElementById("taskTable");
        const rows = tbody.children.length;

        const pageSize = parseInt(table.dataset.pageSize, 10);
        const totalPages = parseInt(table.dataset.totalPages, 10);
        const currentPage = parseInt(table.dataset.currentPage, 10);

        if (currentPage !== totalPages) {
            return;
        }

        if (rows >= pageSize) {
            location.reload();
            return;
        }

        const newRow = document.createElement("tr");
        newRow.setAttribute("data-task-id", task.id);
        newRow.className = "align-middle";

        const fields = ['id',"title", "description", "status", "startDate", "targetDate"];
        fields.forEach(field => {
            const cell = document.createElement("td");
            cell.textContent = task[field];
            newRow.appendChild(cell);
        });

        const actionsCell = document.createElement("td");
        actionsCell.innerHTML = `
            <button type="button" class="btn btn-primary mb-2 edit-task-btn"
                    data-bs-toggle="modal" data-bs-target="#taskModal"
                    data-id="${task.id}" data-title="${task.title}"
                    data-description="${task.description}" data-status="${task.status}"
                    data-startdate="${task.startDate}" data-targetdate="${task.targetDate}">
                <i class="bi bi-pencil me-2"></i>Edit
            </button>
            <button type="button" class="btn btn-success mb-2" onclick="completeTask(${task.id})">
                <i class="bi bi-check-lg me-2"></i>Complete
            </button>
            <button type="button" class="btn btn-danger mb-2" onclick="deleteTask(${task.id})">
                <i class="bi bi-trash me-2"></i>Delete
            </button>
        `;
        newRow.appendChild(actionsCell);

        tbody.appendChild(newRow);
    }
}

function completeTask(id) {
    fetch(urls.api + "?id=" + id, {
         method: 'PATCH',
         headers: {
             'Content-Type': 'application/x-www-form-urlencoded',
         },
     })
     .then(response => {
         if (response.ok) {
             return response.json();
         } else {
             throw new Error('Error updating task.');
         }
     })
     .then(task => {
         updateTable(task);
         openToast('Task ' + task.title + ' completed successfully!', 'success');
     })
     .catch(error => {
         console.error(error);
         openToast("Error completing task", "danger");
     });
 }



function deleteTask(id) {
    fetch(urls.api + "?id=" + id, {
        method: 'DELETE',
    })
    .then(response => {
        if (response.ok) return response.json();
        else throw new Error('Error deleting task.');
    })
    .then(data => {
        const row = document.querySelector("tr[data-task-id='" + id + "']");
        if (row) {
            row.remove();
            openToast('Task ' + id + ' deleted successfully!', 'success');
        }
    })
    .catch(error => {
        console.error("Error:", error);
        openToast("Error deleting task", "danger");
    });
}

function openToast(message, type = "success") {
    const toastElement = document.getElementById('liveToast')
    toastElement.classList.remove("bg-success", "bg-danger");
    toastElement.classList.add(type === "success" ? "bg-success" : "bg-danger");
    toastElement.querySelector(".toast-body").textContent = message;
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastElement)
    toastBootstrap.show()
}

$(document).ready(function () {
    $("#taskModalForm").validate({
        rules: {
            title: {
                required: true,
                minlength: 3
            },
            description: {
                required: true,
                minlength: 5
            },
            startDate: {
                required: true,
                date: true
            },
            targetDate: {
                required: true,
                date: true,
                minDate: "#startDate"
            }
        },
        messages: {
            title: {
                required: "Title is required.",
                minlength: "Title must be at least 3 characters long."
            },
            description: {
                required: "Description is required.",
                minlength: "Description must be at least 5 characters long."
            },
            startDate: {
                required: "Start date is required.",
                date: "Invalid date format."
            },
            targetDate: {
                required: "Target date is required.",
                date: "Invalid date format.",
                minDate: "Target date must be after start date."
            }
        },
        errorPlacement: function (error, element) {
            error.addClass("error-message");
            error.insertAfter(element);
        },
        highlight: function (element) {
            $(element).addClass("is-invalid");
        },
        unhighlight: function (element) {
            $(element).removeClass("is-invalid");
        },
        submitHandler: function (form) {
            save();
        }
    });

    $.validator.addMethod("minDate", function(value, element, param) {
        let startDateValue = $(param).val();
        if (!startDateValue) return false;
        return new Date(value) >= new Date(startDateValue);
    }, "Target date must be after start date.");
});
