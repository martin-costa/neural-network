import network.*;
import network.data.*;
import network.linear_algebra.*;
import network.ui.*;

public class Test {

  public static void main(String[] args) {

    NumberData trainingData = DataLoader.loadMNISTTraining(false);
    NumberData testData = DataLoader.loadMNISTTest(false).randomize();

    Network net = new Network(784, 30, 10);

    net.stochasticGradientDescent(5, 100, 3, trainingData, testData);

    Window W = new Window();

    for (int i = 0; i < 60000; i++) {
      //System.out.println(net.feedForward(trainingData.getImage(i)).toString(2));
      //System.out.println(trainingData.getResult(i).toString(2) + "  " + trainingData.getNumber(i));

      System.out.println(trainingData.getNumber(i));
      W.display(trainingData.getImage(i));
      W.update();

      try {
        Thread.sleep(400);
      }
      catch(Exception e) {}
    }
    for (int i = 0; i < 10000; i++) {
      System.out.println(net.feedForward(testData.getImage(i)).toString(2));
      System.out.println(testData.getResult(i).toString(2) + "  " + testData.getNumber(i));

      System.out.println(testData.getNumber(i));
      W.display(testData.getImage(i));
      W.update();
    }

    W.close();
  }
}
