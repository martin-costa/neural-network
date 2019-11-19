#include "costfunctions.hpp"

// cost of this function for an output a
double QuadraticCost::f(Vector a, Vector y) {
  return pow(magnitude(a - y), 2) / 2.0;
}

// return error in final layer, more useful than cost derivative since allows
// for algebraic manipulation
Vector QuadraticCost::finalError(Vector z, Vector a, Vector y) {
  return (a - y).schur(Sigmoid::fPrime(z));
}

// return error in final layer, more useful than cost derivative since allows
// for algebraic manipulation
Vector CrossEntropyCost::finalError(Vector z, Vector a, Vector y) {
  return a - y;
}