import tensorflow as tf

from tensorflow.keras import datasets, models, layers, optimizers, losses

# load in the training and test data
(train_images, train_labels), (test_images, test_labels) = datasets.mnist.load_data()

# scale the data
train_images, test_images = train_images/255.0, test_images/255.0

# create the model
model = models.Sequential()

model.add(layers.Flatten(input_shape=(28, 28)))

model.add(layers.Dense(150, activation='relu'))
model.add(layers.Dense(10, activation='softmax'))

model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])

# output a summary of the model
model.summary()

# train the multi-layer perceptron
model.fit(train_images, train_labels, batch_size=10, epochs=50,
          validation_data=(test_images, test_labels))

# get accuracy on test data
print('accuracy on test data: ', model.evaluate(test_images, test_labels, verbose=0)[1])
