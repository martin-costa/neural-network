#include <SFML/Graphics.hpp>
#include <algorithm>

#include "vector.hpp"

#define I(i,j) 4*(i*res + j)


//// __ base class Panel used to draw an image to a window __ ////
 
class Panel {
protected:
  int res;
  int pixelSize;

  int vertOffset;
  int horOffset;

  sf::VertexArray image;

  void setUpGrid();

  void initPixel(int i, int j, sf::Color color);

  void setPixel(int i, int j, sf::Color color);

public:

  //constructors
  Panel(int res, int pixelSize, int vertOffset, int horOffset);

  Panel(int res, int pixelSize);

  Panel();

  //reset
  void reset();

  //get the image
  Vector getImageVector();

  sf::VertexArray getImageArray() { return image; }

  sf::VertexArray* getImageArrayPointer() { return &image; }
};

//// __ DrawingPanel extends functionality of Panel to allow drawing __ ////
 
class DrawingPanel : public Panel {
private:

  sf::Vector2i mousePos;

public:

  // constructors
  DrawingPanel(int res, int pixelSize, int vertOffset, int horOffset);

  DrawingPanel(int res, int pixelSize);

  DrawingPanel();

  // draw
  void draw(sf::Vector2i pos, bool clicked);

  Vector getPreprocessedImage();
};

//// __ ViewingPanel extends functionality of Panel to allow passing in images __ ////
 
class ViewingPanel : public Panel {
public:

  ViewingPanel(int res, int pixelSize, int vertOffset, int horOffset);

  ViewingPanel(int res, int pixelSize);

  ViewingPanel();

  // set the image
  void setImage(sf::VertexArray image);

  void setImage(sf::VertexArray* image);

  void setImage(Vector image);
};

// preprocessing methods
Vector preprocess(Vector* image, int res, int norm, int newRes);

Vector normalize(Vector* image, int res, int newRes, int norm);

Vector changeResolution(Vector* image, int res, int newRes);

Vector centre(Vector* image, int res);

// preprocessing aux methods
void getBoundingBox(Vector* image, int* lowerX, int* upperX, int* lowerY, int* upperY, int res);

// aux methods
double distanceFromLine(int x1, int y1, int x2, int y2, int x0, int y0);

