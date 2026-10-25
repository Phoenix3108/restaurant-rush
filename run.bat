@echo off
REM Compile the game into out\ and start it.
REM -sourcepath lets javac find every class from Main (works with spaces in folder names).
if exist out rmdir /s /q out
mkdir out
javac -d out -sourcepath src src\restaurant\Main.java
if errorlevel 1 exit /b 1
java -cp out restaurant.Main