package fun.inject;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinNT;

public class InjectorUtils {
    public static final Kernel32 kernel32 = Kernel32.INSTANCE;
    public static final User32 user32 = User32.INSTANCE;
    public static  void injector(int pid,String dllPath){
        WinNT.HANDLE hdl = kernel32.OpenProcess(0x1F1FFB, false, pid);
        loadLibrary(hdl,dllPath);

    }
    public static void loadLibrary(WinNT.HANDLE hdl, String path) {
        Memory pathMemory = new Memory((long) path.length() + 1L);
        pathMemory.setString(0L, path);
        BaseTSD.SIZE_T pathSize = new BaseTSD.SIZE_T(pathMemory.size());
        Pointer pathRemote = kernel32.VirtualAllocEx(hdl, null, pathSize, 12288, 4);
        if (pathRemote == Pointer.NULL)
            throw new IllegalStateException("failed to allocate DLL path.");
        if (!kernel32.WriteProcessMemory(hdl, pathRemote, pathMemory, pathSize.intValue(), null))
            throw new IllegalStateException("could not write DLL path to process.");
        Function loadLibrary = Function.getFunction("kernel32", "LoadLibraryA");
        WinNT.HANDLE hThread = kernel32.CreateRemoteThread(hdl, null, 0, loadLibrary, pathRemote, 0, null);
        if (kernel32.WaitForSingleObject(hThread, -1) != 0)
            throw new IllegalStateException("WaitForSingleObject failed.");
        kernel32.VirtualFreeEx(hdl, pathMemory, new BaseTSD.SIZE_T(0L), 32768);
        System.out.println("dll loaded");
    }

}
