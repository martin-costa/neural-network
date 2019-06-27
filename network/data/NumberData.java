package network.data;

import network.linear_algebra.*;
import java.util.Random;

public class NumberData {

  //amount of numbers in data
  public final int size;

  //data
  private DataPiece[] data;

  NumberData(int size) {
    this.size = size;
    data = new DataPiece[size];
  }

  //add a piece of data to this set
  public void addData(int i, int number, Vector image) {
    data[i] = new DataPiece(new Vector(image), number);
  }

  //randomize order of data pieces for random sampling
  public NumberData randomize() {
    Random generator = new Random();
    int i = size;
    
    while (1 < i) {
      int j = generator.nextInt(i--);
      DataPiece temp = data[i];
      data[i] = data[j];
      data[j] = temp;
    }
    return this;
  }

  //splits the NumberData new NumberDatas of size newSize
  public NumberData[] split(int newSize) {
    int batchCount = size/newSize;
    NumberData[] batches = new NumberData[batchCount];
    for (int i = 0; i < batchCount; i++) {
      batches[i] = new NumberData(newSize);

      for (int j = 0; j < newSize; j++) {
        int index = i*newSize + j;

        batches[i].addData(j, data[index].number, data[index].image);
      }
    }
    return batches;
  }

  //getters for all the data
  public Vector getImage(int i) {
    return data[i].image;
  }

  public int getNumber(int i) {
    return data[i].number;
  }

  public Vector getResult(int i) {
    return data[i].vectoriseNumber();
  }

  //data structure for data peice
  private class DataPiece {
    public Vector image;
    public int number;

    DataPiece(Vector image, int number) {
      this.image = image;
      this.number = number;
    }

    public Vector vectoriseNumber() {
      Vector V = new Vector(10, 0); //the 10 dim 0 vec.
      V.set(number, 1);
      return V;
    }
  }

}
