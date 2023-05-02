from sklearn.neural_network.multilayer_perceptron import MLPRegressor


def train(x, y):
    model = MLPRegressor(hidden_layer_sizes=(12, 6, 3), batch_size=200)
    model.fit(x, y)
    return model

