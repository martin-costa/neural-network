#include "panel.hpp"

#include <iostream>

Panel::Panel(int res, int pixelSize, int vertOffset, int horOffset) {
  this->res = res;
  this->pixelSize = pixelSize;

  this->vertOffset = vertOffset;
  this->horOffset = horOffset;

  this->image = sf::VertexArray(sf::Quads, res * res * 4);

  setUpGrid();
}

Panel::Panel(int res, int pixelSize) {
  *this = Panel(res, pixelSize, 0, 0);
}

Panel::Panel() {};

// sets the color of the pixel (i,j)
void Panel::setPixel(int i, int j, sf::Color color) {
  if (i < 0 || i > res || j < 0 || j > res) return;
  for (int k = 0; k < 4; k++) image[I(i, j) + k].color = color;
}

// reset the colours on the panel
void Panel::reset() {
  for (int i = 0; i < res; i++) {
    for (int j = 0; j < res; j++) {
      setPixel(i, j, sf::Color::White);
    }
  }
}

// set up image by initialising all the pixels
void Panel::setUpGrid() {
  for (int i = 0; i < res; i++) {
    for (int j = 0; j < res; j++) {
      initPixel(i, j, sf::Color::White);
    }
  }
}

// initialise the pixel (i,j)
void Panel::initPixel(int i, int j, sf::Color color) {
  image[I(i, j)]     = sf::Vertex(sf::Vector2f(i * pixelSize + horOffset, j * pixelSize + vertOffset), color);
  image[I(i, j) + 1] = sf::Vertex(sf::Vector2f((i + 1) * pixelSize + horOffset, j * pixelSize + vertOffset), color);
  image[I(i, j) + 2] = sf::Vertex(sf::Vector2f((i + 1) * pixelSize + horOffset, (j + 1) * pixelSize + vertOffset), color);
  image[I(i, j) + 3] = sf::Vertex(sf::Vector2f(i * pixelSize + horOffset, (j + 1) * pixelSize + vertOffset), color);
}

Vector Panel::getImageVector() {
  Vector v(res * res);
  for (int i = 0; i < res; i++) {
    for (int j = 0; j < res; j++) {
      v[i + j * res] = 1 - image[I(i, j)].color.r / 255.0;
    }
  }
  return v;
}

//// __ Methods for drawing panel __ ////

// call Panel's constructors
DrawingPanel::DrawingPanel(int res, int pixelSize, int vertOffset, int horOffset)
  : Panel(res, pixelSize, vertOffset, horOffset) {}

DrawingPanel::DrawingPanel(int res, int pixelSize)
  : Panel(res, pixelSize) {}

DrawingPanel::DrawingPanel() {}

// draw at the position pos
void DrawingPanel::draw(sf::Vector2i pos, bool clicked) {

  pos = sf::Vector2i(pos.x / pixelSize, pos.y / pixelSize);

  if (clicked) this->mousePos = pos;

  double rad = 18 / (double)pixelSize;
  for (int i = -rad; i <= rad; i++) {
    for (int j = -rad; j <= rad; j++) {
      if (sqrt(i * i + j * j) < rad && 0 <= pos.x + i && pos.x + i < res && 0 <= pos.y + j && pos.y + j < res) {
        setPixel(pos.x + i, pos.y + j, sf::Color::Black);
      }
    }
  }

  int x = (pos.x - mousePos.x);
  int y = (pos.y - mousePos.y);

  int dx = (x >= 0) ? 1 : -1;
  int dy = (y >= 0) ? 1 : -1;

  for (int i = -rad; i <= abs(x) + rad; i++) {
    for (int j = -rad; j <= abs(y) + rad; j++) {

      sf::Vector2i p = sf::Vector2i(mousePos.x + i * dx, mousePos.y + j * dy);

      if (distanceFromLine(pos.x, pos.y, mousePos.x, mousePos.y, p.x, p.y) < rad) {
        if ( ( (p.x - pos.x) * (mousePos.x - pos.x) + (p.y - pos.y) * (mousePos.y - pos.y) > 0 && (p.x - mousePos.x) * (pos.x - mousePos.x) + (p.y - mousePos.y) * (pos.y - mousePos.y) > 0 )
          || sqrt(pow(p.x - pos.x, 2) * pow(p.y - pos.y, 2)) <= rad || sqrt(pow(p.x - mousePos.x, 2) * pow(p.y - mousePos.y, 2)) <= rad)
          setPixel(p.x, p.y, sf::Color::Black);
      }
    }
  }

  this->mousePos = pos;
}

Vector DrawingPanel::getPreprocessedImage() {
  return preprocess(&getImageVector(), res, 20, 28);
}

//// __ Methods for viewing panel __ ////

// call Panel's constructors
ViewingPanel::ViewingPanel(int res, int pixelSize, int vertOffset, int horOffset)
  : Panel(res, pixelSize, vertOffset, horOffset) {}

ViewingPanel::ViewingPanel(int res, int pixelSize)
  : Panel(res, pixelSize) {}

ViewingPanel::ViewingPanel() {}

void ViewingPanel::setImage(sf::VertexArray image) {
  this->image = image;
}

void ViewingPanel::setImage(sf::VertexArray* image) {
  this->image = *image;
}

void ViewingPanel::setImage(Vector image) {
  for (int i = 0; i < res; i++) {
    for (int j = 0; j < res; j++) {
      int c = (image[i + j * res])*255;
      setPixel(i, j, sf::Color(c, c, c));
    }
  }
}

//// __ Preprocessing methods __ ////

Vector preprocess(Vector* image, int res, int norm, int newRes) {

  Vector processedImage = *image;

  processedImage = normalize(&processedImage, res, newRes, norm);

  processedImage = changeResolution(&processedImage, res, newRes);

  return centre(&processedImage, newRes);
}

Vector normalize(Vector* image, int res, int newRes, int norm) {

  int lowerX, upperX, lowerY, upperY;
  getBoundingBox(image, &lowerX, &upperX, &lowerY, &upperY, res);

  int dx = upperX - lowerX;
  int dy = upperY - lowerY;

  if (dx < 1 || dy < 1) return *image;

  double z = std::max((double)dx / (double)(norm * res / newRes), (double)dy / (double)(norm * res / newRes));
  double dx0 = ((double)dx / z);
  double dy0 = ((double)dy / z);

  Vector buffer(res * res);

  for (int i = 0; i < (int)dx0; i++) {
    for (int j = 0; j < (int)dy0; j++) {
      if (i < res && j < res) {
        buffer[i + j * res] = (*image)[lowerX + (int)round(i * z) + ((int)round(lowerY + j * z)) * res];
      }
    }
  }

  return buffer;
}

Vector changeResolution(Vector* image, int res, int newRes) {

  Vector buffer(newRes * newRes);
  double ratio = (double)res / (double)newRes;

  for (int i = 0; i < newRes; i++) {
    for (int j = 0; j < newRes; j++) {

      double totalColor = 0;
      for (int i0 = 0; i0 < ratio; i0++) {
        for (int j0 = 0; j0 < ratio; j0++) {
          totalColor += (*image)[(int)(i * ratio + i0) + (int)((j * ratio + j0) * res)];
        }
      }
      buffer[i + j * newRes] = totalColor / (ratio * ratio);
    }
  }
  return buffer;
}

Vector centre(Vector* image, int res) {

  double x = 0, y = 0, m = 0;
  for (int i = 0; i < res; i++) {
    for (int j = 0; j < res; j++) {
      m += (*image)[i + j * res];
      x += i * (*image)[i + j * res];
      y += j * (*image)[i + j * res];
    }
  }

  if (m <= 0) return *image;

  x /= m, y /= m;

  int dx = (int)(x - (double)res / 2); int dy = (int)(y - (double)res / 2);

  Vector buffer(res * res);

  for (int i = 0; i < res; i++) {
    for (int j = 0; j < res; j++) {
      if (i < res && j < res && i + dx < res && j + dy < res && 0 <= i + dx && 0 <= j + dy) {
        buffer[i + j * res] = (*image)[i + dx + (j + dy) * res];
      }
      else buffer[i + j * res] = 0;
    }
  }
  return buffer;
}

// preprocessing aux methods

void getBoundingBox(Vector* image, int* lowerX, int* upperX, int* lowerY, int* upperY, int res) {

  *lowerX = res; *lowerY = res; *upperX = -1; *upperY = -1;

  for (int i = 0; i < res; i++) {
    for (int j = 0; j < res; j++) {
      if ((*image)[(res - i - 1) + (res - j - 1) * res] > 0) *lowerX = res - i - 1;
      if ((*image)[(res - j - 1) + (res - i - 1) * res] > 0) *lowerY = res - i - 1;
      if ((*image)[i + j * res] > 0) *upperX = i;
      if ((*image)[j + i * res] > 0) *upperY = i;
    }
  }
}

// aux methods
double distanceFromLine(int x1, int y1, int x2, int y2, int x0, int y0) {
  return abs( (y2-y1)*x0 - (x2-x1)*y0 + x2*y1 - y2*x1 ) / sqrt( (y2-y1)*(y2-y1) + (x2-x1)*(x2-x1) );
}