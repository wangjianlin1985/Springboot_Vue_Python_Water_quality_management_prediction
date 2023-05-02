from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import AdaBoostRegressor


def train(x, y):
    max_depth = 8
    n_estimators = 400
    model = AdaBoostRegressor(DecisionTreeRegressor(max_depth=max_depth), n_estimators=n_estimators)
    model.fit(x, y)
    return model
