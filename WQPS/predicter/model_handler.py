from django.conf import settings
from sklearn.externals import joblib
from .models import Model
from keras import models
import os


def load_model(model_info, model_id):
    model_path = model_info["path"]
    method = model_info["method"]
    if method == "LSTM":
        model_name = str(model_id) + '.h5'
        model = models.load_model(os.path.join(model_path, model_name))
    else:
        model_name = str(model_id) + '.model'
        model = joblib.load(os.path.join(model_path, model_name))
    return model


def load_vote_model(model_info, model_id):
    model_path = model_info["path"]
    svm_model_name = str(model_id) + '_' + 'SVM.model'
    rvm_model_name = str(model_id) + '_' + 'RVM.model'
    bp_model_name = str(model_id) + '_' + 'BP.model'
    adaboost_model_name = str(model_id) + '_' + 'Adaboost.model'
    svm_model = joblib.load(os.path.join(model_path, svm_model_name))
    rvm_model = joblib.load(os.path.join(model_path, rvm_model_name))
    bp_model = joblib.load(os.path.join(model_path, bp_model_name))
    adaboost_model = joblib.load(os.path.join(model_path, adaboost_model_name))
    return svm_model, rvm_model, bp_model, adaboost_model


def save_model(model, model_info, model_id):
    obj = model_info["obj"]
    method = model_info["method"]
    model_path = model_info["path"]
    if not os.path.exists(model_path):
        os.makedirs(model_path)
    if method == "LSTM":
        model_name = str(model_id) + '.h5'
        model.save(os.path.join(model_path, model_name))
    else:
        model_name = str(model_id) + '.model'
        joblib.dump(model, os.path.join(model_path, model_name))


def save_vote_model(svm_model, rvm_model, bp_model, adaboost_model, model_info, model_id):
    obj = model_info["obj"]
    method = model_info["method"]
    model_path = model_info["path"]
    if not os.path.exists(model_path):
        os.makedirs(model_path)
    svm_model_name = str(model_id) + '_' + 'SVM.model'
    rvm_model_name = str(model_id) + '_' + 'RVM.model'
    bp_model_name = str(model_id) + '_' + 'BP.model'
    adaboost_model_name = str(model_id) + '_' + 'Adaboost.model'
    joblib.dump(svm_model, os.path.join(model_path, svm_model_name))
    joblib.dump(rvm_model, os.path.join(model_path, rvm_model_name))
    joblib.dump(bp_model, os.path.join(model_path, bp_model_name))
    joblib.dump(adaboost_model, os.path.join(model_path, adaboost_model_name))


def get_model_by_id(model_id):
    return Model.objects.get(id=model_id)


def get_model_info(model_id):
    model_entity = get_model_by_id(model_id)
    obj = model_entity.target
    method = model_entity.name.split("_", maxsplit=1)[1]
    model_path = os.path.join(settings.BASE_DIR, 'Model', method, obj)
    return {"obj": obj, "method": method, "path": model_path}





