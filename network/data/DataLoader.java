package network.data;

import network.ui.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.awt.image.*;
import network.linear_algebra.*;

public class DataLoader {

  public static NumberData loadMNISTTraining(boolean showData) {
    return loadMNIST(showData, "train");
  }

  public static NumberData loadMNISTTest(boolean showData) {
    return loadMNIST(showData, "t10k");
  }

  //this method is to load in the data from the MNIST database
  private static NumberData loadMNIST(boolean showData, String path) {
    //make display window for data being loaded
    Window display = null;

    if (showData) display = new Window();

    int res = 28; //all MNIST images have a resolution of 28x28

    //the data
    NumberData data = null;

    try {
      FileInputStream images = new FileInputStream("network/data/mnist/" + path + "-images.idx3-ubyte");
      FileInputStream labels = new FileInputStream("network/data/mnist/" + path + "-labels.idx1-ubyte");

      //skip magic number
      images.skip(4);

      //get image count, stored as 32 bit integer so requires next 4 bytes
      int imageCount = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());

      //skip rest to labels and pixels
      images.skip(8);
      labels.skip(8);

      Vector pixelVector = new Vector(res*res, 0);

      //create the data
      data = new NumberData(imageCount);

      for(int k = 0; k < imageCount; k++) {
        if(k % 50 == 0) System.out.print("\r" + k + "/" + imageCount + " images loaded");

        //update the display window
        if (showData) {
          double[][] pixels = new double[res][res];
          for (int j = 0; j < res; j++) {
            for (int i = 0; i < res; i++) {
              pixels[i][j] = images.read()/255d;
              pixelVector.set(j*res + i + 1, pixels[i][j]);
            }
          }
          display.display(pixels);
          display.update();
        }
        else {
          for (int i = 1; i <= res*res; i++) {
            pixelVector.set(i, images.read()/255d);
          }
        }

        data.addData(k, labels.read(), pixelVector);

        // try {
        //   Thread.sleep(200);
        // }
        // catch(Exception e) {}
      }
    }
    catch (Exception e) {
      System.err.println("error");
    }

    if (showData) display.close();
    System.out.println("\rData successfully exported");
    return data;
  }
}
