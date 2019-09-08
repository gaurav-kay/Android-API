from flask import Flask, request, jsonify

app = Flask(__name__)


@app.route('/', methods=['GET', 'POST'])
def post_request():
    print(request.files['image'])
    return jsonify({'message': "Hey i gotchu"})


if __name__ == '__main__':
    app.run(debug=True, port=5000)
