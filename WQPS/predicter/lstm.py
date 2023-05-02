from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM
from . import data_processor


def train(x, y):
    x = data_processor.transform_x(x)
    model = Sequential()
    model.add(LSTM(3, input_shape=(x.shape[1], x.shape[2]),
                   return_sequences=True))
    model.add(LSTM(8, activation='tanh'))
    model.add(Dense(1))
    model.compile(loss='mae', optimizer='adam')
    model.fit(x, y, verbose=1, epochs=70)
    return model


