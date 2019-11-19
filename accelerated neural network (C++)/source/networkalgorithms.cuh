#pragma once

#include <iostream>
#include "network.hpp"

//// __ Stochastic Gradient Descent using CPU __ ////

// sgd algorithm for training the neural network
void stochasticDescentCPU(Network* net, int epochs, int miniBatchSize, double learnRate, DataSet<NumberData>* trainingData, DataSet<NumberData>* testData);

// applies sgd using backprop to a mini batch
void updateMiniBatch(Network* net, DataSet<NumberData>* miniBatch, double learnRate);

// backpropegation algorithm for calculating the gradient of the cost function
void backpropagation(Network* net, Matrix* dgradw, Vector* dgradb, Vector* image, Vector* result);

/*

//// __ Stochastic Gradient Descent using GPU __ ////

// sgd algorithm for training the neural network on the GPU
void __host__ stochasticDescentGPU(Network* net, int epochs, int miniBatchSize, double learnRate, DataSet<NumberData>* trainingData, DataSet<NumberData>* testData);

// sigmoid activation function
double __host__ __device__ sigmoidFunction(double x);

double* __host__ __device__ sigmoidActivation(double* v, int size);

void __global__ sigmoidActivationKernel(double* v, int size);

double* __host__ feedForward(double* x, int* layersHost, int layerCount, double* weightsDev, double* biasesDev);

int __host__ evaluate(double* testData, int* testLabels, int testDataSize, int dataSize, int* layersHost,
  int layerCount, double* weightsDev, double* biasesDev);

*/