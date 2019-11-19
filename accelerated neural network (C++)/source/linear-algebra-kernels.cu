/*

#include "linear-algebra-kernels.cuh"

double* __host__ vectorMatMult(double* M, double* v, int m, int n) {

  double* u = 0;
  cudaMalloc((void**)&u, sizeof(double) * m);

  dim3 blockDim(std::min(m, 1024));
  dim3 gridDim(getBlock(blockDim.x, m));

  vectorMatMultKernel <<<gridDim, blockDim>>> (u, M, v, m, n);

  return u;
}

void __global__ vectorMatMultKernel(double* u, double* M, double* v, int m, int n) {
  int i = threadIdx.x + blockDim.x * blockIdx.x;

  if (i < m) {
    double temp = 0;

    for (int j = 0; j < n; j++) {
      temp += M[i + j * m] * v[j];
    }
    u[i] = temp;
  }
}

double* __host__ __device__ vectorAddOn(double* v, double* u, int size) {

  dim3 blockDim(std::min(size, 1024));
  dim3 gridDim(getBlock(blockDim.x, size));

  vectorAddOnKernel <<<gridDim, blockDim>>> (v, u, size);

  return v;
}

void __global__ vectorAddOnKernel(double* v, double* u, int size) {
  int i = threadIdx.x + blockDim.x * blockIdx.x;

  if (i < size)
    v[i] += u[i];
}

int __host__ __device__ maxIndex(double* v, int size) {

  int* idxDev = 0;
  cudaMalloc((void**)&idxDev, sizeof(int));
  maxIndexKernel <<<1, 1>>> (v, idxDev, size);

  int idx[] = {0};
  *idx = 0;

  cudaMemcpy(idx, idxDev, sizeof(int), cudaMemcpyDeviceToHost);
  cudaFree(idxDev);

  return *idx;
}

void __global__ maxIndexKernel(double* v, int* idx, int size) {

  double max = v[0];
  *idx = 0;

  for (int i = 1; i < size; i++) {
    if (v[i] >= max) {
      max = v[i];
      *idx = i;
    }
  }
}

void __global__ schurProductKernel(double* w, double* v, double* u, int size) {
  int i = threadIdx.x + blockDim.x * blockIdx.x;

  if (i < size)
    w[i] = v[i] * u[i];
}

*/