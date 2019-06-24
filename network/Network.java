package network;

import network.linear_algebra.*;

public class Network {
  private final int[] layers;

  private Matrix[] weights;
  private Vector[] biases;

  private double learnRate;
  private double miniBatchSize;

  public Network(int... layers) {
    this.layers = layers;
  }
}
