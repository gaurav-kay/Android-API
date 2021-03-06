from flask import Flask, jsonify, request, send_file
import cv2

app = Flask(__name__)
image_number = 0


@app.route('/post', methods=['POST', 'GET'])
def index():
    global image_number
    image_number += 1

    img = request.files["image"]

    img_dir = "./uploads/image_" + str(image_number) + "." + img.filename.split('.')[-1]
    img.save(img_dir)

    bw = cv2.imread(img_dir, cv2.IMREAD_GRAYSCALE)

    bw_dir = "./processed/black_and_white_" + str(image_number) + "." + img.filename.split('.')[-1]
    cv2.imwrite(bw_dir, bw)

    return send_file(bw_dir, mimetype='image/gif')


@app.route('/get', methods=['GET'])
def java():
    with open('./processed/black_and_white_1.jpeg', 'rb') as f:
        return f.read()

if __name__ == '__main__':
    app.run(debug=True, port=8000)

# file_str = file.read()
# np_image = np.fromstring(file_str, np.uint8)
#
# black_and_white = cv2.imdecode(np_image, cv2.IMREAD_GRAYSCALE)
#
# img = Image.fromarray(black_and_white)
# print(type(img))
# print(img)
#
# return send_file()
