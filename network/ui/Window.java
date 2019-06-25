package network.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.*;

public class Window {
  private JFrame window;
  private DrawingPanel panel;

  private MouseInputs mouseInputs;
  private KeyInputs keyInputs;

  private Point mousePos = new Point();

  private final int resolution = 28;
  private final int pixelWidth = 15;
  private double[][] pixels;

  private boolean drawMode;

  public Window() {
    drawMode = true;

    //create window and DrawingPanel
    window = new JFrame("Neural Network");
    panel = new DrawingPanel();

    //create pixel array
    pixels = new double[resolution][resolution];

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

    window.setSize(new Dimension(resolution*pixelWidth, resolution*pixelWidth));
    window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public void update() {
    mousePos = MouseInfo.getPointerInfo().getLocation();

    //draw on the window by left clicking
    if (mouseInputs.leftPressed && drawMode)
      draw((int)(mousePos.getX() - window.getX()), (int)(mousePos.getY() - window.getY()));

    //move window by right clicking
    if (mouseInputs.rightPressed)
      move();

    //update the windows image
    panel.repaint();
  }

  //display imaage passed into the method, turns off drawmode
  public void display(double[][] pixels) {
    this.pixels = pixels;
    drawMode = false;
  }

  //update methods

  //draw on the window
  public void draw(int x , int y) {
    try{
      pixels[x/pixelWidth][y/pixelWidth] = 1;
      pixels[x/pixelWidth + 1][y/pixelWidth] = 1;
      pixels[x/pixelWidth - 1][y/pixelWidth] = 1;
      pixels[x/pixelWidth][y/pixelWidth + 1] = 1;
      pixels[x/pixelWidth][y/pixelWidth - 1] = 1;

      if (pixels[x/pixelWidth + 1][y/pixelWidth + 1] < 1) {
        pixels[x/pixelWidth + 1][y/pixelWidth + 1] = Math.random();
      }
      if (pixels[x/pixelWidth + 1][y/pixelWidth - 1] < 1) {
        pixels[x/pixelWidth + 1][y/pixelWidth - 1] = Math.random();
      }
      if (pixels[x/pixelWidth - 1][y/pixelWidth + 1] < 1) {
        pixels[x/pixelWidth - 1][y/pixelWidth + 1] = Math.random();
      }
      if (pixels[x/pixelWidth - 1][y/pixelWidth - 1] < 1) {
        pixels[x/pixelWidth - 1][y/pixelWidth - 1] = Math.random();
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
    for (int i = 0; i < pixels.length; i++) {
      for (int j = 0; j < pixels.length; j++) {
        pixels[i][j] = 0;
      }
    }
  }

  //close the window
  public void close() {
    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
  }



  class DrawingPanel extends JPanel {

    @Override
    public void paint(Graphics g) {
      for (int i = 0; i < resolution; i++) {
        for (int j = 0; j < resolution; j++) {
          double c = pixels[i][j];
          int intensity = (int)((1 - c)*255);
          g.setColor(new Color(intensity, intensity, intensity, 255));
          g.fillRect(i*pixelWidth, j*pixelWidth, pixelWidth, pixelWidth);
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
        reset();
      }
      if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        close();
      }
    }

    public void keyTyped(KeyEvent e) {
      if (Character.toLowerCase(e.getKeyChar()) == 'd') {
        drawMode = !drawMode;
        reset();
      }
    }
  }

}
