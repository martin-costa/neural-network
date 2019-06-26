import network.*;
import network.data.*;
import network.linear_algebra.*;
import network.ui.*;

public class Test {

  public static void main(String[] args) {

    //NumberData trainingData = DataLoader.loadMNISTTraining(true);
    NumberData testData = DataLoader.loadMNISTTest(true);

    Network net = new Network(784, 30, 10);

    System.out.println(net.evaluate(testData) + "/10000 images correctly classified");

    // Window W = new Window();
    // for (int i = 0; i < 60000; i++) {
    //   System.out.println(net.feedForward(trainingData.images[i]).toString(2));
    //   System.out.println(trainingData.results[i].toString(2) + "  " + trainingData.numbers[i]);
    //   W.display(trainingData.images[i]);
    //   W.update();
    // }
    // for (int i = 0; i < 10000; i++) {
    //   System.out.println(net.feedForward(testData.images[i]).toString(2));
    //   System.out.println(testData.results[i].toString(2) + "  " + testData.numbers[i]);
    //   W.display(testData.images[i]);
    //   W.update();
    // }
    // W.close();
  }
}
