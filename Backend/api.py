from flask import Flask, request, jsonify

app = Flask(__name__)


@app.route('/')
def post_request():
    try:
        print("Request is", request)
    except Exception:
        print("request print fail")
    try:
        print("request.files['images']", request.files['image'])
    except Exception:
        print("no file")
    try:
        print("request.json['image']", request.json['image'])
    except Exception:
        print("request.json['image']", "no json image")
    try:
        print("request['image']", request['image'])
    except Exception:
        print("request['image']", "fail no 'image'")

    return jsonify({'message': "Hey i gotchu"})


if __name__ == '__main__':
    app.run()
