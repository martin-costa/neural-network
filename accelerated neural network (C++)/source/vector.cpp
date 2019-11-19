
#include "vector.hpp"

// constructors
Vector::Vector(std::vector<double> elts) {
  this->elts = elts;
  this->length = elts.size();
}

Vector::Vector(std::initializer_list<double> elts) {
  this->length = elts.size();
  this->elts = std::vector<double>(length);
  double* ptr = (double*)elts.begin();
  for (int i = 0; i < length; i++) this->elts[i] = ptr[i];
}

Vector::Vector(int length) {
  this->elts = std::vector<double>(length);
  this->length = length;
}

Vector::Vector() {}

// get access to elements
double& Vector::operator[](int i) {
  return elts[i];
}

double& Vector::operator()(int i) {
  return elts[i];
}

void Vector::operator=(double* v) {
  for (int i = 0; i < length; i++) elts[i] = v[i];
}

// standard vector space operations
Vector Vector::operator+(Vector v) {
  if (length != v.size()) throw IncompatibleVectorException();
  Vector u = *this;
  for (int i = 0; i < length; i++) u[i] += v[i];
  return u;
}

void Vector::operator+=(Vector v) {
  if (length != v.size()) throw IncompatibleVectorException();
  for (int i = 0; i < length; i++) elts[i] += v[i];
}

Vector Vector::operator-() {
  return *this * -1;
}

Vector Vector::operator-(Vector v) {
  if (length != v.size()) throw IncompatibleVectorException();
  Vector u = *this;
  for (int i = 0; i < length; i++) u[i] -= v[i];
  return u;
}

void Vector::operator-=(Vector v) {
  if (length != v.size()) throw IncompatibleVectorException();
  for (int i = 0; i < length; i++) elts[i] -= v[i];
}

Vector Vector::operator*(double a) {
  Vector u = *this;
  for (int i = 0; i < length; i++) u[i] *= a;
  return u;
}

void Vector::operator*=(double a) {
  for (int i = 0; i < length; i++) elts[i] *= a;
}

Vector Vector::operator/(double a) {
  Vector u = *this;
  for (int i = 0; i < length; i++) u[i] /= a;
  return u;
}

void Vector::operator/=(double a) {
  for (int i = 0; i < length; i++) elts[i] /= a;
}

// real vector operations
double magnitude(Vector v) {
  double magnitude = 0;
  for (int i = 0; i < v.length; i++) magnitude += pow(v.elts[i], 2);
  return sqrt(magnitude);
}

Vector normalize(Vector v) {
  return v * 1 / magnitude(v);
}

Vector schur(Vector v, Vector u) {
  if (v.length != u.length) throw IncompatibleVectorException();
  Vector w = v;
  for (int i = 0; i < w.length; i++) w.elts[i] *= u.elts[i];
  return w;
}

double dot(Vector v, Vector u) {
  if (v.length != u.length) throw IncompatibleVectorException();
  double dot = 0;
  for (int i = 0; i < v.size(); i++) dot += v.elts[i] * u.elts[i];
  return dot;
}

Vector Vector::normalize() {
  *this *= 1 / magnitude(*this);
  return *this;
}

Vector Vector::schur(Vector v) {
  if (length != v.size()) throw IncompatibleVectorException();
  for (int i = 0; i < length; i++) elts[i] *= v[i];
  return *this;
}

Vector Vector::fillGaussian(double mu, double sigma) {

  // create a seed based on the time and a normal dis. based on the seed
  int seed = std::chrono::system_clock::now().time_since_epoch().count();
  std::default_random_engine generator(seed);
  std::normal_distribution<double> gauss(mu, sigma);

  for (int i = 0; i < length; i++) {
    elts[i] = gauss(generator);
  }
  return *this;
}

int Vector::maxIndex() {
  int j = 0;
  for (int i = 1; i < length; i++) {
    if (elts[j] < elts[i]) j = i;
  }
  return j;
}

// overload stream operator
std::ostream& operator<<(std::ostream& stream, const Vector& v) {
  stream << "[";
  for (int i = 0; i < v.length - 1; i++) {
    stream << v.elts[i] << ", ";
  }
  if (v.length > 0) stream << v.elts[v.length - 1];
  return stream << "]";
}

// setters and getters
std::vector<double> Vector::getElts() { return elts; }

double* Vector::getEltsPtr() { return elts.data(); }

int Vector::size() { return length; }