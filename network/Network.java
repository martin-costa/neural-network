package network;

import network.linear_algebra.*;
import network.data.*;
import java.util.Random;

public class Network {
  private int[] layers;
  private int layerCount;

  private Matrix[] weights;
  private Vector[] biases;

  //initialise the neural network
  public Network(int... layers) {

    this.layers = layers;
    layerCount = layers.length;

    Random generator = new Random();

    //initialise biases
    biases = new Vector[layerCount - 1];
    for (int i = 0; i < layerCount - 1; i++) {
      biases[i] = new Vector(layers[i + 1]);
    }

    //initialise weights
    weights = new Matrix[layerCount - 1];
    for (int i = 0; i < layerCount - 1; i++) {
      weights[i] = new Matrix(layers[i + 1], layers[i]);
    }
  }

  //method that gets output for input x for current weights and biases
  public Vector feedForward(Vector x) {
    Vector a = new Vector(x);
    for (int i = 0; i < layerCount - 1; i++) {
      a = sigmoid(weights[i].mult(a).add(biases[i]));
    }
    return a;
  }

  //learning algorithms here

  //sgd algorithm for training the neural network
  public void stochasticGradientDescent(int epochs, int miniBatches, int learnRate, NumberData trainingData, NumberData testData) {

  }

  //applies sgd using backprop to a mini batch
  public void updateMiniBatch(NumberData miniBatch) {

  }

  //backprop
  public void backpropagation() {

  }

  public int evaluate(NumberData testData) {
    int j = 0;
    for (int i = 0; i < testData.size; i++) {
      if (feedForward(testData.images[i]).maxIndex() == testData.numbers[i]) j++;
    }
    return j;
  }

  //aux methods ..............

  public static double sigmoid(double x) {
    return 1d/(1d + Math.exp(-x));
  }

  public static Vector sigmoid(Vector x) {
    for (int i = 0; i < x.m; i++) {
      x.set(i , sigmoid(x.get(i)));
    }
    return x;
  }
}
