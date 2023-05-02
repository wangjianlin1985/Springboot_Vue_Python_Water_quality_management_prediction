import numpy as np
from sklearn import preprocessing
from .models import Waterquality


def load_all_data():
    raw_data = Waterquality.objects.order_by('station', 'date')
    data = []
    for waterquality in raw_data:
        month = waterquality.date.month
        data.append([month, waterquality.do, waterquality.nh3n, waterquality.ph])
    return np.array(data)


def select_Y(data, feature_num):
    feature_num = feature_num - 1
    Y = np.ones(len(data)).reshape(len(data), 1)
    for i in range(0, len(data)):
        Y[i] = data[i][feature_num]
    return Y


def select_X(data, feature_lst):
    X = select_Y(data, feature_lst[0])
    for i in range(1, len(feature_lst)):
        X = np.hstack((X, select_Y(data, feature_lst[i])))
    return X


def loop_feature(data, feature_num, loop):
    feature = select_Y(data, feature_num)
    length = len(feature)
    looped_feature = np.ones([loop, length - loop])
    for i in range(0, length - loop):
        for j in range(0, loop):
            looped_feature[j][i] = feature[i + j]
    return looped_feature.T


def rearrange(x, loop):
    length = len(x)
    res = x[:, 0].reshape(length, 1)
    for i in range(0, loop):
        combined = x[:, i].reshape(length, 1)
        for j in range(0, 1):
            combined = np.hstack((combined, x[:, i + loop].reshape(length, 1)))
        res = np.hstack((res, combined))
    return res[:, 1:]


def transform_x(x, loop=3):
    x = rearrange(x, loop)
    x = x.reshape((x.shape[0], loop, 2))
    return x


def generate_sets(data, obj, mode=1, loop=3):
    obj_lst = {'month': 1, 'DO': 2, 'NH3N': 3, 'PH': 4}
    obj_num = obj_lst[obj]
    month = loop_feature(data, obj_lst['month'], loop)
    X = loop_feature(data, obj_num, loop)
    Y = select_Y(data, obj_num)
    length = len(data)
    Y = Y[loop:length, :]
    sets = np.hstack((month, X, Y))
    if mode == 2:
        training_set = build_training_set(sets, 0.6)
        valid_set = build_valid_set(sets, 0.6, 0.2)
        test_set = build_test_set(sets, 0.2)
        return {'training_set': training_set, 'valid_set': valid_set, 'test_set': test_set}
    training_set = build_training_set(sets, 0.8)
    test_set = build_test_set(sets, 0.2)
    return {'training_set': training_set, 'test_set': test_set}


def build_training_set(sets, percent):
    length = int(len(sets) * percent)
    return sets[0:length, :]


def build_test_set(sets, percent):
    length = int(len(sets) * (1 - percent))
    return sets[length:len(sets), :]


def build_valid_set(sets, training_set_percent, valid_set_percent):
    start = int(len(sets) * training_set_percent)
    end = int(len(sets) * (training_set_percent + valid_set_percent))
    return sets[start:end, :]


def standardize(data):
    scaler = preprocessing.StandardScaler().fit(data)
    data = scaler.transform(data)
    return data


def standardize_for_prediction(mean_and_std, data):
    obj_mean = mean_and_std["mean"]
    obj_std = mean_and_std["std"]
    month_mean = 6.5
    month_std = 3.6
    for i in range(0, 3):
        data[0][i] = (data[0][i] - obj_mean) / obj_std
    for i in range(3, 6):
        data[0][i] = (data[0][i] - month_mean) / month_std
    return data


def get_mean_and_std(data, obj):
    obj_lst = {'month': 0, 'DO': 1, 'NH3N': 2, 'PH': 3}
    obj_num = obj_lst[obj]
    scaler = preprocessing.StandardScaler().fit(data)
    return {'mean': scaler.mean_[obj_num], 'std': np.sqrt(scaler.var_)[obj_num]}


def build_x(data, obj):
    obj_lst = {'month': 0, 'DO': 1, 'NH3N': 2, 'PH': 3}
    date = data[:, obj_lst['month']].T
    feature = data[:, obj_lst[obj]].T
    X = np.hstack((date, feature))
    return X.reshape(1, -1)


def get_x(data):
    col_num = np.size(data, 1)
    x = data[:, 0:col_num - 1]
    return x


def get_y(data):
    col_num = np.size(data, 1)
    y = data[:, col_num - 1]
    return y


def get_last_loop_data(data, loop):
    length = np.size(data, 0)
    return data[length - loop:length, :]


def get_last_months_data(obj, month_num):
    obj = str.lower(obj)
    raw_data = Waterquality.objects.order_by('-station', '-date')[:month_num]
    date = raw_data.values('date')
    obj_data = raw_data.values(obj)
    month = []
    data = []
    for i in range(0, len(date)):
        month.append(date[i]['date'].month)
        data.append(obj_data[i][obj])
    return {'month': month, 'data': data}


def save_data(data):
    water = Waterquality(date=data['date'], do=data['DO'],
                         ph=data['PH'], nh3n=data['NH3N'], station=10)
    water.save()


def get_uploaded_data():
    raw_data = Waterquality.objects.filter(station=10).order_by('date')
    return raw_data


def delete(waterquality_id):
    Waterquality.objects.filter(id=waterquality_id).delete()


def delete_uploaded_data(start, nums):
    raw_data = Waterquality.objects.filter(station=10).order_by('date')
    index = 0
    cnt = 0
    flag = False
    for waterquality in raw_data:
        if index == start:
            flag = True
        if flag:
            waterquality.delete()
            cnt = cnt + 1
        index = index + 1
        if cnt == nums:
            break






