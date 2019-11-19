
#include "matrix.hpp"

// constructors
Matrix::Matrix(std::vector<double> elts, int m, int n) {
  this->m = m;
  this->n = n;
  this->length = m * n;
  this->elts = elts;
}

Matrix::Matrix(std::initializer_list<std::initializer_list<double>> elts) {
  std::initializer_list<double>* rowPtr = (std::initializer_list<double>*)elts.begin();

  this->m = elts.size();
  this->n = rowPtr->size();
  this->length = m * n;

  this->elts = std::vector<double>(length);

  for (int i = 0; i < m; i++) {
    double* colPtr = (double*)rowPtr[i].begin();
    for (int j = 0; j < n; j++) this->elts[i * n + j] = colPtr[j];
  }
}

Matrix::Matrix(int m, int n) {
  this->m = m;
  this->n = n;
  this->length = m * n;
  this->elts = std::vector<double>(m * n);
}

Matrix::Matrix() {}

// get access to elements
double& Matrix::operator[](int i) {
  return elts[i];
}

double& Matrix::operator()(int i, int j) {
  return elts[i * n + j];
}

void Matrix::operator=(double* v) {
  for (int i = 0; i < length; i++) elts[i] = v[i];
}

// standard vector space operations
Matrix Matrix::operator+(Matrix v) {
  if (m != v.rows() || n != v.cols()) throw IncompatibleMatrixException();
  Matrix u = *this;
  for (int i = 0; i < length; i++) u[i] += v[i];
  return u;
}

void Matrix::operator+=(Matrix v) {
  if (m != v.rows() || n != v.cols()) throw IncompatibleMatrixException();
  for (int i = 0; i < length; i++) elts[i] += v[i];
}

Matrix Matrix::operator-() {
  return *this * -1;
}

Matrix Matrix::operator-(Matrix v) {
  if (length != v.size()) throw IncompatibleMatrixException();
  Matrix u = *this;
  for (int i = 0; i < length; i++) u[i] -= v[i];
  return u;
}

void Matrix::operator-=(Matrix v) {
  if (length != v.size()) throw IncompatibleMatrixException();
  for (int i = 0; i < length; i++) elts[i] -= v[i];
}

Matrix Matrix::operator*(double a) {
  Matrix u = *this;
  for (int i = 0; i < length; i++) u[i] *= a;
  return u;
}

void Matrix::operator*=(double a) {
  for (int i = 0; i < length; i++) elts[i] *= a;
}

Matrix Matrix::operator/(double a) {
  Matrix u = *this;
  for (int i = 0; i < length; i++) u[i] /= a;
  return u;
}

void Matrix::operator/=(double a) {
  for (int i = 0; i < length; i++) elts[i] /= a;
}

// real matrix operations
Vector Matrix::operator*(Vector v) {
  if (n != v.size()) throw IncompatibleVectorException();
  Vector u = Vector(m);
  for (int i = 0; i < m; i++) {
    for (int j = 0; j < n; j++) {
      u[i] += v[j] * (*this)(i, j);
    }
  }
  return u;
}

Matrix Matrix::operator*(Matrix v) {
  if (n != v.rows()) throw IncompatibleMatrixException();
  Matrix u = Matrix(m, v.cols());
  for (int i = 0; i < u.rows(); i++) {
    for (int j = 0; j < u.cols(); j++) {
      for (int k = 0; k < n; k++) {
        u(i, j) += (*this)(i, k) * v(k, j);
      }
    }
  }
  return u;
}

void Matrix::operator*=(Matrix v) {
  *this = *this * v;
}

Matrix transpose(Matrix v) {
  Matrix u = Matrix(v.n, v.m);
  for (int i = 0; i < u.rows(); i++) {
    for (int j = 0; j < u.cols(); j++) {
      u(i, j) = v(j, i);
    }
  }
  return u;
}

Matrix schur(Matrix v, Matrix u) {
  if (v.m != u.m || v.n != u.n) throw IncompatibleVectorException();
  Matrix w = v;
  for (int i = 0; i < w.size(); i++) w[i] *= u[i];
  return w;
}

Matrix Matrix::transpose() {
  Matrix u = *this;
  *this = Matrix(n, m);
  for (int i = 0; i < m; i++) {
    for (int j = 0; j < n; j++) {
      (*this)(i, j) = u(j, i);
    }
  }
  return *this;
}

Matrix Matrix::schur(Matrix v) {
  if (m != v.rows() || n != v.cols()) throw IncompatibleVectorException();
  for (int i = 0; i < length; i++) elts[i] *= v[i];
  return *this;
}

Matrix Matrix::fillGaussian(double mu, double sigma) {

  // create a seed based on the time and a normal dis. based on the seed
  int seed = std::chrono::system_clock::now().time_since_epoch().count();
  std::default_random_engine generator(seed);
  std::normal_distribution<double> gauss(mu, sigma);

  for (int i = 0; i < length; i++) {
    elts[i] = gauss(generator);
  }

  return *this;
}

Matrix Matrix::toRowMat(Vector v) {
  return Matrix(v.getElts(), v.size(), 1);
}

Matrix Matrix::toColMat(Vector v) {
  return Matrix(v.getElts(), 1, v.size());
}

// overload stream operator
std::ostream& operator<<(std::ostream& stream, const Matrix& v) {
  for (int i = 0; i < v.m; i++) {
    stream << "[";
    for (int j = 0; j < v.n; j++) {
      stream << v.elts[i * v.n + j];
      if (j < v.n - 1) stream << ", ";
    }
    stream << "]\n";
  }
  return stream;
}

// setters and getters
std::vector<double> Matrix::getElts() { return elts; }

double* Matrix::getEltsPtr() { return elts.data(); }

int Matrix::size() { return length; }

int Matrix::rows() { return m; }

int Matrix::cols() { return n; }