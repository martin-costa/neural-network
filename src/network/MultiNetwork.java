package network;

import network.*;
import network.data.*;
import network.linear_algebra.*;
import network.ui.*;

public class MultiNetwork {

  private Network[] networks;

  public final int n;

  public MultiNetwork(int n, int... layers) {
    this.n = n;
    this.networks = new Network[n];
    for (int i = 0; i < n; i++) {
      networks[i] = new Network(layers);
    }
  }

  public void train(int epochs, int miniBatchSize, double learnRate, NumberData trainingData, NumberData testData) {
    for (int j = 0; j < epochs; j++) {
      for (int i = 0; i < n; i++) {
        networks[i].stochasticGradientDescent(1, miniBatchSize, learnRate, trainingData, null);
      }
      if (testData != null) {
        System.out.println("Epoch " + j + " test data " + evaluate(testData) + "/" + testData.size);
      }
    }
  }

  public int classify(Vector image) {
    Vector results = new Vector(10, 0);
    for (int i = 0; i < n; i++) {
      results = results.add(networks[i].feedForward(image));
    }
    return results.maxIndex();
  }

  public int evaluate(NumberData testData) {
    int j = 0;
    for (int i = 0; i < testData.size; i++) {
      if (classify(testData.getImage(i)) == testData.getNumber(i)) j++;
    }
    return j;
  }
}
