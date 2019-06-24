package network.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.MouseInfo;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.*;

import network.linear_algebra.*;

public class Window {
  private JFrame window;
  private DrawingPanel panel;
  private MouseInputs mouseInputs;
  private KeyInputs keyInputs;

  private int mouseX = 0;
  private int mouseY = 0;


  private Color backgroundColor = new Color(255,255,255,255);

  private final int resolution = 28;
  private final int pixelWidth = 15;
  private final int width;
  private double[][] pixels;

  public Window() {
    this.window = new JFrame("Neural Network");

    this.width = resolution*pixelWidth;

    this.pixels = new double[resolution][resolution];

    this.panel = new DrawingPanel(resolution, pixelWidth);

    this.mouseInputs = new MouseInputs(pixels);
    this.keyInputs = new KeyInputs(pixels);

    //add inputs to panel
    this.window.addMouseListener(mouseInputs);
    this.window.addKeyListener(keyInputs);

    this.window.setLocation(1400, 300);

    this.window.add(this.panel);

    this.window.setUndecorated(true);
    this.window.setResizable(false);
    this.window.setVisible(true);

    this.window.setSize(new Dimension(this.width, this.width));
    this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void update() {
    mouseX = (int)MouseInfo.getPointerInfo().getLocation().getX();
    mouseY = (int)MouseInfo.getPointerInfo().getLocation().getY();

    mouseInputs.draw(mouseX - this.window.getX(), mouseY - this.window.getY());

    //update here
    this.panel.repaint();
    this.panel.setBackground(backgroundColor);
  }

  class DrawingPanel extends JPanel {
    private final int resolution;
    private final int width;
    private final int pixelWidth;

    DrawingPanel(int resolution, int pixelWidth) {
      this.resolution = resolution;
      this.pixelWidth = pixelWidth;
      this.width = resolution*pixelWidth;
    }

    @Override
    public void paint(Graphics g) {
      for (int i = 0; i < resolution; i++) {
        for (int j = 0; j < resolution; j++) {
          double c = pixels[i][j];
          g.setColor(new Color((int)((1 - c)*255), (int)((1 - c)*255), (int)((1 - c)*255), 255));
          g.fillRect(i*this.pixelWidth, j*this.pixelWidth, this.pixelWidth, this.pixelWidth);
        }
      }
    }

  }

  class MouseInputs extends MouseAdapter {
    double[][] pixels;
    boolean pressed;

    MouseInputs(double[][] pixels) {
      this.pixels = pixels;
    }

    public void draw(int x , int y) {
      if (pressed) {
        try{
          this.pixels[x/pixelWidth][y/pixelWidth] = 1;
          this.pixels[x/pixelWidth + 1][y/pixelWidth] = 1;
          this.pixels[x/pixelWidth - 1][y/pixelWidth] = 1;
          this.pixels[x/pixelWidth][y/pixelWidth + 1] = 1;
          this.pixels[x/pixelWidth][y/pixelWidth - 1] = 1;

          if (this.pixels[x/pixelWidth + 1][y/pixelWidth + 1] < 1) {
            this.pixels[x/pixelWidth + 1][y/pixelWidth + 1] = Math.random();
          }
          if (this.pixels[x/pixelWidth + 1][y/pixelWidth - 1] < 1) {
            this.pixels[x/pixelWidth + 1][y/pixelWidth - 1] = Math.random();
          }
          if (this.pixels[x/pixelWidth - 1][y/pixelWidth + 1] < 1) {
            this.pixels[x/pixelWidth - 1][y/pixelWidth + 1] = Math.random();
          }
          if (this.pixels[x/pixelWidth - 1][y/pixelWidth - 1] < 1) {
            this.pixels[x/pixelWidth - 1][y/pixelWidth - 1] = Math.random();
          }
        }
        catch(ArrayIndexOutOfBoundsException e) {}
      }
    }

    @Override
    public void mousePressed(MouseEvent e) {
      pressed = true;
      draw(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      draw(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      draw(e.getX(), e.getY());
      pressed = false;
    }
  }

  class KeyInputs extends KeyAdapter {
    double[][] pixels;

    KeyInputs(double[][] pixels) {
      this.pixels = pixels;
    }

    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_R) {
        for (int i = 0; i < pixels.length; i++) {
          for (int j = 0; j < pixels.length; j++) {
            pixels[i][j] = 0;
          }
        }
      }
      else if (e.getKeyCode() == KeyEvent.VK_F) {
        window.setLocation(mouseX, mouseY);
      }
      else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
      }
    }
  }

}
