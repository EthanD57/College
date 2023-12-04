import json


class Home:
    def __init__(self):
        self._address = str
        self._smart_devices = list

    def add_device(self, device: 'Home.SmartDevice'):
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
        def __init__(self, name=None, manufacturer=None):
            if name is None and manufacturer is None:
                print("Name and manufacturer are required parameters")
            elif name is None:
                print("Name is a required parameter")
            elif manufacturer is None:
                print("Manufacturer is a required parameter")
            self._name = str(name)
            self._manufacturer = str(manufacturer)

        def to_JSON(self):
            return json.dumps(self)
        
        def get_name(self):
            return self._name

        def get_manufacturer(self):
            return self._manufacturer
        
        def set_name(self, name: str):
            self._name = name

        def set_manufacturer(self, manufacturer: str):
            self._manufacturer = manufacturer

class LightBulb(Home.SmartDevice):
    def __init__(self, manufacturer, name, brightness=None):
        super().__init__(name, manufacturer)
        if brightness is None:
            print("Brightness is a required parameter")
        else:
            self._brightness = brightness
            self.set_name(name)
            self.set_manufacturer(manufacturer)

    def to_JSON(self):
        return json.dumps(self)

    def adjust_brightness(self, brightness: float):
        self._brightness = brightness

    def get_brightness(self):
        return self._brightness
        