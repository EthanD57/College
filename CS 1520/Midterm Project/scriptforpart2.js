function updateCarColorOptions() {
    const carColorSelect = document.getElementById('carColorSelect');
    carColorSelect.innerHTML = ''; // Clear existing options

    const carSelectValue = document.getElementById('carSelect').value;

    if (carSelectValue == '1') {
        carColorSelect.innerHTML = `
            <option value="1">Red</option>
            <option value="2">White</option>
            <option value="3">Yellow</option>
        `;
    } else if (carSelectValue == '2') {
        carColorSelect.innerHTML = `
            <option value="1">Blue</option>
            <option value="2">Green</option>
            <option value="3">Yellow</option>
        `;
    } else if (carSelectValue == '3') {
        carColorSelect.innerHTML = `
            <option value="1">Black</option>
            <option value="2">Silver</option>
            <option value="3">White</option>
        `;
    }
}

document.getElementById('carSelect').addEventListener('change', updateCarColorOptions);