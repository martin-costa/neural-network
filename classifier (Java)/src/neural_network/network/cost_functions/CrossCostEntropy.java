package neural_network.network.cost_functions;

import neural_network.network.activation_functions.*;
import neural_network.linear_algebra.*;

//this assumes that the activation function used is the sigmoid function

public class CrossCostEntropy {

  //cost of this function for an output a
  public static double f(Vector a, Vector y) {
    return 0; // TODO:
  }

  //return error in final layer, more useful than cost derivative since allows
  //for algebraic manipulation
  public static Vector finalError(Vector z, Vector a, Vector y) {
    return a.add(new Vector(y).mult(-1));
  }
}
