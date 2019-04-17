from flask_restful import Api, Resource, request
from flask import Flask

app = Flask(__name__)
api = Api(app)


class Hello(Resource):

    def get(self):
        return {
            'hello': 'world'
        }

    def post(self):
        x = request.args
        print(x)


api.add_resource(Hello, '/')

if __name__ == '__main__':
    app.run(port=8000, debug=True)
