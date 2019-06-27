import network.*;
import network.data.*;
import network.linear_algebra.*;
import network.ui.*;

public class Test {

  public static void main(String[] args) {

    NumberData trainingData = DataLoader.loadMNISTTraining(false);
    NumberData testData = DataLoader.loadMNISTTest(false).randomize();

    Network net = new Network(784, 30, 10);

    net.stochasticGradientDescent(30, 10, 3, trainingData, testData);

    Window W = new Window();

    W.close();
  }
}
