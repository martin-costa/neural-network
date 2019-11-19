#include "activationfunctions.hpp"

// sigmoid
double Sigmoid::f(double x) {
  return 1 / (1 + exp(-x));
}

Vector Sigmoid::f(Vector x) {
  for (int i = 0; i < x.size(); i++) {
    x[i] = f(x[i]);
  }
  return x;
}

double Sigmoid::finv(double x) {
  return log(x / (1 - x));
}

Vector Sigmoid::finv(Vector x) {
  for (int i = 0; i < x.size(); i++) {
    x[i] = finv(x[i]);
  }
  return x;
}

// derivative of sigmoid
double Sigmoid::fPrime(double x) {
  return f(x) * (1 - f(x));
}

Vector Sigmoid::fPrime(Vector x) {
  for (int i = 0; i < x.size(); i++) {
    x[i] = fPrime(x[i]);
  }
  return x;
}