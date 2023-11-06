from flask import Flask, jsonify

app = Flask(__name__)

@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"

@app.route('/whoareyou/<parameter>')
def who_are_you(parameter):
    return jsonify({"name": "Ethan Defilippi", "email": "ECD57@pitt.edu", "Python Experience": "1 year", "Flask Cool?": True, "Parameter": parameter})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=9500, debug=True)