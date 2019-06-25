import network.*;
import network.data.*;
import network.linear_algebra.*;
import network.ui.*;

public class Test {

  public static void main(String[] args) {
    Window w;
    DataLoader loder = new DataLoader();

    loder.loadMNIST(true);
  }

}
