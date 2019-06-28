package network.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import network.linear_algebra.*;

public class Window {
  private JFrame window;
  private DrawingPanel panel;

  private MouseInputs mouseInputs;
  private KeyInputs keyInputs;

  private Point mousePos = new Point();

  //display reslution is the resultion the iumages are drawn in
  //image resultion is the resultion of the images that need to
  //be exported and displayed
  private final int displayResolution = 280;
  private final int imageResolution = 28;

  private final int pixelWidth = 20;

  private Vector imagePixels;
  private Vector displayPixels;

  private boolean drawMode;

  public Window() {
    drawMode = true;

    //create window and DrawingPanel
    window = new JFrame("Neural Network");
    panel = new DrawingPanel();

    //create pixel array
    imagePixels = new Vector(imageResolution*imageResolution, 0);
    displayPixels = new Vector(displayResolution*displayResolution, 0);

    //create input handlers
    mouseInputs = new MouseInputs();
    keyInputs = new KeyInputs();

    //add inputs to window
    window.addMouseListener(mouseInputs);
    window.addKeyListener(keyInputs);

    //initial location of window
    window.setLocation(900, 300);

    //add panel to window
    window.add(panel);

    //window settings
    window.setUndecorated(true);
    window.setResizable(false);
    window.setVisible(true);

    window.setSize(new Dimension(imageResolution*pixelWidth, imageResolution*pixelWidth));
    window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public void update() {

    //get mouse position
    mousePos = MouseInfo.getPointerInfo().getLocation();

    //move window by right clicking
    if (mouseInputs.rightPressed)
      move();

    if (drawMode) {
      //draw on the window by left clicking
      if (mouseInputs.leftPressed && drawMode)
        draw((int)(mousePos.getX() - window.getX()), (int)(mousePos.getY() - window.getY()));
    }

    //update the windows image
    panel.repaint();
  }

  //display imaage passed into the method, turns off drawmode
  public void display(Vector imagePixels) {
    this.imagePixels = imagePixels;
    drawMode = false;
  }

  //draw on the window
  public void draw(int x , int y) {
    try{
      x = 10*x/pixelWidth;
      y = 10*y/pixelWidth;
      int r = 10;

      for (int i = x - r; i <= x + r; i++) {
        for (int j = y - r; j <= y + r; j++) {
          if (Math.pow(i - x, 2) + Math.pow(j - y, 2) < r*r) {
            displayPixels.set(i + j*displayResolution, 1);
          }
        }
      }
    }
    catch(ArrayIndexOutOfBoundsException e) {}
  }

  //move the window to mouse position
  public void move() {
    window.setLocation((int)mousePos.getX(), (int)mousePos.getY());
  }

  //clear the window
  public void reset() {
    imagePixels = new Vector(imageResolution*imageResolution, 0);
    displayPixels = new Vector(displayResolution*displayResolution, 0);
  }

  //close the window
  public void close() {
    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
  }

  public Vector getPixels() {
    processDrawing();
    return imagePixels;
  }

  public void processDrawing() {

    //change reslution to imageResolution
    for (int i = 0; i < imageResolution; i++) {
      for (int j = 0; j < imageResolution; j++) {
        double totalColor = 0;

        for (int i0 = 0; i0 < 10; i0++) {
          for (int j0 = 0; j0 < 10; j0++) {
            totalColor += displayPixels.get(i*10 + i0 + (j*10 + j0)*displayResolution);
          }
        }
        imagePixels.set(i + j*imageResolution, totalColor/100d);
      }
    }

    //centre by centre of mass
    double x = 0;
    double y = 0;
    double m = 0;
    for (int i = 0; i < imageResolution; i++) {
      for (int j = 0; j < imageResolution; j++) {
        m += imagePixels.get(i + j*imageResolution);
        x += i*imagePixels.get(i + j*imageResolution);
        y += j*imagePixels.get(i + j*imageResolution);
      }
    }
    x /= m;
    y /= m;

    int dx = (int)(x - imageResolution/2);
    int dy = (int)(y - imageResolution/2);

    Vector buffer = new Vector(imageResolution*imageResolution);
    for (int i = 0; i < imageResolution; i++) {
      for (int j = 0; j < imageResolution; j++) {
        try {
          buffer.set(i + j*imageResolution, imagePixels.get(i + dx + (j + dy)*imageResolution));
        }
        catch(Exception e) {
          buffer.set(i + j*imageResolution, 0);
        }
      }
    }

    imagePixels = buffer;
  }

  class DrawingPanel extends JPanel {

    @Override
    public void paint(Graphics g) {

      //draw imagePixels if not in drawing mode
      if (!drawMode) {
        for (int i = 0; i < imageResolution; i++) {
          for (int j = 0; j < imageResolution; j++) {
            double c = imagePixels.get(i + j*imageResolution);
            int intensity = (int)((1 - c)*255);
            g.setColor(new Color(intensity, intensity, intensity, 255));
            g.fillRect(i*pixelWidth, j*pixelWidth, pixelWidth, pixelWidth);
          }
        }
      }

      //if in drawing mode draw displayPixels
      else {
        for (int i = 0; i < displayResolution; i++) {
          for (int j = 0; j < displayResolution; j++) {
            double c = displayPixels.get(i + j*displayResolution);
            int intensity = (int)((1 - c)*255);
            g.setColor(new Color(intensity, intensity, intensity, 255));
            g.fillRect(i*pixelWidth/10, j*pixelWidth/10, pixelWidth/10, pixelWidth/10);
          }
        }
      }
    }
  }

  //handles all the mouse inputs
  class MouseInputs extends MouseAdapter {
    boolean leftPressed;
    boolean rightPressed;

    @Override
    public void mousePressed(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) leftPressed = true;
      if (e.getButton() == MouseEvent.BUTTON3) rightPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) leftPressed = false;
      if (e.getButton() == MouseEvent.BUTTON3) rightPressed = false;
    }
  }

  //handles all of the key inputs
  class KeyInputs extends KeyAdapter {

    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_R && drawMode) {
        System.out.println("Resetting");
        reset();
      }
      if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        close();
      }
    }

    public void keyTyped(KeyEvent e) {
      if (Character.toLowerCase(e.getKeyChar()) == 'd') {
        drawMode = !drawMode;
        System.out.println("Drawmode: " + drawMode);
        reset();
      }
    }
  }

}
