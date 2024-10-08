function createEngine(horsePower, CC, gears) {
    return {
        horsePower,
        CC,
        gears
    };
}

function createInventory() {
    carInventory = ["Ford, 33000, 35, 6, 2000",
                    "Toyota, 23000, 40, 6, 2100",
                    "Mitsubishi, 44000, 45, 6, 2200",
                    "Nissan, 21000, 37, 6, 2300",
                    "GM, 25000, 39, 6, 2400",
                    "VW, 42000, 25, 6, 2500"];
    carList = [];
    for (let i = 0; i < carInventory.length; i++) {
        carList.push(createCar(carInventory[i]));
    }
    return carList;
}

function printInventory(inventory) {
    for (let i = 0; i < inventory.length; i++) {
        inventory[i].printInfo();
    }
}

function createCar(carInfo) {
    carInfo = carInfo.split(',');
    let Brand = carInfo[0];
    let Price = Number(carInfo[1]);
    let Engine = createEngine(Number(carInfo[2]), Number(carInfo[3]), Number(carInfo[4]));
    return {
        Brand: Brand,
        Price: Price,
        Engine: Engine,
        printInfo: function() {
            console.log("==== Car Info ====");
            console.log(`Brand: ${this.Brand}`);
            console.log(`Price: $${this.Price}`);
            console.log(`Engine Features: ${this.Engine.horsePower} Horsepower, ${this.Engine.CC} CC, ${this.Engine.gears} Gears\n`);
        }
    };
}

function main(){
    printInventory(createInventory());
    }