from . import data_processor
from . import model_handler
from . import model_trainer
from . import model_tester
from . import model_predicter
import numpy as np

def train_model(model_id, loop=3):
    model_info = model_handler.get_model_info(model_id)
    obj = model_info["obj"]
    method = model_info["method"]
    if method == "Optimization":
       return train_vote_model(model_id, model_info)
    lstm_mode = False
    if method == "LSTM":
        lstm_mode = True
    raw_data = data_processor.load_all_data()
    mean_and_std = data_processor.get_mean_and_std(raw_data, obj)
    data = data_processor.standardize(raw_data)
    sets = data_processor.generate_sets(data, obj)
    training_set = sets['training_set']
    test_set = sets['test_set']
    model = model_trainer.get_trained_model(method, training_set)
    model_handler.save_model(model, model_info, model_id)
    return model_tester.test_model(model, test_set, mean_and_std, lstm_mode)


def train_vote_model(model_id, model_info):
    obj = model_info["obj"]
    model_path = model_info["path"]
    raw_data = data_processor.load_all_data()
    mean_and_std = data_processor.get_mean_and_std(raw_data, obj)
    data = data_processor.standardize(raw_data)
    sets = data_processor.generate_sets(data, obj, 2)
    training_set = sets['training_set']
    valid_set = sets['valid_set']
    test_set = sets['test_set']
    svm_model, rvm_model, bp_model, adaboost_model = model_trainer.get_trained_vote_model(training_set, valid_set)
    model_handler.save_vote_model(svm_model, rvm_model, bp_model, adaboost_model, model_info, model_id)
    res = model_tester.test_vote_model(svm_model, rvm_model, bp_model, adaboost_model, test_set, mean_and_std)
    print(res)
    return res


def predict_next_month(model_id, data):
    model_info = model_handler.get_model_info(model_id)
    obj = model_info["obj"]
    method = model_info["method"]
    lstm_mode = False
    if method == "LSTM":
        lstm_mode = True
    raw_data = data_processor.load_all_data()
    mean_and_std = data_processor.get_mean_and_std(raw_data, obj)
    x = data_processor.standardize_for_prediction(mean_and_std, np.array(data))
    # x = data_processor.build_x(data, obj)
    if method == "Optimization":
        svm_model, rvm_model, bp_model, adaboost_model = model_handler.load_vote_model(model_info, model_id)
        pred = model_predicter.vote_predict(svm_model, rvm_model, bp_model, adaboost_model, x, mean_and_std)[0]
        return {'pred': pred}
    model = model_handler.load_model(model_info, model_id)
    pred = model_predicter.predict(model, x, mean_and_std, lstm_mode)[0]
    return {'pred': pred}


def get_last_months_data(obj, month_num=5):
    return data_processor.get_last_months_data(obj, month_num)


def save_one_waterquality(data):
    data_processor.save_data(data)


def get_uploaded_waterquality():
    return data_processor.get_uploaded_data()


def delete_uploaded_waterquality(start, nums):
    return data_processor.delete_uploaded_data(start, nums)


def delete(id):
    data_processor.delete(id)



