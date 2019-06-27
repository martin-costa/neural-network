package network.linear_algebra;

import java.util.Random;

public class Vector {
  public final int m; //rows

  private double[] elts;

  public Vector(int m) {
    this.m = m;
    elts = new double[m];
    fillGaussian();
  }

  public Vector(int m, double x) {
    this.m = m;
    elts = new double[m];
    fillVector(x);
  }

  public Vector(double[] elts) {
    this.m = elts.length;
    this.elts = elts;
  }

  public Vector(Vector V) {
    this.m = V.m;
    setVector(V);
  }

  public void setVector(Vector V) {
    elts = new double[V.m];
    for (int i = 0; i < m; i++) {
      set(i, V.get(i));
    }
  }

  public void set(int m, double x) {
    elts[m] = x;
  }

  public double get(int m) {
    return elts[m];
  }

  public Vector add(Vector V) {
    if (this.m != V.m) {
      System.err.println("error: incomplatible vector sizes");
      return null;
    }
    Vector U = new Vector(m);
    for (int i = 0; i < m; i++) {
      U.set(i, this.get(i) + V.get(i));
    }
    return U;
  }

  public Vector mult(double x) {
    for (int i = 0; i < m; i++) {
      elts[i] *= x;
    }
    return this;
  }

  public Matrix mult(Vector x) {
    Matrix M = new Matrix(this.m, x.m);
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < x.m; j++) {
        M.set(i, j, this.get(i)*x.get(j));
      }
    }
    return M;
  }

  public double dot(Vector x) {
    if (this.m != x.m) {
      System.out.println("error: vectors have different dimensions");
      return 0;
    }
    double dotProduct = 0;
    for (int i = 0; i < x.m; i++) {
      dotProduct += this.get(i) * x.get(i);
    }
    return dotProduct;
  }

  public Vector schur(Vector x) {
    if (this.m != x.m) {
      System.out.println("error: vectors have different dimensions");
      return null;
    }
    Vector y = new Vector(x);
    for (int i = 0; i < m; i++) {
      y.set(i, y.get(i) * this.get(i));
    }
    return y;
  }

  public double length() {
    double length = 0;
    for (int i = 0; i < m; i++) {
      length += Math.pow(get(i), 2);
    }
    return Math.sqrt(length);
  }

  public int maxIndex() {
    int j = 0;
    for (int i = 1; i < m; i++) {
      if (elts[i] > elts[j]) {
        j = i;
      }
    }
    return j;
  }

  public Vector fillVector(double e) {
    for (int i = 0; i < m; i++) {
      elts[i] = e;
    }
    return this;
  }

  public Vector fillGaussian() {
    Random gen = new Random();
    for (int i = 0; i < m; i++) {
      elts[i] = gen.nextGaussian();
    }
    return this;
  }

  public String toString(int d) {
    String end = " ;";
    if (d >= 10) {
      end = " ]\n[";
      d = (int)Math.abs(d - 10);
    }
    String s = "[";
    for (int i = 0; i < m; i++) {
      s += " " + String.format("%."+d+"f", this.elts[i]);
      if (i < m - 1) {
        s += end;
      }
    }
    s += " ]";
    return s;
  }

  public String toString() {
    return toString(5);
  }

}
