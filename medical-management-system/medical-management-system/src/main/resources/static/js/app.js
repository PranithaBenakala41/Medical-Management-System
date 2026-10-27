document.addEventListener("DOMContentLoaded", function () {
console.log("Medical Management System loaded successfully.");

// Prevent appointment dates in the past.
const appointmentDate = document.getElementById("appointmentDate");

if (appointmentDate) {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0");
    const day = String(today.getDate()).padStart(2, "0");

    appointmentDate.min = ${year}-${month}-${day};
}

});