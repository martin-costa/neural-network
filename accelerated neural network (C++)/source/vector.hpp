#pragma once

#include <chrono>
#include <random>
#include <ostream>
#include <initializer_list>

#include "linear-algebra-exceptions.hpp"

class Vector {
private:
  std::vector<double> elts;
  int length; // amount of elements

public:

  // constructors
  Vector(std::vector<double> elts);

  Vector(std::initializer_list<double> elts);

  Vector(int length);

  Vector();

  // get access to elements
  double& operator[](int i);

  double& operator()(int i);

  void operator=(double* v);

  // standard vector space operations
  Vector operator+(Vector v);

  void operator+=(Vector v);

  Vector operator-(Vector v);

  Vector operator-();

  void operator-=(Vector v);

  Vector operator*(double a);

  void operator*=(double a);

  Vector operator/(double a);

  void operator/=(double a);

  // real vector operations
  friend double magnitude(Vector v);

  friend Vector normalize(Vector v);

  friend Vector schur(Vector v, Vector u);

  friend double dot(Vector v, Vector u);

  Vector normalize();

  Vector schur(Vector v);

  Vector fillGaussian(double mu, double sigma);

  int maxIndex();

  // overload stream operator
  friend std::ostream& operator<<(std::ostream& stream, const Vector& v);

  // setters and getters
  std::vector<double> getElts();

  double* getEltsPtr();

  int size();
};
