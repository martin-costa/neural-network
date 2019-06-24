package network.linear_algebra;

public class Vector extends Matrix {

  public Vector(int m) {
    super(m, 1);
  }

  public Vector(double[] elts) {
    super(elts.length, 1);
    double[][] elts2 = new double[1][];
    elts2[0] = elts;
    super.setMatrix(elts2);
  }

  public Vector(Vector V) {
    super(V);
  }

  public void set(int m, double e) {
    super.set(m,1,e);
  }

  public double get(int m) {
    return super.get(m,1);
  }

  public Vector add(Vector V) {
    return super.add(V).toVector();
  }

  public double dot(Vector x) {
    if (super.m != x.m) {
      System.out.println("error: vectors have different dimensions");
      return 0;
    }
    double dotProduct = 0;
    for (int i = 1; i <= x.m; i++) {
      dotProduct += super.get(i,1) * x.get(i,1);
    }
    return dotProduct;
  }
}
