import json
from flask import Flask, render_template, request
from model.smart_devices import Home, SmartDevice, LightBulb

app = Flask(__name__)

home = Home("123 Main St")

@app.route("/")
def index():
    LightBulb1 = LightBulb("Bulb1", "Phillips", 100)
    for device in home._smart_devices:
        if device._name == LightBulb1._name:
            return render_template("home.html", home=home)
    home.add_device(LightBulb1)
    return render_template("home.html", home=home)


@app.route("/add", methods=["POST"])
def post_bulb():
    attributes = request.get_json()
    Lightbulb2 = LightBulb(attributes['name'], attributes['manufacturer'], attributes['brightness'])
    home.add_device(Lightbulb2)
    outfile = open("DUMP.json", 'w')
    json.dump(Lightbulb2.__dict__, outfile)              
    outfile.close()
    return ("IT'S ADDED")
    
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)