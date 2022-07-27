@echo off
cd src
javac -d ..\bin -classpath .;..\lib\complexity.jar;..\lib\exceptions.jar;..\lib\java-cup-11b.jar;..\lib\jflex-full-1.8.2.jar *.java
cd ..
pause
@echo on