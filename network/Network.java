package network;

import network.linear_algebra.*;
import java.util.Random;

public class Network {
  private int[] layers;
  private int layerCount;

  private Matrix[] weights;
  private Vector[] biases;

  private double learnRate;
  private double miniBatchSize;

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

  //sgd algorithm for training
  public void stochasticGradientDescent() {

  }

  //aux methods ..............

  public static double sigmoid(double x) {
    return 1d/(1d + Math.exp(-x));
  }

  public static Vector sigmoid(Vector x) {
    for (int i = 1; i <= x.m; i++) {
      x.set(i , sigmoid(x.get(i)));
    }
    return x;
  }
}
