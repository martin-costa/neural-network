#pragma once

#include <SFML/Graphics.hpp>

#include "panel.hpp"
#include "framerate.hpp"
#include "inputdetector.hpp"

class Display {
private:
  bool running = true;

  int drawingRes;
  int viewingRes;

  int drawingPixelSize;
  int viewingPixelSize;

public:
  sf::RenderWindow drawingWindow;
  sf::RenderWindow viewingWindow;

  DrawingPanel drawingPanel;
  ViewingPanel viewingPanel;

  Display(int drawingRes, int viewingRes, int drawingPixelSize, int viewingPixelSize);

  void inputs();

  void draw(bool clicked);

  void reset();

  Vector update(bool process);

  bool isRunning();

  void close();

  sf::RenderWindow* getDrawingWindow() { return &drawingWindow; }

  sf::RenderWindow* getViewingWindow() { return &viewingWindow; }

  DrawingPanel* getDrawingPanel() { return &drawingPanel; }
  
  ViewingPanel* getViewingPanel() { return &viewingPanel; }
};