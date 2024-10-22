class Car {
    constructor(type, color, description, price, images) {
        this.type = type;
        this.color = color;
        this.description = description;
        this.price = price;
        this.images = images;
        this.imageIndex = 0;
    }

    updatePrice() {
        const insuranceValue = document.querySelector('input[name="insurance"]:checked').value;
        if (this.type === 'Aston Martin') {
            this.price = 2000000 * insuranceValue;
        } else if (this.type === 'Porsche') {
            this.price = 130000 * insuranceValue;
        } else if (this.type === 'Lotus') {
            this.price = 115000 * insuranceValue;
        }
        updateDescription()
    }

    updateType(type) {
        this.type = type;
    }

    updateColor(color) {
        this.color = color;
    }
}

//Desciption template
descriptionTemplate = "The %make% is a luxury car that is known for its speed and style. It is a great car for those who want to make a statement. The %make% comes in a variety of colors including %color%. The price of the %make% is $%price% and comes with %insurance% plan.";


//Create our default car
const car = new Car('Aston Martin', 'Cumberland Grey', descriptionTemplate, 100000*1.3, ['Images\Aston-CumberlandGrey.png', 'Images\Aston-OberonBlack.png', 'Images\Aston-OnyxBlack.png']);

document.addEventListener('DOMContentLoaded', function() {
    updateDescription();
});

//Update the description of the car using the template
function updateDescription() {
    const insuranceValue = document.querySelector('input[name="insurance"]:checked').value;
    let insurance = insuranceValue == 1 ? 'no insurance' : 'a 3-year insurance';
    const description = descriptionTemplate
        .replace(/%make%/g, car.type)
        .replace(/%color%/g, car.color.toLowerCase())
        .replace(/%price%/g, car.price.toLocaleString())
        .replace(/%insurance%/g, insurance);
        const carDescriptionText = document.getElementById('carDescriptionText');    
        carDescriptionText.value = description;
}

//Updates the SHOWN color options based on the selected car make and sets the default color
function updateCarColorOptions() {
    const carColorSelect = document.getElementById('carColorSelect');
    carColorSelect.innerHTML = ''; // Clear existing options

    const carSelectValue = document.getElementById('carSelect').value;

    if (carSelectValue == '1') {
        carColorSelect.innerHTML = `
            <option value="1">Cumberland Grey</option>
            <option value="2">Oberon Black</option>
            <option value="3">Onyx Black</option>
        `;
        car.updateColor('Cumberland Grey'); // Set default color for Aston Martin
    } else if (carSelectValue == '2') {
        carColorSelect.innerHTML = `
            <option value="1">White</option>
            <option value="2">Black</option>
            <option value="3">Red</option>
        `;
        car.updateColor('White'); // Set default color for Porsche
    } else if (carSelectValue == '3') {
        carColorSelect.innerHTML = `
            <option value="1">Black</option>
            <option value="2">Green</option>
            <option value="3">Yellow</option>
        `;
        car.updateColor('Black'); // Set default color for Lotus
    }
    car.imageIndex = 0;
    updateDescription();
}

//Update the image of the car based on the selected make and color
function updateImages() {
    const carImageContainer = document.getElementById('carPicture');
    let img = carImageContainer.querySelector('img');
    img.src = car.images[car.imageIndex];
    img.alt = `${car.type} ${car.color}`;
}

//Update the make of the car based on the selected make
document.getElementById('carSelect').addEventListener('change', function() {
    const carSelectValue = this.value;
    if (carSelectValue == '1') {
        car.updateType('Aston Martin');
        car.imageIndex = 0;
        car.images = ['Images/Aston-CumberlandGrey.png', 'Images/Aston-OberonBlack.png', 'Images/Aston-OnyxBlack.png'];
    } else if (carSelectValue == '2') {
        car.updateType('Porsche');
        car.imageIndex = 0;
        car.images = ['Images/Porsche-White.png', 'Images/Porsche-Black.png', 'Images/Porsche-Red.png'];
    } else if (carSelectValue == '3') {
        car.updateType('Lotus');
        car.imageIndex = 0;
        car.images = ['Images/Lotus-Black.png', 'Images/Lotus-Green.png', 'Images/Lotus-Yellow.png'];
    }
    updateCarColorOptions();
    updateImages();
    car.updatePrice();
    updateDescription();
});

//Update the color of the car based on the selected color
document.getElementById('carColorSelect').addEventListener('change', function() {
    const carColorSelect = document.getElementById('carColorSelect');
    const carSelectValue = document.getElementById('carSelect').value;

    //Checks the selected car make and updates the color based off available colors for each make
    if (carSelectValue == '1') {
        if (carColorSelect.value == '1') {
            car.updateColor('Cumberland Grey');
            car.imageIndex = 0;
        } else if (carColorSelect.value == '2') {
            car.updateColor('Oberon Black');
            car.imageIndex = 1;
        } else if (carColorSelect.value == '3') {
            car.updateColor('Onyx Black');
            car.imageIndex = 2;
        }
    } else if (carSelectValue == '2') {
        if (carColorSelect.value == '1') {
            car.updateColor('White');
            car.imageIndex = 0;
        } else if (carColorSelect.value == '2') {
            car.updateColor('Black');
            car.imageIndex = 1;
        } else if (carColorSelect.value == '3') {
            car.updateColor('Red');
            car.imageIndex = 2;
        }
    } else if (carSelectValue == '3') {
        if (carColorSelect.value == '1') {
            car.updateColor('Black');
            car.imageIndex = 0;
        } else if (carColorSelect.value == '2') {
            car.updateColor('Green');
            car.imageIndex = 1;
        } else if (carColorSelect.value == '3') {
            car.updateColor('Yellow');
            car.imageIndex = 2;
        }
    }
    updateDescription();
    updateImages();
});

document.getElementById('insurancePlan').addEventListener('change', function() {
    car.updatePrice();  //Need this to update the price of the car because the insurance plan has changed. This updates the description as well.
});

