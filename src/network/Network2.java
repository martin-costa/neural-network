package network;

import network.linear_algebra.*;
import network.data.*;

public class Network2 {
  private int[] layers;
  private int layerCount;

  private Matrix[] weights;
  private Vector[] biases;

  //initialise the neural network
  public Network2(int... layers) {

    this.layers = layers;
    layerCount = layers.length;

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
  public void stochasticGradientDescent(int epochs, int miniBatchSize, double learnRate,
    double lambito, NumberData trainingData, NumberData testData) {

    //run each epoch of training
    for (int i = 0; i < epochs; i++) {
      NumberData[] miniBatches = trainingData.randomize().split(miniBatchSize);

      //update mini batches
      for (int j = 0; j < miniBatches.length; j++) {
        updateMiniBatch(miniBatches[j], learnRate, lambito, trainingData.size);
      }

      //test the current weights and biases against the test data
      if (testData != null) {
        System.out.println(evaluate(testData) + "/" + testData.size + " numbers correctly classified");
      }
      System.out.println("Epoch " + i + " complete");
    }
  }

  //applies sgd using backprop to a mini batch
  public void updateMiniBatch(NumberData miniBatch, double learnRate, double lambito, int n) {

    Matrix[] gradw = new Matrix[layerCount - 1];
    Vector[] gradb = new Vector[layerCount - 1];

    Matrix[] dgradw = new Matrix[layerCount - 1];
    Vector[] dgradb = new Vector[layerCount - 1];

    for (int j = 0; j < layerCount - 1; j++) {
      gradw[j] = new Matrix(weights[j].m, weights[j].n, 0);
      gradb[j] = new Vector(biases[j].m, 0);

      dgradw[j] = new Matrix(weights[j].m, weights[j].n, 0);
      dgradb[j] = new Vector(biases[j].m, 0);
    }

    //for each piece of training data
    for (int i = 0; i < miniBatch.size; i++) {

      for (int j = 0; j < layerCount - 1; j++) {
        dgradw[j].fill(0);
        dgradb[j].fill(0);
      }

      backpropagation(dgradw, dgradb, miniBatch.getImage(i), miniBatch.getResult(i));

      for (int j = 0; j < layerCount - 1; j++) {
        gradw[j] = gradw[j].add(dgradw[j]); //<<-- slowness all here!!
        gradb[j] = gradb[j].add(dgradb[j]);
      }
    }

    for (int j = 0; j < layerCount - 1; j++) {
      weights[j] = weights[j].mult(1-learnRate*(lambito/(double)n));
      weights[j] = weights[j].add(gradw[j].mult(-learnRate/(double)miniBatch.size));
      biases[j] = biases[j].add(gradb[j].mult(-learnRate/(double)miniBatch.size));
    }
  }

  //backpropegation algorithm for calculating the gradient of the cost function
  public void backpropagation(Matrix[] dgradw, Vector[] dgradb, Vector image, Vector result) {
    Vector[] As = new Vector[layerCount]; //activations
    Vector[] Zs = new Vector[layerCount - 1]; //pre sigmoid activations

    Vector a = image;
    As[0] = a;

    //get activations of layers
    for (int i = 0; i < layerCount - 1; i++) {
      Vector z = weights[i].mult(a).add(biases[i]);
      Zs[i] = new Vector(z);
      a = sigmoid(z);
      As[i + 1] = new Vector(a); // may work w/ out new
    }
    //backwards pass
    Vector delta = costPrime(As[layerCount - 1], result);

    dgradb[layerCount - 2] = delta;
    dgradw[layerCount - 2] = delta.mult(As[layerCount - 2]);

    for (int i = 2; i < layerCount; i++) {
      Vector z = Zs[layerCount - i - 1];
      delta = weights[layerCount - i].transpose().mult(delta).schur(sigmoidPrime(z));
      dgradb[layerCount - i - 1] = delta;
      dgradw[layerCount - i - 1] = delta.mult(As[layerCount - i - 1]);
    }
  }

  public int evaluate(NumberData testData) {
    int j = 0;
    for (int i = 0; i < testData.size; i++) {
      if (classify(testData.getImage(i)) == testData.getNumber(i)) j++;
    }
    return j;
  }

  public int classify(Vector image) {
    return feedForward(image).maxIndex();
  }

  //gradient of cost
  public static Vector costPrime(Vector a, Vector y) {
    return a.add(y.mult(-1));
  }

  //sigmoid
  public static double sigmoid(double x) {
    return 1d/(1d + Math.exp(-x));
  }

  public static Vector sigmoid(Vector x) {
    for (int i = 0; i < x.m; i++) {
      x.set(i , sigmoid(x.get(i)));
    }
    return x;
  }

  //derivative of sigmoid
  public static double sigmoidPrime(double x) {
    return sigmoid(x)*(1d - sigmoid(x));
  }

  public static Vector sigmoidPrime(Vector x) {
    for (int i = 0; i < x.m; i++) {
      x.set(i , sigmoidPrime(x.get(i)));
    }
    return x;
  }
}
