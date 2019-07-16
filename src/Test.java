import neural_network.network.*;
import neural_network.data.*;
import neural_network.linear_algebra.*;
import neural_network.ui.*;

public class Test {

  public static void main(String[] args) {

    //let user load or train a network
    if (args.length > 1 && args[0].equals("load")) {
      useNetwork(loadNetwork(args[1], true));
    }

    if (args.length > 4 && args[0].equals("train")) {
      useNetwork(trainNetwork(args));
    }

    //run this code if neither of the above arguments used =>

    //TRAIN AND STORE A NETWORK

    // NumberData trainingData = DataLoader.loadMNISTTraining(false, true);
    // NumberData testData = DataLoader.loadMNISTTest(false, true);
    //
    // Network2 network = new Network2(784, 100, 10);
    // network.stochasticGradientDescent(0, 10, 0.2, 5, trainingData, testData);
    // NetworkLoader.storeNetwork("net6", network);
    //
    // trainingData = null;
    // testData = null;
    // useNetwork(network);

    //LOAD AND TEST MULTNETWORK (note: net 2 & 3 and weaker)

    NumberData testData = DataLoader.loadMNISTTest(false, true);
    MultiNetwork multiNetwork = NetworkLoader.loadNetworks("net1", "net2", "net5");
    useNetwork(multiNetwork);
  }

  //allow user input into the netowrk
  public static <T extends Classifier> void useNetwork(T classifier) {
    Window display = new Window();
    int i = 0;

    while(true) {

      if (i++ % 2500 == 0) {
        display.draw();
        System.out.print("\r" + classifier.classify(display.getPixels()));
      }
      display.update();
    }
  }

  //loads network with name path
  public static Network loadNetwork(String path, boolean test) {
    Network network = NetworkLoader.loadNetwork(path);
    System.out.println("loading network: " + path);
    if (test) System.out.println(network.evaluate(DataLoader.loadMNISTTest(false, false)) + "/10000 images classified correctly");
    return network;
  }

  //trains a new network with inputs as hyperparameters
  public static Network1 trainNetwork(String[] args) {
    int layerCount = args.length - 4;
    int[] layers = new int[layerCount];

    for (int i = 0; i < layerCount; i++) {
      layers[i] = Integer.valueOf(args[4 + i]);
    }
    Network1 network = new Network1(layers);
    network.stochasticGradientDescent(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Double.valueOf(args[3]), DataLoader.loadMNISTTraining(false, false), DataLoader.loadMNISTTest(false, false));
    return network;
  }
}
