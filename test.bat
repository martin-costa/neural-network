cd src
javac -d ../bin Test.java
cls

java -cp ../bin Test %~1%
::exit 0