package network.linear_algebra;

import java.util.Random;

public class Matrix {
  public final int m; //rows
  public final int n; //columns

  protected double[][] elts;

  public Matrix(int m, int n) {
    this.m = m;
    this.n = n;
    elts = new double[m][n];
    fillGaussian();
  }

  public Matrix(int m, int n, double x) {
    this.m = m;
    this.n = n;
    elts = new double[m][n];
    fillMatrix(x);
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

  private void initialiseMatrix() {
    elts = new double[m][n];
    fillGaussian();
  }

  private void setMatrix(Matrix M) {
    elts = new double[m][n];
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        set(i,j, M.get(i,j));
      }
    }
  }

  protected void setMatrix(double[][] elts) {
    this.elts = new double[m][n];
    try {
      for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
          set(i,j, elts[i - 1][j - 1]);
        }
      }
    }
    catch(ArrayIndexOutOfBoundsException ex) {
      System.out.println("error: array not a valid matrix");
    }
  }

  public void set(int m, int n, double e) {
    try {
      this.elts[m - 1][n - 1] = e;
    }
    catch(ArrayIndexOutOfBoundsException ex) {
      System.err.println("error: index out of bounds");
    }
  }

  public double get(int m, int n) {
    try {
      return this.elts[m - 1][n - 1];
    }
    catch(ArrayIndexOutOfBoundsException ex) {
      System.err.println("error: index out of bounds");
    }
    return 0;
  }

  public Matrix add(Matrix M) {
    if (this.m != M.m || this.n != M.n) {
      System.err.println("error: incomplatible matrix sizes");
      return null;
    }
    Matrix A = new Matrix(this.m, this.n);
    for (int i = 1; i <= this.m; i++) {
      for (int j = 1; j <= this.n; j++) {
        A.set(i, j, this.get(i,j) + M.get(i,j));
      }
    }
    return A;
  }

  public Matrix mult(Matrix M) {
    if (this.n != M.m) {
      System.err.println("error: incomplatible matrix sizes");
      return null;
    }
    Matrix A = new Matrix(this.m, M.n, 0d);
    for (int i = 1; i <= M.n; i++) {
      for (int j = 1; j <= this.m; j++) {
        for (int k = 1; k <= this.n; k++) {
          A.set(j, i, A.get(j,i) + (this.get(j,k) * M.get(k,i)) );
        }
      }
    }
    return A;
  }

  public Vector mult(Vector V) {
    return mult((Matrix)V).toVector();
  }

  public Matrix fillMatrix(double e) {
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        set(i,j,e);
      }
    }
    return this;
  }

  public Matrix fillGaussian() {
    Random gen = new Random();
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        set(i, j, gen.nextGaussian());
      }
    }
    return this;
  }

  public Vector toVector() {
    if (this.n != 1) {
      return null;
    }
    Vector V = new Vector(this.m);
    for (int i = 1; i <= this.m; i++) {
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
