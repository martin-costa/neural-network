package neural_network.network;

import neural_network.linear_algebra.*;
import neural_network.data.*;
import neural_network.network.activation_functions.*;

public class Network implements Classifier{
  protected int layerCount;
  protected int[] layers;

  protected Matrix[] weights;
  protected Vector[] biases;

  //initialise the neural network with random weights and biases
  public Network(int... layers) {
    layerCount = layers.length;
    this.layers = layers;

    //initialise biases
    biases = new Vector[layerCount - 1];
    for (int i = 0; i < layerCount - 1; i++) {
      biases[i] = new Vector(layers[i + 1]).fillGaussian(0, 1d/Math.sqrt(layers[i]));
    }

    //initialise weights
    weights = new Matrix[layerCount - 1];
    for (int i = 0; i < layerCount - 1; i++) {
      weights[i] = new Matrix(layers[i + 1], layers[i]).fillGaussian(0, 1d/Math.sqrt(layers[i]));
    }
  }

  //initialise the neural network by loading in weights and biases
  public Network(int[] layers, Matrix[] weights, Vector[] biases) {
    layerCount = layers.length;
    this.layers = layers;
    this.weights = weights;
    this.biases = biases;
  }

  //method that gets output for input x for current weights and biases
  public Vector feedForward(Vector x) {
    Vector a = new Vector(x);
    for (int i = 0; i < layerCount - 1; i++) {
      a = Sigmoid.f(weights[i].mult(a).add(biases[i]));
    }
    return a;
  }

  //return the number of pieces of data in the test data correctly evaluated
  public int evaluate(NumberData testData) {
    int j = 0;
    for (int i = 0; i < testData.size; i++) {
      if (classify(testData.getImage(i)) == testData.getNumber(i)) j++;
    }
    return j;
  }

  //classify this image with current weights and biases
  public int classify(Vector image) {
    return feedForward(image).maxIndex();
  }

  //getters and setters
  public int getLayerCount() {
    return layerCount;
  }
  public int[] getLayers() {
    return layers;
  }
  public Matrix[] getWeights() {
    return weights;
  }
  public Vector[] getBiases() {
    return biases;
  }
}
