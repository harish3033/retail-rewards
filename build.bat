@echo off
set currentDirectory=%~dp0
cd /d %currentDirectory%
mvn clean compile package