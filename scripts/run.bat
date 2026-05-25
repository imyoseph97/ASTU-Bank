@echo off
setlocal
cd /d "%~dp0.."
if not exist "out" mkdir out
javac --release 17 -encoding UTF-8 -d out src\main\java\banking\Main.java src\main\java\banking\model\*.java src\main\java\banking\security\*.java src\main\java\banking\service\*.java src\main\java\banking\store\*.java src\main\java\banking\ui\*.java
if errorlevel 1 exit /b 1
java -cp out banking.Main
