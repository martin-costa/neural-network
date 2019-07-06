package network.data;

import java.io.*;
import java.util.zip.*;

import network.linear_algebra.*;
import network.ui.*;

public class DataLoader {

  //loads the MNIST training data
  public static NumberData loadMNISTTraining(boolean showData, boolean showProgress) {
    try {
      return loadMNIST(showData, showProgress, "train");
    }
    catch(IOException e) {
      System.out.println("\rError loading training data");
      return null;
    }
  }

  //loads the MNIST test data
  public static NumberData loadMNISTTest(boolean showData, boolean showProgress) {
    try {
      return loadMNIST(showData, showProgress, "t10k");
    }
    catch(IOException e) {
      System.out.println("\rError loading test data");
      return null;
    }
  }

  //this method loads in the data from the MNIST database
  private static NumberData loadMNIST(boolean showData, boolean showProgress, String path) throws IOException {
    //make display window for data being loaded
    Window display = null;

    if (showData) display = new Window();

    int res = 28; //all MNIST images have a resolution of 28x28

    //the data
    NumberData data = null;

    GZIPInputStream images = new GZIPInputStream(new FileInputStream("../data/mnist/" + path + "-images-idx3-ubyte.gz"));
    GZIPInputStream labels = new GZIPInputStream(new FileInputStream("../data/mnist/" + path + "-labels-idx1-ubyte.gz"));

    //skip magic number
    images.skip(4);

    //get image count, stored as 32 bit integer so requires next 4 bytes
    int imageCount = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());

    //skip rest to labels and pixels
    images.skip(8);
    labels.skip(8);

    //create vector to store pixels in form used by network
    Vector pixels = new Vector(res*res, 0);

    //create buffer to load data
    byte[] buffer = new byte[res*res];

    //initialise the data storage
    data = new NumberData(imageCount);

    //main loop where all the images are loaded
    for(int j = 0; j < imageCount; j++) {
      if(showProgress && j % 101 == 0) System.out.print("\r" + j + "/" + imageCount + " images loaded");

      readCompressedData(buffer, images);
      for (int i = 0; i < res*res; i++) {
        pixels.set(i, ((int)buffer[i] & 0xff)/255d);
      }

      //update the display window
      if (showData) {
        display.display(pixels);
        display.update();
      }
      data.addData(j, labels.read(), pixels);

      // try {
      //   Thread.sleep(200);
      // }
      // catch(Exception e) {}
    }

    images.close();
    labels.close();

    if (showData) display.close();
    if (showProgress) System.out.println("\r" + imageCount + "/" + imageCount + " images loaded");
    return data;
  }

  private static void readCompressedData(byte[] buffer, GZIPInputStream stream) throws IOException {
    int j = buffer.length;
    while (j > 0) {
      j -= stream.read(buffer, buffer.length - j, j);
    }
  }

}
