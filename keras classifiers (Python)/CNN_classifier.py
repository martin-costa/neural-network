from __future__ import absolute_import, division, print_function, unicode_literals

import tensorflow as tf

# import everything needed for the model
from tensorflow.keras import datasets, layers, models, optimizers
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Conv2D, Dense, Flatten, Dropout, BatchNormalization

# load and reshape the data
(train_images, train_labels), (test_images, test_labels) = datasets.mnist.load_data()

train_images = train_images.reshape((60000, 28, 28, 1))
test_images = test_images.reshape((10000, 28, 28, 1))

# normalize pixel values to [0, 1]
train_images, test_images = train_images/255.0, test_images/255.0

# hyper-parameters
EPOCHS = 150
BATCH_SIZE = 64

# Create the model
model = Sequential()

model.add(Conv2D(32, (3, 3), activation='relu', input_shape=(28, 28, 1)))

model.add(BatchNormalization())
model.add(Conv2D(32, (3, 3), activation='relu'))

model.add(BatchNormalization())
model.add(Conv2D(32, (5, 5), strides=2, padding='same', activation='relu'))

model.add(BatchNormalization())
model.add(Dropout(0.4))
model.add(Conv2D(64, (3, 3), activation='relu'))

model.add(BatchNormalization())
model.add(Conv2D(64, (3, 3), activation='relu'))

model.add(BatchNormalization())
model.add(Conv2D(64, (5, 5), strides=2, padding='same', activation='relu'))

model.add(BatchNormalization())
model.add(Dropout(0.4))
model.add(Conv2D(128, (4, 4), activation='relu'))

model.add(BatchNormalization())
model.add(Flatten())
model.add(Dropout(0.4))
model.add(Dense(10, activation='softmax'))

# compile the model
model.compile(optimizer='adam', loss="sparse_categorical_crossentropy", metrics=["accuracy"])

# output a summary of the model
model.summary()

# expand the data set
data_expand = tf.keras.preprocessing.image.ImageDataGenerator(
        rotation_range=10,
        zoom_range=0.10,
        width_shift_range=0.1,
        height_shift_range=0.1)

# saves the model that gets the best validation accuracy
checkpoints = tf.keras.callbacks.ModelCheckpoint(
                                filepath='./models/mnist_cnn_3.h5',
                                #monitor='val_acc',
                                verbose=1,
                                save_best_only=False)
                                #mode='min')

# define how learn rate will change
learn_rate = tf.keras.callbacks.LearningRateScheduler(lambda lr: 0.001 * 0.97 ** lr)

# train the convolutional neural network
model.fit_generator(
        data_expand.flow(train_images, train_labels, batch_size=BATCH_SIZE),
        steps_per_epoch=train_images.shape[0]//BATCH_SIZE,
#model.fit(
        #train_images, train_labels, batch_size=BATCH_SIZE,
        epochs=EPOCHS,
        validation_data=(test_images, test_labels),
        callbacks=[checkpoints])

# get accuracy on test data
print('accuracy on test data: ', model.evaluate(test_images, test_labels, verbose=0)[1])
