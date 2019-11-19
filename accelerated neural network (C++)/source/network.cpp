#include "network.hpp"

// initialise the neural network with random weights and biases
Network::Network(std::initializer_list<int> layers) {
  this->layerCount = layers.size();
  this->layers = (int*)layers.begin();

  // initialise biases
  biases = new Vector[layerCount - 1]();
  for (int i = 0; i < layerCount - 1; i++) {
    biases[i] = Vector(this->layers[i + 1]).fillGaussian(0, 1 / sqrt(this->layers[i]));
  }

  // initialise weights
  weights = new Matrix[layerCount - 1]();
  for (int i = 0; i < layerCount - 1; i++) {
    weights[i] = Matrix(this->layers[i + 1], this->layers[i]).fillGaussian(0, 1 / sqrt(this->layers[i]));
  }
}

// initialise the neural network by loading in weights and biases
Network::Network(int* layers, int layerCount, Matrix* weights, Vector* biases) {
  this->layerCount = layerCount;
  this->layers = layers;
  this->weights = weights;
  this->biases = biases;
}

Network::Network() {}

// method that gets output for input x for current weights and biases 
Vector Network::feedForward(Vector a) {
  for (int i = 0; i < layerCount - 1; i++) {
    a = Sigmoid::f(weights[i] * a + biases[i]);
  }
  return a;
}

Vector Network::feedBackward(Vector a) {
  for (int i = layerCount - 2; i >= 0; i--) {
    a = Sigmoid::f(weights[i].transpose() * (a + biases[i]));
  }
  return a;
}

// return the number of pieces of data in the test data correctly evaluated
int Network::evaluate(DataSet<NumberData> testData) {
  int j = 0;
  for (int i = 0; i < testData.getSize(); i++) {
    if (classify(testData[i].getData()) == testData[i].getNumber()) j++;
  }
  return j;
}

// classify this image with current weights and biases
int Network::classify(Vector image) {
  return feedForward(image).maxIndex();
}

// getters and setters
int Network::getLayerCount() {
  return layerCount;
}
int* Network::getLayers() {
  return layers;
}
Matrix* Network::getWeights() {
  return weights;
}
Vector* Network::getBiases() {
  return biases;
}