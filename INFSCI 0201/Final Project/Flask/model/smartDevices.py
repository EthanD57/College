import json
from abc import ABC, abstractmethod
import random

class Home:
    def __init__(self, address):
        self._address = address
        self._smart_devices = []

    def add_device(self, device):
        self._smart_devices.append(device)

    def print_devices(self):
        for device in self._smart_devices:
            print(device.name)

    def get_device(self, name: str):
        for device in self._smart_devices:
            if device.get_name() == name:
                return device
           
    def get_address(self):
        return self._address
    
class SmartDevice(ABC):
    def __init__(self, name, manufacturer):
        self._name = str(name)
        self._manufacturer = str(manufacturer)

    @abstractmethod
    def to_JSON(self):
        pass
    
    def get_name(self):
        return self._name

    def get_manufacturer(self):
        return self._manufacturer
    
    def set_name(self, name: str):
        self._name = name

    def set_manufacturer(self, manufacturer: str):
        self._manufacturer = manufacturer


class LightBulb(SmartDevice):
    def __init__(self, name, manufacturer, brightness):
        super().__init__(name, manufacturer)
        self._brightness = int(brightness)

    def to_JSON(self):
        return json.dumps(self, default=lambda x: x.__dict__, sort_keys=True, indent=4)

    def adjust_brightness(self, brightness: float):
        self._brightness = brightness
        print("Brightness is set to " + str(brightness))

    def get_brightness(self):
        return self._brightness
    
    def changeName(self, name: str):
        self._name = name
        print("Name is set to " + name)

    def changeManufacturer(self, manufacturer: str):
        self._manufacturer = manufacturer
        print("Manufacturer is set to " + manufacturer)

    def is_on(self):
        return self._name + " is on." if self._brightness > 0 else self._name + " is off."
    
    def turn_off(self):
        self._brightness = 0
        print("Light is turned off")
    
    def turn_on(self):
        self._brightness = 100
        print("Light is turned on")
    

class Thermostat(SmartDevice):
    def __init__(self, name, manufacturer, temperature):
        super().__init__(name, manufacturer)
        self._temperature = int(temperature)

    def to_JSON(self):
        return json.dumps(self, default=lambda x: x.__dict__, sort_keys=True, indent=4)

    def adjust_temp(self, temperature: int):
        self._temperature = temperature
        print("Temperature is set to " + str(temperature))

    def get_temperature(self):
        return self._temperature
    
    def changeName(self, name: str):
        self._name = name
        print("Name is set to " + name)

    def changeManufacturer(self, manufacturer: str):
        self._manufacturer = manufacturer
        print("Manufacturer is set to " + manufacturer)
    

class SmartVacuum(SmartDevice):
    def __init__(self, name, manufacturer, cleaning):
        super().__init__(name, manufacturer)
        self._cleaning = bool(cleaning)
        self._battery = random.randint(0, 100)

    def to_JSON(self):
        return json.dumps(self, default=lambda x: x.__dict__, sort_keys=True, indent=4)
    
    def changeName(self, name: str):
        self._name = name
        print("Name is set to " + name)

    def changeManufacturer(self, manufacturer: str):
        self._manufacturer = manufacturer
        print("Manufacturer is set to " + manufacturer)

    def turn_on(self):
        self._cleaning = True
        print("Vacuum is turned on")

    def turn_off(self):
        self._cleaning = False
        print("Vacuum is turned off")

    def is_on(self):
        return self._name + " is on." if self._on else self._name + " is off."
    
    def get_battery(self):
        return self._battery


class SmartPlug(SmartDevice):
    def __init__(self, name, manufacturer, state):
        super().__init__(name, manufacturer)
        self._state = bool(state)

    def to_JSON(self):
        return json.dumps(self, default=lambda x: x.__dict__, sort_keys=True, indent=4)
    
    def changeName(self, name: str):
        self._name = name
        print("Name is set to " + name)

    def changeManufacturer(self, manufacturer: str):
        self._manufacturer = manufacturer
        print("Manufacturer is set to " + manufacturer)

    def turn_on(self):
        self._state = True
        print("Plug is turned on")
    
    def turn_off(self):
        self._state = False
        print("Plug is turned off")
    
    def is_on(self):
        return self._name + " is on." if self._state else self._name + " is off."

class SmartLock(SmartDevice):
    def __init__(self, name, manufacturer, locked):
        super().__init__(name, manufacturer)
        self._lock = bool(locked)

    def to_JSON(self):
        return json.dumps(self, default=lambda x: x.__dict__, sort_keys=True, indent=4)
    
    def changeName(self, name: str):
        self._name = name
        print("Name is set to " + name)

    def changeManufacturer(self, manufacturer: str):
        self._manufacturer = manufacturer
        print("Manufacturer is set to " + manufacturer)

    def lock(self):
        self._lock = True
        print("Door is locked")

    def unlock(self):
        self._lock = False
        print("Door is unlocked")

    def is_locked(self):
        return self._name + " is locked." if self._lock else self._name + " is unlocked."
    
