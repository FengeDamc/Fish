#include <windows.h>
#include <stdio.h>

#pragma pack(push, 1) // 确保结构体紧凑，没有字节填充

// 定义跳转指令结构体
typedef struct {
    BYTE opcode;      // 操作码，对于jmp是0xE9
    DWORD relativeOffset; // 相对偏移量
} JUMP_INSTRUCTION;

#pragma pack(pop)

// 假设这是原始函数的类型
typedef int (__stdcall *OriginalFuncType)();

// 拦截函数


// Hook 原始函数
void HookFunction(OriginalFuncType *originalFuncPtr, OriginalFuncType newFunc)
{
    DWORD oldProtect;
    VirtualProtect((LPVOID)originalFuncPtr, sizeof(JUMP_INSTRUCTION), PAGE_EXECUTE_READWRITE, &oldProtect);

    // 构造跳转到新函数的指令
    JUMP_INSTRUCTION jump = { 0xE9 };
    jump.relativeOffset = (DWORD)((char*)newFunc - (char*)(originalFuncPtr + 1)) - sizeof(JUMP_INSTRUCTION);

    // 写入跳转指令
    memcpy(originalFuncPtr, &jump, sizeof(jump));

    VirtualProtect((LPVOID)originalFuncPtr, sizeof(JUMP_INSTRUCTION), oldProtect, &oldProtect);
}

// Unhook 原始函数
void UnhookFunction(OriginalFuncType *originalFuncPtr, OriginalFuncType originalFunc)
{
    DWORD oldProtect;
    VirtualProtect((LPVOID)originalFuncPtr, sizeof(JUMP_INSTRUCTION), PAGE_EXECUTE_READWRITE, &oldProtect);

    // 恢复原始函数的指令
    originalFunc(); // 调用原始函数以获取其第一条指令
    *originalFuncPtr = originalFunc;

    VirtualProtect((LPVOID)originalFuncPtr, sizeof(JUMP_INSTRUCTION), oldProtect, &oldProtect);
}
int __stdcall InterceptedFunction()
{
    printf("InterceptedFunction is called before the original function.\n");

    // 这里可以调用原始函数，如果需要的话
    // 例如：return originalFunction();

    return 0; // 这里返回0作为示例
}
int main()
{
    // 获取原始函数的地址
    OriginalFuncType originalFunc = (OriginalFuncType)GetProcAddress(GetModuleHandle("YourDllName.dll"), "FunctionNameToHook");

    if (originalFunc)
    {
        // Hook 原始函数
        HookFunction(&originalFunc, InterceptedFunction);

        // 调用原始函数，现在会被拦截
        originalFunc();

        // Unhook 原始函数
        UnhookFunction(&originalFunc, originalFunc);

        // 再次调用原始函数，现在不再被拦截
        originalFunc();
    }
    else
    {
        fprintf(stderr, "Failed to get the address of the function to hook.\n");
        return EXIT_FAILURE;
    }

    return EXIT_SUCCESS;
}