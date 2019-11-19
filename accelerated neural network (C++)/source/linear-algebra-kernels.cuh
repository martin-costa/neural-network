/*

#pragma once

#include "cuda_runtime.h"
#include "device_launch_parameters.h"

//#include "cooperative_groups.h"

#include "device_functions.h"
#include <iostream>

#include <algorithm>

//NOTE: ALL COMPATABILITY CHECKS SHOULD BE DONE BEFORE INVOKING ANY OF THESE METHODS

double* __host__ vectorMatMult(double* M, double* v, int m, int n);

void __global__ vectorMatMultKernel(double* u, double* M, double* v, int m, int n);

double* __host__ __device__ vectorAddOn(double* v, double* u, int size);

void __global__ vectorAddOnKernel(double* v, double* u, int size);

int __host__ __device__ maxIndex(double* v, int size);

void __global__ maxIndexKernel(double* v, int* idx, int size);

void __global__ schurProductKernel(double* w, double* v, double* u, int size);

//aux method to get size block
static int __host__ __device__ getBlock(float threadCount, float size) {
  int x = ceil(size / threadCount);
  if (x <= 0) return 1;
  return x;
}

*/