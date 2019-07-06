import network.*;
import network.data.*;
import network.linear_algebra.*;
import network.ui.*;

public class Test {

  public static void main(String[] args) {

    // NumberData trainingData = DataLoader.loadMNISTTraining(false);
    // NumberData testData = DataLoader.loadMNISTTest(false);
    //
    // Network network = new Network(784, 30, 10);
    //
    // network.stochasticGradientDescent(15, 10, 3, trainingData, testData);
    //
    // trainingData = null;
    // testData = null;
    //
    // NetworkLoader.storeNetwork("PretrainedNetwork1", network);

    Network network = NetworkLoader.loadNetwork("PretrainedNetwork1");

    System.out.println(network.evaluate(DataLoader.loadMNISTTest(false)));

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
}
