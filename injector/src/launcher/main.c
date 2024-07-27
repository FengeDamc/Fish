//
// Created by admin on 2024/6/10.
//
#include <windows.h>
#include <stdio.h>
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
    ShowWindow(GetConsoleWindow(), SW_HIDE);
    char command[1024];
    sprintf(command, "java -jar FunGhostClient.jar");

    // 使用system函数执行命令
    return system(command);
}


