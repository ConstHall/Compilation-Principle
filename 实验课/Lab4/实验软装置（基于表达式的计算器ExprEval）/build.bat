@echo off
cd src
javac -d ..\bin -classpath ..\bin parser\*.java
javac -d ..\bin -classpath ..\bin scanner\*.java
cd ..
pause
@echo on