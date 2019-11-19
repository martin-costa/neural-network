#include "networkalgorithms.cuh"

//// __ Stochastic Gradient Descent using CPU __ ////

// sgd algorithm for training the neural network on the CPU
void stochasticDescentCPU(Network* net, int epochs, int miniBatchSize, double learnRate, DataSet<NumberData>* trainingData, DataSet<NumberData>* testData) {

  // run each epoch of training
  for (int i = 0; i < epochs; i++) {
    int batchCount = trainingData->getSize() / miniBatchSize;
    DataSet<NumberData>* miniBatches = trainingData->randomise().split(miniBatchSize);

    // update mini batches
    for (int j = 0; j < batchCount; j++) {
      updateMiniBatch(net, &miniBatches[j], learnRate);
    }

    delete[] miniBatches;

    // test the current weights and biases against the test data
    if (testData != nullptr) {
      std::cout << net->evaluate(*testData) << "/" << testData->getSize() << " numbers correctly classified\n";
    }
    std::cout << "Epoch " << i << " complete\n";
  }
}

// applies sgd using backprop to a mini batch
void updateMiniBatch(Network* net, DataSet<NumberData>* miniBatch, double learnRate) {

  Matrix* gradw = new Matrix[net->getLayerCount() - 1]();
  Vector* gradb = new Vector[net->getLayerCount() - 1]();

  Matrix* dgradw = new Matrix[net->getLayerCount() - 1]();
  Vector* dgradb = new Vector[net->getLayerCount() - 1]();

  for (int j = 0; j < net->getLayerCount() - 1; j++) {
    gradw[j] = Matrix(net->getWeights()[j].rows(), net->getWeights()[j].cols());
    gradb[j] = Vector(net->getBiases()[j].size());
  }

  // for each piece of training data
  for (int i = 0; i < miniBatch->getSize(); i++) {

    for (int j = 0; j < net->getLayerCount() - 1; j++) {
      dgradw[j] = Matrix(net->getWeights()[j].rows(), net->getWeights()[j].cols());
      dgradb[j] = Vector(net->getBiases()[j].size());
    }

    backpropagation(net, dgradw, dgradb, &(*miniBatch)[i].getData(), &(*miniBatch)[i].getResult());

    for (int j = 0; j < net->getLayerCount() - 1; j++) {
      gradw[j] += dgradw[j];
      gradb[j] += dgradb[j];
    }
  }

  for (int j = 0; j < net->getLayerCount() - 1; j++) {
    net->getWeights()[j] += gradw[j] * (-learnRate / (double)miniBatch->getSize());
    net->getBiases()[j] += gradb[j] * (-learnRate / (double)miniBatch->getSize());
  }

  delete[] gradw;
  delete[] gradb;
  delete[] dgradw;
  delete[] dgradb;
}

// backpropegation algorithm for calculating the gradient of the cost function
void backpropagation(Network* net, Matrix* dgradw, Vector* dgradb, Vector* image, Vector* result) {
  int layerCount = net->getLayerCount();

  Vector* As = new Vector[layerCount](); //activations
  Vector* Zs = new Vector[layerCount - 1](); //pre sigmoid activations

  // get activations of layers
  As[0] = *image;

  for (int i = 0; i < layerCount - 1; i++) {
    Zs[i] = net->getWeights()[i] * As[i] + net->getBiases()[i];
    As[i + 1] = Sigmoid::f(Zs[i]);
  }

  // backwards pass
  Vector delta = CrossEntropyCost::finalError(Zs[layerCount - 2], As[layerCount - 1], *result);

  dgradb[layerCount - 2] = delta;
  dgradw[layerCount - 2] = Matrix::toRowMat(delta) * Matrix::toColMat(As[layerCount - 2]);

  for (int i = 2; i < layerCount; i++) {
    delta = (transpose(net->getWeights()[layerCount - i]) * delta).schur(Sigmoid::fPrime(Zs[layerCount - i - 1]));
    dgradb[layerCount - i - 1] = delta;
    dgradw[layerCount - i - 1] = Matrix::toRowMat(delta) * Matrix::toColMat(As[layerCount - i - 1]);
  }

  delete[] As;
  delete[] Zs;
}

/*

//// __ Stochastic Gradient Descent using GPU __ ////

// sgd algorithm for training the neural network on the GPU
void __host__ stochasticDescentGPU(Network* net, int epochs, int miniBatchSize, double learnRate, DataSet<NumberData>* trainingData, DataSet<NumberData>* testData) {

  // get the network data onto the GPU

  // parameters
  int layerCount = net->getLayerCount();
  int* layersDev = 0;
  double* weightsDev = 0;
  double* biasesDev = 0;

  //data
  double* trainingDataDev = 0;
  double* testDataDev = 0;

  int* trainingLabelsDev = 0;
  int* testLabelsDev = 0;

  int* trainingDataPermDev = 0;
  int* testDataPermDev = 0;

  // allocate space and values to layersDev
  cudaMalloc((void**)&layersDev, sizeof(int) * layerCount);
  cudaMemcpy(layersDev, net->getLayers(), sizeof(int) * layerCount, cudaMemcpyHostToDevice);

  // allocate space and values to weightsDev
  int weightsSize = 0;
  for (int i = 0; i < layerCount - 1; i++) {
    weightsSize += net->getLayers()[i + 1] * net->getLayers()[i];
  }
  cudaMalloc((void**)&weightsDev, sizeof(double) * weightsSize);

  int j = 0;
  for (int i = 0; i < layerCount - 1; i++) {
    cudaMemcpy(&weightsDev[j], &net->getWeights()[i][0], sizeof(double) * net->getLayers()[i + 1] * net->getLayers()[i], cudaMemcpyHostToDevice);
    j += net->getLayers()[i + 1] * net->getLayers()[i];
  }

  //allocate space and values to biasesDev
  int biasesSize = 0;
  for (int i = 0; i < layerCount - 1; i++) {
    biasesSize += net->getLayers()[i + 1];
  }
  cudaMalloc((void**)&biasesDev, sizeof(double) * biasesSize);

  j = 0;
  for (int i = 0; i < layerCount - 1; i++) {
    cudaMemcpy(&biasesDev[j], &net->getBiases()[i][0], sizeof(double) * net->getLayers()[i + 1], cudaMemcpyHostToDevice);
    j += net->getLayers()[i + 1];
  }

  // allcoate space and values to data
  int trainingDataSize = trainingData->getSize();
  int testDataSize = testData->getSize();
  int dataSize = (*trainingData)[0].getData().size();

  cudaMalloc((void**)&trainingDataDev, trainingDataSize * sizeof(double) * dataSize);
  cudaMalloc((void**)&testDataDev, testDataSize * sizeof(double) * dataSize);

  cudaMalloc((void**)&trainingLabelsDev, sizeof(int) * trainingDataSize);
  cudaMalloc((void**)&testLabelsDev, sizeof(int) * testDataSize);

  int *trainingLabelsHost = new int[trainingDataSize];
  int* testLabelsHost = new int[trainingDataSize];

  for (int i = 0; i < trainingDataSize; i++) {
    cudaMemcpy(&trainingDataDev[i * dataSize], &(*trainingData)[i].getData()[0], sizeof(double) * dataSize, cudaMemcpyHostToDevice);
    trainingLabelsHost[i] = (*trainingData)[i].getNumber();
  }
  cudaMemcpy(trainingLabelsDev, trainingLabelsHost, sizeof(int) * trainingDataSize, cudaMemcpyHostToDevice);

  for (int i = 0; i < testDataSize; i++) {
    cudaMemcpy(&testDataDev[i * dataSize], &(*testData)[i].getData()[0], sizeof(double) * dataSize, cudaMemcpyHostToDevice);
    testLabelsHost[i] = (*testData)[i].getNumber();
  }
  cudaMemcpy(testLabelsDev, testLabelsHost, sizeof(int) * testDataSize, cudaMemcpyHostToDevice);

  //cudaMalloc((void**)&trainingDataPermDev, sizeof(double) * trainingData->getSize());
  //cudaMalloc((void**)&testDataPermDev, sizeof(double) * testData->getSize());

  //int* trainingDataPermHost = permutation(trainingData->getSize());
  //int* testDataPermHost = permutation(trainingData->getSize());

  //cudaMemcpy(trainingDataPermDev, trainingDataPermHost, sizeof(double) * trainingData->getSize(), cudaMemcpyHostToDevice);
  //cudaMemcpy(testDataPermDev, testDataPermHost, sizeof(double) * testData->getSize(), cudaMemcpyHostToDevice);

  //delete[] trainingDataPermHost;
  //delete[] testDataPermHost;

  // run each epoch of training
  for (int i = 0; i < epochs; i++) {
    int batchCount = trainingData->getSize() / miniBatchSize;
    DataSet<NumberData>* miniBatches = trainingData->randomise().split(miniBatchSize);

    // update mini batches
    for (int j = 0; j < batchCount; j++) {
      updateMiniBatch(net, &miniBatches[j], learnRate);
    }

    delete[] miniBatches;

    // test the current weights and biases against the test data
    if (testData != nullptr) {
      std::cout << net->evaluate(*testData) << "/" << testData->getSize() << " numbers correctly classified\n";
    }
    std::cout << "Epoch " << i << " complete\n";
  }

  std::cout << evaluate(testDataDev, testLabelsHost, testDataSize, dataSize, &net->getLayers()[0], layerCount, weightsDev, biasesDev) << "\n";

  // pass parameters back to the CPU
  int weightOffset = 0;
  int biasOffset = 0;

  for (int i = 0; i < layerCount - 1; i++) {
    cudaMemcpy(&net->getWeights()[i][0], &weightsDev[weightOffset], sizeof(double) * net->getLayers()[i + 1] * net->getLayers()[i], cudaMemcpyDeviceToHost);
    cudaMemcpy(&net->getBiases()[i][0], &biasesDev[biasOffset], sizeof(double)* net->getLayers()[i + 1], cudaMemcpyDeviceToHost);

    weightOffset += net->getLayers()[i + 1] * net->getLayers()[i];
    biasOffset += net->getLayers()[i + 1];
  }

  // free up the GPU
  cudaFree(layersDev);
  cudaFree(weightsDev);
  cudaFree(biasesDev);

  cudaFree(trainingDataDev);
  cudaFree(testDataDev);

  cudaFree(trainingDataPermDev);
  cudaFree(testDataPermDev);

  delete[] trainingLabelsHost;
  delete[] testLabelsHost;
}

// kernels for network
double __host__ __device__ sigmoidFunction(double x) {
  return 1.0 / (1.0 + expf(-x));
}

double* __host__ __device__ sigmoidActivation(double* v, int size) {

  dim3 blockDim(std::min(size, 1024));
  dim3 gridDim(getBlock(blockDim.x, size));

  sigmoidActivationKernel <<<gridDim, blockDim>>> (v, size);

  return v;
}

void __global__ sigmoidActivationKernel(double* v, int size) {
  int i = threadIdx.x + blockDim.x * blockIdx.x;

  if (i < size)
    v[i] = sigmoidFunction(v[i]);
}

double* __host__ feedForward(double* x, int* layersHost, int layerCount, double* weightsDev, double* biasesDev) {
  int weightOffset = layersHost[1] * layersHost[0];
  int biasOffset = layersHost[1];

  // -> CAUSING MEMORY LEAKS <- //
  double* a = sigmoidActivation(vectorAddOn(vectorMatMult(&weightsDev[0], x, layersHost[1], layersHost[0]), &biasesDev[0], layersHost[1]), layersHost[1]);

  for (int i = 1; i < layerCount - 1; i++) {
    a = sigmoidActivation(vectorAddOn(vectorMatMult(&weightsDev[weightOffset], a, layersHost[i + 1], layersHost[i]), &biasesDev[biasOffset], layersHost[i + 1]), layersHost[i + 1]);

    weightOffset += layersHost[i + 1] * layersHost[i];
    biasOffset += layersHost[i + 1];
  }
  return a;
}

int __host__ evaluate(double* testData, int* testLabels, int testDataSize, int dataSize, int* layersHost, int layerCount, double* weightsDev, double* biasesDev) {
  int j = 0;

  for (int i = 0; i < testDataSize; i++) {
    if (maxIndex(feedForward(&testData[i * dataSize], layersHost, layerCount, weightsDev, biasesDev), layersHost[layerCount - 1]) == testLabels[i])
      j++;
  }

  return j;
}

*/