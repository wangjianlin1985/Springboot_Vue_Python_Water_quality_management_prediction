import os, django
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "WQPS.settings")
django.setup()
from unittest import TestCase
from . import data_processor
from . import service
from . import model_handler
from . import model_trainer
from . import model_tester



class Test(TestCase):
    def test_load_data(self):
        print(data_processor.load_all_data())

    def test_train_model(self):
        print(service.train_model('DO'))

    def test_predict_next_month(self):
        print(service.predict_next_month('DO'))

    def test_get_last_months_data(self):
        print(data_processor.get_last_months_data('DO', 5))

    def test_save_data(self):
        data = {'date': '2019-03-01 00:00:00', 'PH': 7.02, 'DO': 7.02, 'NH3N': 7.02}
        data_processor.save_data(data)

    def test_get_next_month(self):
        print(data_processor.get_next_month_num())

    def test_get_model_by_id(self):
        model = model_handler.get_model_by_id(1)
        print(model.name)

    def test_service_train_model(self):
        print(service.train_model(1))

    def test_predict_next_month(self):
        print(service.predict_next_month(1))

    def test_train_model(self):
        obj = 'DO'
        loop = 3
        method = 'LSTM'
        lstm_mode = False
        if method == 'LSTM':
            lstm_mode = True
        raw_data = data_processor.load_all_data()
        mean_and_std = data_processor.get_mean_and_std(raw_data, obj)
        data = data_processor.standardize(raw_data)
        sets = data_processor.generate_sets(data, obj, loop)
        training_set = sets['training_set']
        test_set = sets['test_set']
        model = model_trainer.get_trained_model(method, training_set)
        print(model_tester.test_model(model, test_set, mean_and_std, lstm_mode))

    def test_train_vote_model(self):
        obj = 'DO'
        service.train_vote_model(obj)
