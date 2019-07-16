cd src
javac -d ../bin Test.java
echo "args:"
read args
java -cp ../bin Test $args