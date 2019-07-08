# neural-network

A simple neural network for classifying the [MNIST](http://yann.lecun.com/exdb/mnist/) database of hand-written digits implemented in Java.


## Usage

Run the following command in a terminal in the root directory of the project to load a pre-trained network named 'network'. The network must be stored in the directory data/stored_networks.

```java
start test "load network"
//example: start test "load net1"
```

To create and train a network using stochastic gradient descent and backpropegation, run the following command with the appropriate hyperparameters

```java
start test "train epochs miniBatchSize learningRate layers"
//example: start test "train 15 10 2.5 784 100 10" creates a network with layers [784 100 10] and trains it for 15 epochs with mini batches of size 10 and a learning rate of 2.5
```

Alternatively, you can write code directly into the main method of the class 'Test' in the src folder. To compile and run this class directly run the following commands in a terminal from the directory neural-network/src

```java
javac -d ../bin Test.java //compile
java -pc ../bin Test //test
```

The class can also be compiled and run by running the batch file 'test.bat' in the root directory of the program by typing 'start test' into the terminal.

Once the network has finished training/loading a window will appear. You can drag it around by right clicking on it and moving your mouse, draw by left clicking and moving your mouse, and reset it by pressing 'R'. The classification of the number drawn will output to the terminal automatically and will update in real-time.
