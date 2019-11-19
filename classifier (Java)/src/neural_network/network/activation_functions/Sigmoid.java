package neural_network.network.activation_functions;

import neural_network.linear_algebra.*;

public class Sigmoid {

  //sigmoid
  public static double f(double x) {
    return 1d/(1d + Math.exp(-x));
  }

  public static Vector f(Vector x) {
    for (int i = 0; i < x.m; i++) {
      x.set(i , f(x.get(i)));
    }
    return x;
  }

  //derivative of sigmoid
  public static double fPrime (double x) {
    return f(x)*(1d - f(x));
  }

  public static Vector fPrime(Vector x) {
    for (int i = 0; i < x.m; i++) {
      x.set(i , fPrime(x.get(i)));
    }
    return x;
  }
}
