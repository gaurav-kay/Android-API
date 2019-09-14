from PIL import Image
import os
from flask import Flask, request

app = Flask(__name__)
path = './temp.jpeg'


@app.route('/', methods=['GET', 'POST'])
def post_request():
    file = request.files['image']
    file.save(path)

    def generate_and_remove():
        with open(path, 'rb') as f:
            yield from f

        os.remove(path)

    image = Image.open(path)
    black_and_white = image.convert('L')
    black_and_white.save(path)

    response = app.response_class(generate_and_remove(), mimetype='image/gif')
    response.headers.set('Content-Disposition', 'attachment', filename='filename')

    return response


if __name__ == '__main__':
    app.run()
