import network.*;
import network.data.*;
import network.linear_algebra.*;
import network.ui.*;

public class Test {

  public static void main(String[] args) {

    if (args.length > 1 && args[0].equals("load")) {
      useNetwork(loadNetwork(args[1]));
    }

    if (args.length > 4 && args[0].equals("train")) {
      useNetwork(trainNetwork(args));
    }

    //run this code if neither of the above arguments used =>

    // NumberData trainingData = DataLoader.loadMNISTTraining(false, true);
    // NumberData testData = DataLoader.loadMNISTTest(false, true);
    //
    // Network network = new Network(784, 30, 10);
    //
    // network.stochasticGradientDescent(2, 10, 3, trainingData, testData);
    //
    // trainingData = null;
    // testData = null;
  }

  //allow user input into the netowrk
  public static void useNetwork(Network network) {
    Window display = new Window();
    int i = 0;

    while(true) {
      i++;
      if (i % 1000 == 0) {
        System.out.print("\r" + network.classify(display.getPixels()));
        display.update();
      }
    }
  }

  //loads network with name path
  public static Network loadNetwork(String path) {
    Network network = NetworkLoader.loadNetwork(path);
    System.out.println("loading network: " + path);
    System.out.println(network.evaluate(DataLoader.loadMNISTTest(false, false)) + "/10000 images classified correctly");
    return network;
  }

  //trains a new network with inputs as hyperparameters
  public static Network trainNetwork(String[] args) {
    int layerCount = args.length - 4;
    int[] layers = new int[layerCount];

    for (int i = 0; i < layerCount; i++) {
      layers[i] = Integer.valueOf(args[4 + i]);
    }
    Network network = new Network(layers);
    network.stochasticGradientDescent(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Double.valueOf(args[3]), DataLoader.loadMNISTTraining(false, false), DataLoader.loadMNISTTest(false, false));
    return network;
  }

}
