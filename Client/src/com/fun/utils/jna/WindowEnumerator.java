package com.fun.utils.jna;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

import com.sun.jna.ptr.IntByReference;

import java.util.ArrayList;

public class WindowEnumerator {

    public static ArrayList<String> getWindows(int processId) {
        // 假设你已经知道要遍历的进程ID

        ArrayList<String> strs=new ArrayList<>();
        WinUser.WNDENUMPROC enumWindowsProc = new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(HWND hwnd, Pointer pointer) {
                IntByReference ref = new IntByReference();
                if (User32.INSTANCE.GetWindowThreadProcessId(hwnd, ref)!=0 && ref.getValue() == processId) {
                    // 获取窗口标题
                    int length = User32.INSTANCE.GetWindowTextLength(hwnd) + 1;
                    char[] buffer = new char[length];
                    User32.INSTANCE.GetWindowText(hwnd, buffer, length);
                    String windowTitle = new String(buffer);
                    strs.add(windowTitle);//System.out.println("窗口标题: " + windowTitle);
                }
                return true; // 继续枚举
            }
        };

        User32.INSTANCE.EnumWindows(enumWindowsProc, new Pointer(0));
        return strs;
    }
}