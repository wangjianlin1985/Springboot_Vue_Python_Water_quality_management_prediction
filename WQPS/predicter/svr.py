from sklearn.svm import SVR


def train(x, y):
    model = SVR(kernel='rbf')
    model.fit(x, y)
    return model

