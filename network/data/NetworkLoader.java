package network.data;

import java.io.*;

import network.linear_algebra.*;
import network.ui.*;
import network.*;

public class NetworkLoader {

  public static Network loadNetwork(String path) {
    int[] layers = null;
    Vector[] biases = null;
    Matrix[] weights = null;

    try {
      DataInputStream reader = new DataInputStream(new FileInputStream("network/data/stored_networks/" + path));

      int layerCount = reader.readInt();

      layers = new int[layerCount];

      for (int i = 0; i < layerCount; i++) {
        layers[i] = reader.readInt();
      }

      biases = new Vector[layerCount - 1];
      for (int i = 0; i < layerCount - 1; i++) {
        biases[i] = new Vector(layers[i + 1]);

        for (int j = 0; j < layers[i + 1]; j++) {
          biases[i].set(j, reader.readDouble());
        }
      }

      weights = new Matrix[layerCount - 1];
      for (int i = 0; i < layerCount - 1; i++) {
        weights[i] = new Matrix(layers[i + 1], layers[i]);

        for (int j = 0; j < layers[i + 1]; j++) {
          for (int k = 0; k < layers[i]; k++) {
            weights[i].set(j, k, reader.readDouble());
          }
        }
      }

      reader.close();
    }
    catch(IOException e) {
      System.out.println("error loading network" + e);
    }

    return new Network(layers, weights, biases);
  }

  public static void storeNetwork(String path, Network network) {

    try {
      //create an output stream to write data into file
      DataOutputStream writer = new DataOutputStream(new FileOutputStream("network/data/stored_networks/" + path));

      //write amount of layers first
      writer.writeInt(network.getLayerCount());

      for (int i : network.getLayers()) {
        writer.writeInt(i);
      }

      for (Vector v : network.getBiases()) {
        for (int i = 0; i < v.m; i++) {
          writer.writeDouble(v.get(i));
        }
      }

      for (Matrix v : network.getWeights()) {
        for (int i = 0; i < v.m; i++) {
          for (int j = 0; j < v.n; j++) {
            writer.writeDouble(v.get(i,j));
          }
        }
      }

      writer.close();
    }
    catch(IOException e) {
      System.out.println("error storing network" + e);
    }

  }

}
