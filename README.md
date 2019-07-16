# neural-network

A simple neural network for classifying the [MNIST](http://yann.lecun.com/exdb/mnist/) database of hand-written digits implemented in Java.


## Usage

Run the shell script `test.sh` to compile and run `Test.java` in the src forlder. A prompt will appear so you can input the appropriate arguments to pass to `Test.main()`. To load a network from __data/stored_networks__ pass in the string __"load " + path__ where path is the name of the network. 

```shell
./test.sh
args:
load net1
```

To create and train a network using stochastic gradient descent and backpropegation, pass in the string __"train " + epochs + " " + miniBatchSize + " " + learningRate + " " + layers__ for the appropriate hyperparameters. The following script creates a network with layers [784 100 10] and trains it for 15 epochs with mini batches of size 10 and a learning rate of 2.5

```shell
./test.sh 
args:
train 15 10 2.5 784 100 10
```
Once the network has finished training/loading a window will appear. You can drag it around by right clicking on it and moving your mouse, draw by left clicking and moving your mouse, and reset it by pressing 'R'. The classification of the number drawn will output to the terminal automatically and will update in real-time.

Alternatively, you can write code directly into the main method of `Test.java` in the src folder. To compile and run this class directly run the following commands in a terminal from the directory __neural-network/src__

```shell
javac -d ../bin Test.java
java -cp ../bin Test
```