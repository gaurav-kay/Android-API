from flask import Flask, request, jsonify

app = Flask(__name__)


@app.route('/')
def post_request():
    try:
        with open('op.txt', 'a') as f:
            f.write(request)
            f.write('\n')
        print("hey, request is: ", request)
    except Exception:
        pass
    try:
        print(request.files['image'])
    except Exception:
        print("no file")
    try:
        print(request.json['image'])
    except Exception:
        pass
    try:
        print(request['image'])
    except Exception:
        pass
    return jsonify({'message': "Hey i gotchu"})


if __name__ == '__main__':
    app.run()
