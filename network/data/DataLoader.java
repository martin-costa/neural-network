package network.data;

import network.ui.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.awt.image.*;

public class DataLoader {

  //make display window for data being loaded
  Window display;

  //this method is to load in the data from the MNIST database
  public void loadMNIST(boolean showData) {
    if (showData) display = new Window();

    int res = 28; //all images have a resolution of 28x28

    FileInputStream images;
    FileInputStream labels;

    String inputImagePath = "network/data/mnist/train-images.idx3-ubyte";
    String inputLabelPath = "network/data/mnist/train-labels.idx1-ubyte";

    // String inputImagePath = "network/data/mnist/t10k-images.idx3-ubyte";
    // String inputLabelPath = "network/data/mnist/t10k-labels.idx1-ubyte";

    //String outputPath = "";

    try {
      images = new FileInputStream(inputImagePath);
      labels = new FileInputStream(inputLabelPath);

      //skip magic number
      images.skip(4);

      //get image count, stored as 32 bit integer so requires next 4 bytes
      int imageCount = (images.read() << 24) | (images.read() << 16) | (images.read() << 8) | (images.read());

      //skip rest to labels and pixels
      images.skip(8);
      labels.skip(8);

      double[][] pixels = new double[res][res];

      for(int k = 0; k < imageCount; k++) {
        if(k % 100 == 0) System.out.println("Number of images extracted: " + k);

        for (int j = 0; j < res; j++) {
          for (int i = 0; i < res; i++) {
            pixels[i][j] = images.read()/255d;
          }
        }

        //update the display window
        if (showData) {
          display.display(pixels);
          display.update();
        }

        //sleep
        try {
          Thread.sleep(400);
        }
        catch(Exception e) {}

        //some output shi
        System.out.println(labels.read());
      }

    }
    catch (Exception e) {
      System.err.println("error");
    }

    System.out.println("All images successfully exctracted");
    if (showData) display.close();
  }
}
