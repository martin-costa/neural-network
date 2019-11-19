#pragma once

#include "vector.hpp"
#include "linear-algebra-exceptions.hpp"

class Matrix {
private:
  std::vector<double> elts;
  int length; // amount of elts
  int m; // rows
  int n; // columns

public:

  // constructors
  Matrix(std::vector<double> elts, int m, int n);

  Matrix(std::initializer_list<std::initializer_list<double>> elts);

  Matrix(int m, int n);

  Matrix();

  // get access to elements
  double& operator[](int i);

  double& operator()(int i, int j);

  void operator=(double* v);

  // standard vector space operations
  Matrix operator+(Matrix v);

  void operator+=(Matrix v);

  Matrix operator-(Matrix v);

  Matrix operator-();

  void operator-=(Matrix v);

  Matrix operator*(double a);

  void operator*=(double a);

  Matrix operator/(double a);

  void operator/=(double a);

  // real matrix operations
  Vector operator*(Vector v);

  Matrix operator*(Matrix v);

  void operator*=(Matrix v);

  friend Matrix transpose(Matrix v);

  friend Matrix schur(Matrix v, Matrix u);

  Matrix transpose();

  Matrix schur(Matrix v);

  static Matrix toRowMat(Vector v);

  static Matrix toColMat(Vector v);

  Matrix fillGaussian(double mu, double sigma);

  // overload stream operator
  friend std::ostream& operator<<(std::ostream& stream, const Matrix& v);

  // setters and getters
  std::vector<double> getElts();

  double* getEltsPtr();

  int size();

  int rows();

  int cols();
};
