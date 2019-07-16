package neural_network.network;

import java.util.ArrayList;

import neural_network.network.*;
import neural_network.data.*;
import neural_network.linear_algebra.*;
import neural_network.ui.*;

public class MultiNetwork implements Classifier{

  private ArrayList<Network> networks;

  public MultiNetwork() {
    this.networks = new ArrayList<>();
  }

  public MultiNetwork(Network... networks) {
    this.networks = new ArrayList<>();
    for (int i = 0; i < networks.length; i++) {
      this.networks.add(networks[i]);
    }
  }

  //classify by pooling, generally better
  public int classify(Vector image) {
    Vector results = new Vector(10, 0);
    for (Network network : networks) {
      results = results.add(network.feedForward(image));
    }
    return results.maxIndex();
  }

  //classify by voting
  public int classify2(Vector image) {
    Vector results = new Vector(10, 0);
    for (Network network : networks) {
      Vector v = new Vector(10, 0);
      v.set(network.classify(image), 1);
      results = results.add(v);
    }
    return results.maxIndex();
  }

  public int evaluate(NumberData testData) {
    int j = 0;
    for (int i = 0; i < testData.size; i++) {
      if (classify(testData.getImage(i)) == testData.getNumber(i)) j++;
    }
    return j;
  }

  public void addNetowrk(Network network) {
    this.networks.add(network);
  }
}
