#!/bin/bash
cd ./src/ && javac --class-path ../bin/ parser/*.java scanner/*.java -d ../bin/ && cd ..