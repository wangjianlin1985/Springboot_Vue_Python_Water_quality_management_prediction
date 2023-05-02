import numpy as np
from . import model_predicter
import matplotlib.pyplot as plt

def rmse(pred, real):
    length = len(pred)
    res = 0
    for i in range(0, length):
        res = res + np.square(pred[i] - real[i])
    res = np.sqrt(res/length)
    return np.round(res, 2)


def cor(pred, real):
    return np.round(np.corrcoef(pred, real)[0][1], 2)


def accuracy(obj, pred, real):
    length = len(pred)
    res = 0
    if obj == 'PH':
        limit = 0.3
    if obj == 'DO':
        limit = 1
    if obj == 'CODMn':
        limit = 0.5
    if obj == 'NH3N':
        limit = 0.3
    for i in range(0, length):
        if np.abs(pred[i] - real[i]) <= limit:
            res = res + 1
    return np.round(res/length*100, 2)


def plot(pred, real):
    t = np.arange(0, len(pred))
    plt.plot(t, pred, 'b--', t, real, 'r')
    plt.show()


def test_model(model, test_set, mean_and_std, lstm_mode):
    col_num = np.size(test_set, 1)
    x = test_set[:, 0:col_num - 1]
    y = test_set[:, col_num - 1] * mean_and_std['std'] + mean_and_std['mean']
    pred = model_predicter.predict(model, x, mean_and_std, lstm_mode)
    return {'rmse': rmse(pred, y),
            'pred': pred,
            'real': np.round(y, 2).tolist()}


def test_vote_model(svm_model, rvm_model, bp_model, adaboost_model, test_set, mean_and_std):
    col_num = np.size(test_set, 1)
    x = test_set[:, 0:col_num - 1]
    y = test_set[:, col_num - 1] * mean_and_std['std'] + mean_and_std['mean']
    pred = model_predicter.vote_predict(svm_model, rvm_model, bp_model, adaboost_model, x, mean_and_std)
    return {'rmse': rmse(pred, y),
            'pred': pred,
            'real': np.round(y, 2).tolist()}


