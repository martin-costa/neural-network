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

  //loads the MNIST training data
  public static NumberData loadMNISTTraining(boolean showData) {
    try {
      return loadMNIST(showData, "train");
    }
    catch(IOException e) {
      System.out.println("\rError loading training data");
      return null;
    }
  }

  //loads the MNIST test data
  public static NumberData loadMNISTTest(boolean showData) {
    try {
      return loadMNIST(showData, "t10k");
    }
    catch(IOException e) {
      System.out.println("\rError loading test data");
      return null;
    }
  }

  //this method loads in the data from the MNIST database
  private static NumberData loadMNIST(boolean showData, String path) throws IOException {
    //make display window for data being loaded
    Window display = null;

    if (showData) display = new Window();

    int res = 28; //all MNIST images have a resolution of 28x28

    //the data
    NumberData data = null;

    FileInputStream images = new FileInputStream("network/data/mnist/" + path + "-images.idx3-ubyte");
    FileInputStream labels = new FileInputStream("network/data/mnist/" + path + "-labels.idx1-ubyte");

    //skip magic number
    images.skip(4);

    //get image count, stored as 32 bit integer so requires next 4 bytes
    int imageCount = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());

    //skip rest to labels and pixels
    images.skip(8);
    labels.skip(8);

    Vector pixels = new Vector(res*res, 0);

    //create the data
    data = new NumberData(imageCount);

    for(int j = 0; j < imageCount; j++) {
      if(j % 50 == 0) System.out.print("\r" + j + "/" + imageCount + " images loaded");

      byte[] pixelData = new byte[res*res];

      images.read(pixelData);
      for (int i = 0; i < res*res; i++) {
        pixels.set(i, ((int)pixelData[i] & 0xff)/255d);
      }

      //update the display window
      if (showData) {
        display.display(pixels);
        display.update();
      }

      data.addData(j, labels.read(), pixels);
    }

    if (showData) display.close();
    System.out.println("\r" + imageCount + "/" + imageCount + " images loaded");
    return data;
  }
}
