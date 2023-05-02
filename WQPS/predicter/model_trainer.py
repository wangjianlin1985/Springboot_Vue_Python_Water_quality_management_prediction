from . import svr
from . import rvr
from . import bp
from . import adaboost
from . import lstm
from . import svm
from . import data_processor
import numpy as np


def get_trained_model(model_name, training_set):
    col_num = np.size(training_set, 1)
    x = training_set[:, 0:col_num - 1]
    y = training_set[:, col_num - 1]
    if model_name == 'SVM':
        return svr.train(x, y)
    if model_name == 'Adaboost':
        return adaboost.train(x, y)
    if model_name == 'RVM':
        return rvr.train(x, y)
    if model_name == 'BP':
        return bp.train(x, y)
    if model_name == 'LSTM':
        return lstm.train(x, y)
    if model_name == 'SVC':
        return svm.train(x, y)
    return svr.train(x, y)


def build_vote_label(pred, Y):
    length = len(Y)
    label = np.ones(length)
    one_pred = pred[:, 0]
    two_pred = pred[:, 1]
    three_pred = pred[:, 2]
    one_err = np.square(one_pred - Y)
    two_err = np.square(two_pred - Y)
    three_err = np.square(three_pred - Y)
    for i in range(0, length):
        lst = [one_err[i], two_err[i], three_err[i]]
        lst.sort()
        if lst[0] == one_err[i]:
            label[i] = 0
        elif lst[0] == two_err[i]:
            label[i] = 1
        else:
            label[i] = 2
    return label.reshape((-1, 1))


def get_trained_vote_model(training_set, valid_set):
    rvm_model = get_trained_model("RVM", training_set)
    bp_model = get_trained_model("BP", training_set)
    adaboost_model = get_trained_model("Adaboost", training_set)

    valid_x = data_processor.get_x(valid_set)
    valid_y = data_processor.get_y(valid_set)
    rvm_valid_pred = rvm_model.predict(valid_x)
    bp_model_pred = bp_model.predict(valid_x)
    adaboost_pred = adaboost_model.predict(valid_x)
    valid_pred = np.vstack((rvm_valid_pred, bp_model_pred, adaboost_pred)).T
    # print(np.shape(valid_pred))
    # print(np.shape(valid_y))
    label = build_vote_label(valid_pred, valid_y)
    svm_model = get_trained_model("SVC", np.hstack((valid_x, label)))
    return svm_model, rvm_model, bp_model, adaboost_model



