package network.data;

import network.linear_algebra.*;



public class NumberData {

  //amount of images in this piece of data
  public final int n;

  //array of n vectors containing data for images
  public Vector[] images;

  //array of ideal results for corresponding image input
  public Vector[] results;

  NumberData(int n) {
    this.n = n;
    images = new Vector[n];
    results = new Vector[n];
  }

  //add one of the n pieces of data
  public void addData(int k, int i, Vector V) {
    addImage(k, V);
    addResult(k, i);
  }

  //add the image of a piece of data
  public void addImage(int k, Vector V) {
    try {
      images[k] = new Vector(V);
    }
    catch(Exception e) {
      System.out.println("image index out of range");
    }
  }

  //add the result of a piece of data
  public void addResult(int k, int i) {
    results[k] = resultVector(i);
  }

  //get the result vector for a number
  public Vector resultVector(int i) {
    Vector V = new Vector(10, 0); //10 dim 0 vec.
    V.set(i + 1, 1);
    return V;
  }
}
