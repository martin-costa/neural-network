import network.*;
import network.data.*;
import network.linear_algebra.*;
import network.ui.*;

public class Test {

  public static void main(String[] args) {
    Window w;

    NumberData testData = DataLoader.loadMNISTTraining(false);

    //loder.loadMNIST(true);
    Vector V = new Vector(3);
    Vector U = new Vector(V);

    Network net = new Network(784, 30, 10);
  }
}
