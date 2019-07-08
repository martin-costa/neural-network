package neural_network.network;

import neural_network.linear_algebra.*;
import neural_network.data.*;
import neural_network.network.cost_functions.*;
import neural_network.network.activation_functions.*;

public class Network2 extends Network {

  protected CrossCostEntropy costFn = new CrossCostEntropy();

  //initialise the neural network
  public Network2(int... layers) {
    super(layers);
  }

  public Network2(int[] layers, Matrix[] weights, Vector[] biases) {
    super(layers, weights, biases);
  }

  //learning algorithms implemented here

  //sgd algorithm for training the neural network
  public void stochasticGradientDescent(int epochs, int miniBatchSize, double learnRate, NumberData trainingData, NumberData testData) {

    //run each epoch of training
    for (int i = 0; i < epochs; i++) {
      NumberData[] miniBatches = trainingData.randomize().split(miniBatchSize);

      //update mini batches
      for (int j = 0; j < miniBatches.length; j++) {
        updateMiniBatch(miniBatches[j], learnRate);
      }

      //test the current weights and biases against the test data
      if (testData != null) {
        System.out.println(evaluate(testData) + "/" + testData.size + " numbers correctly classified");
      }
      System.out.println("Epoch " + i + " complete");
    }
  }

  //applies sgd using backprop to a mini batch
  public void updateMiniBatch(NumberData miniBatch, double learnRate) {

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
        gradw[j] = gradw[j].add(dgradw[j]);
        gradb[j] = gradb[j].add(dgradb[j]);
      }
    }

    for (int j = 0; j < layerCount - 1; j++) {
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
      a = Sigmoid.f(z);
      As[i + 1] = new Vector(a); // may work w/ out new
    }
    //backwards pass
    Vector delta = costFn.finalError(Zs[layerCount - 2], As[layerCount - 1], result);

    dgradb[layerCount - 2] = delta;
    dgradw[layerCount - 2] = delta.mult(As[layerCount - 2]);

    for (int i = 2; i < layerCount; i++) {
      Vector z = Zs[layerCount - i - 1];
      delta = weights[layerCount - i].transpose().mult(delta).schur(Sigmoid.fPrime(z));
      dgradb[layerCount - i - 1] = delta;
      dgradw[layerCount - i - 1] = delta.mult(As[layerCount - i - 1]);
    }
  }
}
