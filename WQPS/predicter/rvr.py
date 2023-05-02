from skrvm import RVR


def train(x, y):
    model = RVR(kernel='rbf')
    model.fit(x, y)
    return model

