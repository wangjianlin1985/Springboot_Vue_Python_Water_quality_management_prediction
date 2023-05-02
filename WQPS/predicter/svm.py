from sklearn.svm import SVC


def train(x, y):
    model = SVC(kernel='rbf')
    model.fit(x, y)
    return model

