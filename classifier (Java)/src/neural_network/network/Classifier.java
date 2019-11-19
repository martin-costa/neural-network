package neural_network.network;

import neural_network.data.NumberData;
import neural_network.linear_algebra.Vector;

public interface Classifier {

  //has the ability to classify a single image in vector form
  public int classify(Vector image);

  //has the ability to evaluate a set of data
  public int evaluate(NumberData testData);
}
