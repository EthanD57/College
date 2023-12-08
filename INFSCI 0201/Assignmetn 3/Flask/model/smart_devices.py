import json
from abc import ABC, abstractmethod

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
            if device.name == name:
                return device
           
    def get_address(self):
        return self._address
    
class SmartDevice:
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