#include "main.hpp"

int main() {

  DataSet<NumberData> trainingData = loadMNISTTraining(true);
  DataSet<NumberData> testData = loadMNISTTest(true);

  //Network network1({ 784, 30, 10 });
  //stochasticDescentCPU(&network1, 30, 10, 0.5, &trainingData, &testData);

  Network network = loadNetwork("net3");
  std::cout << network.evaluate(testData) << " / " << testData.getSize() << "\n";

  trainingData = NULL;
  testData = NULL;

  Display display(IMAGE_RESOLUTION * DRAWING_SCALE, IMAGE_RESOLUTION, PIXEL_WIDTH, PIXEL_WIDTH * DRAWING_SCALE);

  while (display.isRunning())
    std::cout << "\rNumber classification --> " << network.classify(display.update(true));

  return 0;
}