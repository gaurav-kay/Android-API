import cv2
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

    black_and_white = cv2.imread(path, cv2.IMREAD_GRAYSCALE)
    cv2.imwrite(path, black_and_white)

    response = app.response_class(generate_and_remove(), mimetype='image/gif')
    response.headers.set('Content-Disposition', 'attachment', filename='filename')

    return response


if __name__ == '__main__':
    app.run()
