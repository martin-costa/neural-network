#pragma once

#include <vector>
#include "vector.hpp"

// stores a single data element
class DataElement {
protected:

  // the vectorised data
  Vector data;

public:

  // retrieve the data
  Vector getData();

  // get the ideal output for this data
  virtual Vector getResult() = 0;
};

// stores a single piece of data for a number
class NumberData : public DataElement {
protected:

  // the number the data represents
  int number;

public:

  // construct a new numebr data piece
  NumberData(Vector image, int number);
  
  NumberData();

  // get vectorised result
  Vector getResult();

  int getNumber() { return number; }
};

// a collection of data of type D
template <typename D>
class DataSet {
private:
  std::vector<D> data;
  int size;

public:

  // create a dataset of size size
  DataSet(int size);

  DataSet();

  // get a ref. to the ith element in the data
  D& operator[](int i);

  // randomise the order of the data
  DataSet<D> randomise();

  // split data into many mini batches
  DataSet<D>* split(int newSize);

  // get the size of the set of data
  int getSize() { return size; };
};

template <typename D>
DataSet<D>::DataSet(int size) {
  this->size = size;
  this->data = std::vector<D>(size);
}

template <typename D>
DataSet<D>::DataSet() {}

template <typename D>
D& DataSet<D>::operator[](int i) {
  return data[i];
}

template <typename D>
DataSet<D> DataSet<D>::randomise() {

  int seed = std::chrono::system_clock::now().time_since_epoch().count();
  std::default_random_engine generator(seed);
  std::uniform_int_distribution<int> uniform(0, 10000000);

  int i = size;
  while (1 < i) {
    int j = uniform(generator) % i--;
    D temp = data[i];
    data[i] = data[j];
    data[j] = temp;
  }

  return *this;
}

template <typename D>
DataSet<D>* DataSet<D>::split(int newSize) {
  int batchCount = size / newSize;

  DataSet<D>* batches = new DataSet<D>[batchCount]();

  for (int i = 0; i < batchCount; i++) {
    batches[i] = DataSet<D>(newSize);

    for (int j = 0; j < newSize; j++) {
      int index = i * newSize + j;
      batches[i][j] = data[index];
    }
  }
  return batches;
}

// create a random permutation
static int* permutation(int length) {
  int* perm = new int[length]();
  for (int i = 0; i < length; i++) {
    perm[i] = i;
  }

  int seed = std::chrono::system_clock::now().time_since_epoch().count();
  std::default_random_engine generator(seed);
  std::uniform_int_distribution<int> uniform(0, 10000000);

  int i = length;
  while (1 < i) {
    int j = uniform(generator) % i--;
    int temp = perm[i];
    perm[i] = perm[j];
    perm[j] = temp;
  }

  return perm;
}