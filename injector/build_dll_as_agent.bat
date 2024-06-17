g++ -c src/agent/dllmain.cpp -o dllmain.o
g++  lib/libMinHook.x64.lib dllmain.o
del dllmain.o
pause