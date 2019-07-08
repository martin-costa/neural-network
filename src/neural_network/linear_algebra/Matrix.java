package neural_network.linear_algebra;

import java.util.Random;

public class Matrix {
  public final int m; //rows
  public final int n; //columns

  private double[][] elts;

  public Matrix(int m, int n) {
    this.m = m;
    this.n = n;
    elts = new double[m][n];
  }

  public Matrix(int m, int n, double x) {
    this.m = m;
    this.n = n;
    elts = new double[m][n];
    fill(x);
  }

  public Matrix(double[][] elts) {
    this.m = elts.length;
    this.n = elts[0].length;
    setMatrix(elts);
  }

  public Matrix(Matrix M) { //constructor creates copy if input
    this.m = M.m;
    this.n = M.n;
    setMatrix(M);
  }

  private void setMatrix(Matrix M) {
    elts = new double[m][n];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        set(i,j, M.get(i,j));
      }
    }
  }

  private void setMatrix(double[][] elts) {
    this.elts = new double[m][n];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        this.elts[i][j] = elts[i][j];
      }
    }
  }

  public void set(int m, int n, double e) {
    this.elts[m][n] = e;
  }

  public double get(int m, int n) {
    return this.elts[m][n];
  }

  public Matrix add(Matrix M) {
    if (this.m != M.m || this.n != M.n) {
      System.err.println("error: incomplatible matrix sizes");
      return null;
    }
    Matrix A = new Matrix(this.m, this.n);
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++) {
        A.set(i, j, this.get(i,j) + M.get(i,j));
      }
    }
    return A;
  }

  public Matrix add2(Matrix M) {
    if (this.m != M.m || this.n != M.n) {
      System.err.println("error: incomplatible matrix sizes");
      return null;
    }
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++) {
        set(i, j, get(i,j) + M.get(i,j));
      }
    }
    return this;
  }

  public Matrix mult(Matrix M) {
    if (this.n != M.m) {
      System.err.println("error: incomplatible matrix sizes");
      return null;
    }
    Matrix A = new Matrix(this.m, M.n, 0d);
    for (int i = 0; i < M.n; i++) {
      for (int j = 0; j < this.m; j++) {
        for (int k = 0; k < this.n; k++) {
          A.set(j, i, A.get(j,i) + (this.get(j,k) * M.get(k,i)) );
        }
      }
    }
    return A;
  }

  public Matrix mult(double x) {
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        elts[i][j] *= x;
      }
    }
    return this;
  }

  public Matrix transpose() {
    Matrix M = new Matrix(this.n, this.m, 0);
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        M.set(j, i, get(i, j));
      }
    }
    return M;
  }

  public Vector mult(Vector V) {
    if (this.n != V.m) {
      System.err.println("error: incomplatible matrix sizes");
      return null;
    }
    Vector A = new Vector(this.m, 0d);
    for (int j = 0; j < this.m; j++) {
      for (int k = 0; k < this.n; k++) {
        A.set(j, A.get(j) + (this.get(j,k) * V.get(k)) );
      }
    }
    return A;
  }

  public Matrix fill(double e) {
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        elts[i][j] = e;
      }
    }
    return this;
  }

  public Matrix fillGaussian(double mu, double sigma) {
    Random gen = new Random();
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        elts[i][j] = gen.nextGaussian()*sigma + mu;
      }
    }
    return this;
  }

  public Vector toVector() {
    if (this.n != 1) {
      return null;
    }
    Vector V = new Vector(this.m);
    for (int i = 0; i < this.m; i++) {
      V.set(i, this.get(i,1));
    }
    return V;
  }

  public String toString(int d) {
    String end = " ;";
    if (d >= 10) {
      end = " ]\n[";
      d = (int)Math.abs(d - 10);
    }
    String s = "[";
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        s += " " + String.format("%."+d+"f", this.elts[i][j]);
      }
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
