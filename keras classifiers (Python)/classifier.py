import tensorflow as tf
import numpy as np

import sys

# add display directory
sys.path.append('..')
from display import *

def classify(pixels, get_err=False):
    if get_err==False: return np.argmax(model(pixels))

    class_vec = model(pixels).numpy().reshape(10,)
    classification = np.argmax(class_vec)

    error = 0.0
    for i in range(10):
        if i != classification:
            error += class_vec[i]
        else:
            error += 1 - class_vec[i]
    return (classification, error)

if __name__ == '__main__':

    #### __ load and test the model __ ####
    model = tf.keras.models.load_model('./models/mnist_cnn_74.h5')

    # load the images
    (_, _), (test_images, test_labels) = tf.keras.datasets.mnist.load_data()

    # get the appropriate input shape
    shape = tuple(i for i in model.layers[0].input_shape if i is not None)

    # reshape images and normalize pixel values to [0, 1]
    test_images = test_images.reshape((10000,) + shape)/255.0

    # get accuracy on test data
    print('accuracy on test data =====> ', model.evaluate(test_images, test_labels, verbose=0)[1])

    #### __ use the model __ ####
    drawing_display = DrawingDisplay(28*6, 3, "MNIST classifier - drawing panel")
    viewing_display = ViewingDisplay(28, 18, "MNIST classifier - image preprocessing panel")

    sys.stdout.write('\n')

    while drawing_display.is_running() and viewing_display.is_running():

        drawing_display.update()

        pixels = drawing_display.get_pixels().preprocess(28)

        viewing_display.set_display(pixels)
        viewing_display.update()

        # set the pixels to the correct data format
        pixels = np.transpose(pixels.get_ndarr().reshape((1,) + shape))

        # get the classification form the model
        classification, error = classify(pixels, get_err=True)

        sys.stdout.write('\r')
        sys.stdout.flush()
        sys.stdout.write('Classification ===> << ' + str(classification) + ' >>  ' + str(error))

    if drawing_display.is_running(): drawing_display.close()
    if viewing_display.is_running(): viewing_display.close()
