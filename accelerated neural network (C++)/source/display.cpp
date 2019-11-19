#include "display.hpp"

Display::Display(int drawingRes, int viewingRes, int drawingPixelSize, int viewingPixelSize) {

  this->drawingRes = drawingRes;
  this->viewingRes = viewingRes;
  this->drawingPixelSize = drawingPixelSize;
  this->viewingPixelSize = viewingPixelSize;

  // init the windows
  this->viewingWindow.create(sf::VideoMode(viewingRes * viewingPixelSize, viewingRes * viewingPixelSize), "Viewing Panel", sf::Style::Titlebar);
  this->drawingWindow.create(sf::VideoMode(drawingRes * drawingPixelSize, drawingRes * drawingPixelSize), "Drawing Panel", sf::Style::Titlebar);

  // move them to correct positions
  this->viewingWindow.setPosition(sf::Vector2i((sf::VideoMode::getDesktopMode().width) / 2 - viewingRes * viewingPixelSize - 50,
    (sf::VideoMode::getDesktopMode().height - viewingRes * viewingPixelSize) / 2));

  this->drawingWindow.setPosition(sf::Vector2i((sf::VideoMode::getDesktopMode().width) / 2 + 50,
    (sf::VideoMode::getDesktopMode().height - drawingRes * drawingPixelSize) / 2));

  // init the panels
  this->drawingPanel = DrawingPanel(drawingRes, drawingPixelSize);
  this->viewingPanel = ViewingPanel(viewingRes, viewingPixelSize);
}


// get inputs to window
void Display::inputs() {
  static MouseDetector leftMouse(sf::Mouse::Left);
  bool clicked = leftMouse.clicked();
  if (leftMouse.down()) draw(clicked);

  static KeyDetector keyR(sf::Keyboard::R);
  if (keyR.typed()) reset();
}

// draw to drawingPanel
void Display::draw(bool clicked) {
  drawingPanel.draw(sf::Mouse::getPosition(drawingWindow), clicked);
}

void Display::reset() {
  drawingPanel.reset();
  viewingPanel.reset();
}

Vector Display::update(bool process) {

  sf::Event e;
  while (drawingWindow.pollEvent(e) || viewingWindow.pollEvent(e)) {
    if (sf::Event::Closed || sf::Keyboard::isKeyPressed(sf::Keyboard::Escape)) close();
  }

  inputs();

  drawingWindow.draw(drawingPanel.getImageArray());

  drawingWindow.display();

  if (!process) return viewingPanel.getImageVector();

  Vector image = drawingPanel.getPreprocessedImage();

  viewingPanel.setImage(image);

  viewingWindow.draw(viewingPanel.getImageArray());

  viewingWindow.display();

  return image;
}

bool Display::isRunning() {
  return running;
}

void Display::close() {
  drawingWindow.close();
  viewingWindow.close();
  running = false;
}