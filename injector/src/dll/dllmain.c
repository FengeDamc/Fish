//
// Created by admin on 2024/4/7.
//
#include <windows.h>
#include "../java/jni.h"
#include "utils.h"
#include "../java/jvmti.h"
#include <stdio.h>


#define true 1
#define bool int
#define false 0
#define NEW_STR_SIZE 1024

extern BYTE OldCode[12] = { 0x00 };
extern BYTE HookCode[12] = { 0x48, 0xB8, 0x90, 0x90, 0x90, 0x90, 0x90, 0x90, 0x90, 0x90, 0xFF, 0xE0 };

void HookFunction64(char* lpModule, LPCSTR lpFuncName, LPVOID lpFunction)
{
    DWORD_PTR FuncAddress = (UINT64)GetProcAddress(GetModuleHandle(lpModule), lpFuncName);
    DWORD OldProtect = 0;
    //MessageBoxA(NULL,"1","Fish",0);

    if (VirtualProtect((LPVOID)FuncAddress, 12, PAGE_EXECUTE_READWRITE, &OldProtect))
    {
        //MessageBoxA(NULL,"2","Fish",0);

        memcpy(OldCode, (LPVOID)FuncAddress, 12);                   // 拷贝原始机器码指令
        *(PINT64)(HookCode + 2) = (UINT64)lpFunction;               // 填充90为指定跳转地址
    }
    memcpy((LPVOID)FuncAddress, &HookCode, sizeof(HookCode));
    //MessageBoxA(NULL,"3","Fish",0);
    // 拷贝Hook机器指令
    VirtualProtect((LPVOID)FuncAddress, 12, OldProtect, &OldProtect);
}
void UnHookFunction64(char* lpModule, LPCSTR lpFuncName)
{
    DWORD OldProtect = 0;
    UINT64 FuncAddress = (UINT64)GetProcAddress(GetModuleHandleA(lpModule), lpFuncName);
    if (VirtualProtect((LPVOID)FuncAddress, 12, PAGE_EXECUTE_READWRITE, &OldProtect))
    {
        memcpy((LPVOID)FuncAddress, OldCode, sizeof(OldCode));
    }
    VirtualProtect((LPVOID)FuncAddress, 12, OldProtect, &OldProtect);
}

/*int WINAPI MyMessageBoxW(HWND hWnd, LPCTSTR lpText, LPCTSTR lpCaption, UINT uType)
{
    UnHookFunction64(L"user32.dll", "MessageBoxW");
    int ret = MessageBoxW(0, L"hello lyshark", lpCaption, uType);
    HookFunction64(L"user32.dll", "MessageBoxW", (PROC)MyMessageBoxW);
    return ret;
}
bool APIENTRY DllMain(HANDLE handle, DWORD dword, LPVOID lpvoid)
{
    switch (dword)
    {
        case DLL_PROCESS_ATTACH:
            HookFunction64(L"user32.dll", "MessageBoxW", (PROC)MyMessageBoxW);
            break;
        case DLL_PROCESS_DETACH:
        case DLL_THREAD_DETACH:
            break;
    }
    return true;
}*/







typedef struct {
    JavaVM *vm;
    JNIEnv *jniEnv;
    jvmtiEnv *jvmtiEnv;
} JAVA;
static JAVA *Java;


jclass findThreadClass(JNIEnv *jniEnv, const char *name, jobject thread) {
    jclass Thread = (*jniEnv)->GetObjectClass(jniEnv, thread);
    jclass URLClassLoader = (*jniEnv)->FindClass(jniEnv, "java/net/URLClassLoader");
    jclass ClassLoader = (*jniEnv)->FindClass(jniEnv, "java/lang/ClassLoader");

    jmethodID getContextClassLoader = (*jniEnv)->GetMethodID(jniEnv, Thread, "getContextClassLoader",
                                                                       "()Ljava/lang/ClassLoader;");
    jmethodID findClass = (*jniEnv)->GetMethodID(jniEnv, URLClassLoader, "findClass",
                                                           "(Ljava/lang/String;)Ljava/lang/Class;");
    jmethodID loadClass = (*jniEnv)->GetMethodID(jniEnv, URLClassLoader, "loadClass",
                                                           "(Ljava/lang/String;)Ljava/lang/Class;");
    jmethodID getSystemClassLoader = (*jniEnv)->GetStaticMethodID(jniEnv, ClassLoader, "getSystemClassLoader",
                                                                            "()Ljava/lang/ClassLoader;");

    jobject classloader;
    if (true) {
        classloader = (*jniEnv)->CallObjectMethod(jniEnv, thread, getContextClassLoader);
        return (jclass) (*jniEnv)->CallObjectMethod(jniEnv, classloader, loadClass,
                                                              (*jniEnv)->NewStringUTF(jniEnv, name));
    } else {
        classloader = (*jniEnv)->CallStaticObjectMethod(jniEnv, ClassLoader, getSystemClassLoader);
        return (jclass) (*jniEnv)->CallObjectMethod(jniEnv, classloader, loadClass,
                                                              (*jniEnv)->NewStringUTF(jniEnv, name));
    }
}
jclass JNICALL findClass0(JNIEnv *jniEnv, const char *name, jobject classloader) {

    jmethodID loadClass = (*jniEnv)->GetMethodID(jniEnv, (*jniEnv)->GetObjectClass(jniEnv,classloader), "loadClass",
                                                 "(Ljava/lang/String;)Ljava/lang/Class;");

    return (jclass) (*jniEnv)->CallObjectMethod(jniEnv, classloader, loadClass,
                                                (*jniEnv)->NewStringUTF(jniEnv, name));
}
extern JNIEXPORT jclass JNICALL findClass(JNIEnv *jniEnv, const char *name) {
    jclass ClassLoader = (*jniEnv)->FindClass(jniEnv, "java/lang/ClassLoader");

    //jmethodID findClass = (*jniEnv)->GetMethodID(jniEnv, URLClassLoader, "findClass", "(Ljava/lang/String;)Ljava/lang/Class;");
    jmethodID getSystemClassLoader = (*jniEnv)->GetStaticMethodID(jniEnv, ClassLoader, "getSystemClassLoader",
                                                                  "()Ljava/lang/ClassLoader;");

    jobject classloader;
    classloader = (*jniEnv)->CallStaticObjectMethod(jniEnv, ClassLoader, getSystemClassLoader);
    return findClass0(jniEnv,name,classloader);
}





extern JNIEXPORT void JNICALL classFileLoadHook(jvmtiEnv * jvmti_env, JNIEnv * env,
                               jclass
                               class_being_redefined,
                               jobject loader,
                               const char *name, jobject
                               protection_domain,
                               jint class_data_len,
                               const unsigned char *class_data,
                               jint
                               *new_class_data_len,
                               unsigned char **new_class_data
)
{
    //MessageBoxA(NULL,"transhook","FishCient",0);

//MessageBoxA(NULL, name, "FunGhostClient", NULL);
    //MessageBoxA(NULL, "ReTrans", "FunGhostClient", 0);
    jclass nativeUtils = //(*env)->FindClass(env,"com/fun/inject/NativeUtils");
    findClass(env, "com.fun.inject.NativeUtils");

    if(!nativeUtils){
        MessageBoxA(NULL,"NativeUtils was null","Fish",0);
        return;
    }
    jmethodID transform = (*env)->GetStaticMethodID(env,nativeUtils, "transform",
                                                 "(Ljava/lang/ClassLoader;Ljava/lang/String;Ljava/lang/Class;Ljava/security/ProtectionDomain;[B)[B");//todo
//MessageBoxA(NULL,name,"FunGhostClient",NULL);

    jbyteArray oldBytes = (*env)->NewByteArray(env,class_data_len);
    (*env)->
            SetByteArrayRegion(env,oldBytes,
                                0, class_data_len, (jbyte *)class_data);
//MessageBoxA(NULL,"$","FunGhostClient",NULL);

    jbyteArray newBytes = (jbyteArray) ((*env)->CallStaticObjectMethod(env,nativeUtils, transform,
                                                              loader, (*env)->NewStringUTF(env,name), class_being_redefined,
                                                              protection_domain, oldBytes));
//MessageBoxA(NULL,"%","FunGhostClient",NULL);

    jsize size = (*env)->GetArrayLength(env,newBytes);
    jbyte *classByteArray = (*env)->GetByteArrayElements(env,newBytes, NULL);
    *
            new_class_data = (unsigned char *) classByteArray;
    *
            new_class_data_len = size;

//    env->ReleaseByteArrayElements(newBytes,classByteArray,0);


}

extern JNIEXPORT char* JNICALL cut_str(char *dest, const char *src, int size) {
    for (int i = 0; i < size; i++) {
        dest[i] = src[i];
    }
    dest[size] = '\0';
    return dest;
}


/*
 * Class:     fun_inject_NativeUtils
 * Method:    getAllLoadedClasses
 * Signature: ()Ljava/util/ArrayList;
 */
extern JNIEXPORT jobject JNICALL Java_fun_inject_NativeUtils_getAllLoadedClasses
        (JNIEnv *env, jclass _) {
    JavaVM *vm;
    jvmtiEnv *jvmti;
    (*env)->GetJavaVM(env,&vm);
    (*vm)->GetEnv(vm,(void **) &jvmti, JVMTI_VERSION);
    jint classcount = 0;
    jclass *classes = NULL;
    (*jvmti)->GetLoadedClasses((jvmtiEnv *) jvmti, &classcount, &classes);
    jclass ArrayList = (*env)->FindClass(env, "java/util/ArrayList");
    jmethodID add = (*env)->GetMethodID(env, ArrayList, "add", "(Ljava/lang/Object;)Z");
    jobject list = (*env)->NewObject(env, ArrayList,
                                               (*env)->GetMethodID(env, ArrayList, "<init>", "()V"));
    for (int i = 0; i < classcount; i++)
        (*env)->CallBooleanMethod(env, list, add, classes[i]);
    //free(classes);
    return list;

}

/*
 * Class:     fun_inject_NativeUtils
 * Method:    redefineClass
 * Signature: ([Ljava/lang/instrument/ClassDefinition;)V
 */
extern JNIEXPORT void JNICALL Java_fun_inject_NativeUtils_redefineClass
        (JNIEnv *env, jclass _, jclass clazz, jbyteArray bytes) {
    JAVA java={NULL,NULL,NULL};

    (*env)->GetJavaVM(env,&java.vm);
    (*java.vm)->GetEnv(java.vm,(void **) &(java.jvmtiEnv), JVMTI_VERSION);
    ///////
    jvmtiCapabilities capabilities;
    memset(&capabilities, 0, sizeof(jvmtiCapabilities));
    if(!java.jvmtiEnv)MessageBoxA(NULL,"L","Fish",0);


    //(Java->jvmtiEnv)->GetCapabilities(capabilities);
    capabilities.can_get_bytecodes = 1;
    capabilities.can_redefine_classes = 1;
    capabilities.can_redefine_any_class = 1;
    capabilities.can_generate_all_class_hook_events = 1;
    capabilities.can_retransform_classes = 1;
    capabilities.can_retransform_any_class = 1;

    (*java.jvmtiEnv)->AddCapabilities(java.jvmtiEnv,&capabilities);
    ///////
    jint size = (*env)->GetArrayLength(env,bytes);
    jbyte *classByteArray = (*env)->GetByteArrayElements(env,bytes, NULL);

    jvmtiClassDefinition definition[] = {
            {clazz, size, (unsigned char *) classByteArray}
    };
    (*java.jvmtiEnv)->RedefineClasses(java.jvmtiEnv,1, definition);
    (*env)->ReleaseByteArrayElements(env,bytes, classByteArray, 0);
}

extern JNIEXPORT void *allocate(jlong size) {
    void *resultBuffer = malloc(size);
    return resultBuffer;
}



/*
 * Class:     fun_inject_NativeUtils
 * Method:    retransformClass0
 * Signature: (Ljava/lang/Class;)V
 */
extern JNIEXPORT void JNICALL retransformClass0
        (JNIEnv *env, jclass _, jclass target) {

    if (!Java->jvmtiEnv)MessageBoxA(NULL, "S.B.Bob", "FunGhostClient", 0);
    jclass *clzzs = (jclass *) allocate(sizeof(jclass) * 1);
    clzzs[0] = target;
    char *c = (char *) allocate(4);
    //
    jvmtiError error = (*Java->jvmtiEnv)->RetransformClasses(Java->jvmtiEnv, 1, clzzs);
    //MessageBoxA(NULL, "S.B.Bob", "FunGhostClient", 0);
    if (error > 0)MessageBoxA(NULL, itoa(error, c, 10), "Fish", 0);
    free(c);
    free(clzzs);//env->ReleasePrimitiveArrayCritical(classesArray,clzzs,0);


}
extern JNIEXPORT void JNICALL setEventNotificationMode
        (JNIEnv *env, jclass _, jint state,jint event){
    (*Java->jvmtiEnv)->SetEventNotificationMode(Java->jvmtiEnv,state, event, NULL);
}
extern JNIEXPORT void JNICALL clickMouse
        (JNIEnv *env, jclass _,jint event){
    // 初始化鼠标输入结构体
    MOUSEINPUT mi = {0};
    mi.dx = 0; // 鼠标水平位置
    mi.dy = 0; // 鼠标垂直位置
    mi.dwFlags = event; // 左键按下和释放

// 初始化INPUT结构体
    INPUT input = {0};
    input.type = INPUT_MOUSE;
    input.mi = mi;

// 发送输入
    SendInput(1, &input, sizeof(INPUT));
    //MessageBoxA(NULL,"CLICK MOUSE","FISH",0);
    //mouse_event(event,0,0,0,0);//(*Java->jvmtiEnv)->SetEventNotificationMode(Java->jvmtiEnv,state, event, NULL);
}
extern JNIEXPORT void JNICALL freeLibrary
        (JNIEnv *env, jclass _){
    FreeLibrary(GetModuleHandle("libagent.dll"));
}
extern JNIEXPORT void JNICALL messageBox
        (JNIEnv *jniEnv, jclass _,jstring msg,jstring title){
    const char* cmsg=(*jniEnv)->GetStringUTFChars(jniEnv,msg,false);
    const char* ctitle=(*jniEnv)->GetStringUTFChars(jniEnv,title,false);
    MessageBoxA(NULL,cmsg,ctitle,0);
    (*jniEnv)->ReleaseStringUTFChars(jniEnv,msg,cmsg);
    (*jniEnv)->ReleaseStringUTFChars(jniEnv,title,ctitle);

}
extern JNIEXPORT void JNICALL loadJar
        (JNIEnv *jniEnv, jclass _,jobject cl,jobject url){
    //FreeLibrary(GetModuleHandle("libagent.dll"));
    jclass URLClassLoader = (*jniEnv)->FindClass(jniEnv, "java/net/URLClassLoader");

    jmethodID addURL = (*jniEnv)->GetMethodID(jniEnv, URLClassLoader, "addURL", "(Ljava/net/URL;)V");
    (*jniEnv)->CallVoidMethod(jniEnv,cl,addURL,url);
}
extern JNIEXPORT int JNICALL loadJarToSystemClassLoader(const char *path){
    jclass ClassLoader = (*Java->jniEnv)->FindClass(Java->jniEnv, "java/lang/ClassLoader");
    jmethodID getSystemClassLoader = (*Java->jniEnv)->GetStaticMethodID(Java->jniEnv, ClassLoader, "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
    jclass File = (*Java->jniEnv)->FindClass(Java->jniEnv, "java/io/File");
    jclass URI = (*Java->jniEnv)->FindClass(Java->jniEnv, "java/net/URI");
    jmethodID init = (*Java->jniEnv)->GetMethodID(Java->jniEnv, File, "<init>", "(Ljava/lang/String;)V");
    jobject classloader = (*Java->jniEnv)->CallStaticObjectMethod(Java->jniEnv, ClassLoader, getSystemClassLoader);
    jmethodID toURI = (*Java->jniEnv)->GetMethodID(Java->jniEnv, File, "toURI", "()Ljava/net/URI;");
    jmethodID toURL = (*Java->jniEnv)->GetMethodID(Java->jniEnv, URI, "toURL", "()Ljava/net/URL;");
    jstring filePath = (*Java->jniEnv)->NewStringUTF(Java->jniEnv, path);
    jobject file = (*Java->jniEnv)->NewObject(Java->jniEnv, File, init, filePath);
    jobject uri = (*Java->jniEnv)->CallObjectMethod(Java->jniEnv, file, toURI);
    jobject url = (*Java->jniEnv)->CallObjectMethod(Java->jniEnv, uri, toURL);
    loadJar(Java->jniEnv,0,classloader,url);
    return 1;
}
extern JNIEXPORT void JNICALL addToSystemClassLoaderSearch(JNIEnv *jniEnv, jclass _,jstring str){
    const char* ctr=(*jniEnv)->GetStringUTFChars(jniEnv,str,false);
    (*Java->jvmtiEnv)->AddToSystemClassLoaderSearch(Java->jvmtiEnv,ctr);
    (*jniEnv)->ReleaseStringUTFChars(jniEnv,str,ctr);
}
extern JNIEXPORT int JNICALL printEx(){
    if((*Java->jniEnv)->ExceptionCheck(Java->jniEnv)){
        jthrowable e=(*Java->jniEnv)->ExceptionOccurred((Java->jniEnv));
        jclass e_class=(*Java->jniEnv)->GetObjectClass(Java->jniEnv,e);
        jmethodID e_toString_methodID = (*Java->jniEnv)->GetMethodID(Java->jniEnv, e_class, "toString", "()Ljava/lang/String;");



        jstring e_string_object = (*Java->jniEnv)->CallObjectMethod(Java->jniEnv, e, e_toString_methodID);



        MessageBoxA(NULL,(*Java->jniEnv)->GetStringUTFChars(Java->jniEnv, e_string_object, NULL),"FishCient",0);
        //MessageBoxA(NULL,buffer,"Fish",0);

        (*Java->jniEnv)->ExceptionClear(Java->jniEnv);
        return 1;
    }
    return 0;
}


extern DWORD JNICALL Inject(JAVA java){
    //MessageBoxA(NULL,"Inject","Fish",0);
    Java = &java;

    jclass system=(*Java->jniEnv)->FindClass(Java->jniEnv,"java/lang/System");
    jmethodID getEnv=(*Java->jniEnv)->GetStaticMethodID(Java->jniEnv,system,"getProperty","(Ljava/lang/String;)Ljava/lang/String;");

    jstring appdata=(*Java->jniEnv)->CallStaticObjectMethod(Java->jniEnv,system,getEnv,(*Java->jniEnv)->NewStringUTF(Java->jniEnv,"user.home"));
    char *fileName;
    fileName=(char*)(*Java->jniEnv)->GetStringUTFChars(Java->jniEnv,appdata,NULL);



    strcat(fileName, "\\fish.txt");

    FILE *read_file = fopen(fileName, "r");
    if (read_file == NULL) {

        perror("[Fish]Error opening file for reading");
        MessageBoxA(NULL,fileName,"Fish",0);
        return 1;
    }

    char buffer[1024];
    while (fgets(buffer, sizeof(buffer), read_file)) {
        //MessageBoxA(NULL,buffer,"Fish",0);
    }

    fclose(read_file); // 关闭读取文件



    /*char *c = (char *) allocate(4);
    jvmtiError error=(*Java->jvmtiEnv)->AddToSystemClassLoaderSearch(Java->jvmtiEnv,buffer);
    //jclass URLClassLoader = (*Java->jniEnv)->FindClass(Java->jniEnv, "java/net/URLClassLoader");


    if (error) {
        MessageBoxA(NULL, itoa(error, c, 10), "Fish", 0);
        MessageBoxA(NULL,buffer,"Fish",0);

        //loadJar(java.jniEnv, dirName, NULL,false);
    }
    free(c);*/
    //MessageBoxA(NULL,"Start ADDURL","Fish",0);
    //MessageBoxA(NULL,"Start ADDURL","Fish",0);
    //(*java.jvmtiEnv)->AddToBootstrapClassLoaderSearch(java.jvmtiEnv,buffer);//loadJarToSystemClassLoader(buffer);

    (*java.jvmtiEnv)->AddToSystemClassLoaderSearch(java.jvmtiEnv,buffer);//loadJarToSystemClassLoader(buffer);
    //MessageBoxA(NULL,"Start ADDURL SUCCESSFUL","Fish",0);
    if(printEx()){
        return 0;
    }

    //

    //MessageBoxA(NULL, "JNI_OnLoad3", "FunGhostClient", 0);
    jclass nativeUtils = //(*Java->jniEnv)->FindClass(Java->jniEnv,"com/fun/inject/NativeUtils");
    findClass(Java->jniEnv, "com.fun.inject.NativeUtils");
    if(printEx()){
        MessageBoxA(NULL,buffer,"Fish",0);
        return 0;
    }

    jvmtiCapabilities capabilities;
    memset(&capabilities, 0, sizeof(jvmtiCapabilities));



    //(Java->jvmtiEnv)->GetCapabilities(capabilities);
    capabilities.can_get_bytecodes = 1;
    capabilities.can_redefine_classes = 1;
    capabilities.can_redefine_any_class = 1;
    capabilities.can_generate_all_class_hook_events = 1;
    capabilities.can_retransform_classes = 1;
    capabilities.can_retransform_any_class = 1;

    (*Java->jvmtiEnv)->AddCapabilities(Java->jvmtiEnv,&capabilities);

    jvmtiEventCallbacks callbacks;
    memset(&callbacks, 0, sizeof(jvmtiEventCallbacks));

    callbacks.ClassFileLoadHook = &classFileLoadHook;

    (*Java->jvmtiEnv)->SetEventCallbacks(Java->jvmtiEnv,&callbacks, sizeof(jvmtiEventCallbacks));
    //(*Java->jvmtiEnv)->SetEventNotificationMode(Java->jvmtiEnv,JVMTI_ENABLE, JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);
    JNINativeMethod methods[] = {
            //{"getClassBytes", "(Ljava/lang/Class;)[B", (void *)&GetClassBytes},
            {"redefineClass",       "(Ljava/lang/Class;[B)V",  (void *) &Java_fun_inject_NativeUtils_redefineClass},
            {"getAllLoadedClasses", "()Ljava/util/ArrayList;", (void *) &Java_fun_inject_NativeUtils_getAllLoadedClasses},
            {"retransformClass0",   "(Ljava/lang/Class;)V",    (void *) &retransformClass0},
            {"setEventNotificationMode","(II)V",(void*) &setEventNotificationMode},
            {"clickMouse","(I)V",(void*)&clickMouse},
            {"freeLibrary","()V",(void*)&freeLibrary},
            {"loadJar","(Ljava/net/URLClassLoader;Ljava/net/URL;)V",(void*) loadJar},
            {"messageBox","(Ljava/lang/String;Ljava/lang/String;)V",(void*) messageBox},
            {"addToSystemClassLoaderSearch","(Ljava/lang/String;)V",(void*) addToSystemClassLoaderSearch}

//(Ljava/lang/String;)V
    };

    if(nativeUtils){
        (*Java->jniEnv)->RegisterNatives(Java->jniEnv,nativeUtils, methods, 9);

    }

    jclass agent = //(*Java->jniEnv)->FindClass(Java->jniEnv,"com/fun/inject/Agent");
    findClass(java.jniEnv, "com.fun.inject.Agent");

    if (!agent) {
        MessageBoxA(NULL, "no zuo no die why i try", "FishCient", 0);

    }
    jmethodID start = (*Java->jniEnv)->GetStaticMethodID(Java->jniEnv,agent, "start", "()V");//todo

    (*Java->jniEnv)->CallStaticVoidMethod(Java->jniEnv,agent, start);
    //MessageBoxA(NULL, "started", "FunGhostClient", 0);

    //if(!hook)
    return 0;
    //FreeLibrary(libAgent);

}






extern JNIEXPORT DWORD WINAPI HookMain(JNIEnv *env) {
        JAVA java;
        java.jniEnv=env;
        HMODULE hJvm = GetModuleHandle("jvm.dll");

        typedef jint(JNICALL *fnJNI_GetCreatedJavaVMs)(JavaVM **, jsize, jsize *);
        fnJNI_GetCreatedJavaVMs JNI_GetCreatedJavaVMs;
        JNI_GetCreatedJavaVMs = (fnJNI_GetCreatedJavaVMs) GetProcAddress(hJvm,
                                                                            "JNI_GetCreatedJavaVMs");

        jint num = JNI_GetCreatedJavaVMs(&java.vm, 1, NULL);

        jint num1=(*java.vm)->GetEnv(java.vm, (void **) (&java.jvmtiEnv),JVMTI_VERSION);
        char *errc = (char *) allocate(4);

        if(!java.vm)MessageBoxA(NULL, itoa(num,errc,10),"FishCient",0);
        if(!java.jvmtiEnv)MessageBoxA(NULL,itoa(num1,errc,10),"FishCient",0);


    //*jniEnv = *java.jniEnv;
        //MessageBoxA(NULL,"INJECTED","FishCient",0);
        //MessageBoxA(NULL, "JNI_OnLoad2", "FunGhostClient", 0);
        Inject(java);//CreateThread(NULL,4096,(LPTHREAD_START_ROUTINE)&Inject,NULL,0,NULL);

//typedef void(*Java_org_lwjgl_system_JNI_callP__J)(JNIEnv* env, jclass clazz, jlong lVar);
//
//Java_org_lwjgl_system_JNI_callP__J nglFlush = NULL;

        return 0;


}
typedef jstring(*JVM_GetSystemPackage)(JNIEnv *env, jstring name);
typedef void(*JVM_MonitorNotify)(JNIEnv *env, jobject obj);
typedef jlong(*JVM_CurrentTimeMillis)(JNIEnv *env, jclass ignored);
typedef void(*Java_org_lwjgl_system_JNI_callP__J)(JNIEnv* env, jclass clazz, jlong lVar);
typedef jlong(*LWJGL_GetTime)(JNIEnv* env, jclass clazz);
//Java_org_lwjgl_WindowsSysImplementation_nGetTime
typedef jlong(*JVM_GetNanoTime)(JNIEnv *env, jclass ignored);
static JVM_GetNanoTime getNanoTime;
JNIEXPORT jlong JNICALL
Hook_NanoTime(JNIEnv *env, jclass ignored){
    UnHookFunction64("jvm.dll","JVM_NanoTime");//Java_org_lwjgl_system_JNI_callP__J "lwjgl64.dll"
    //MessageBoxA(NULL,"1HOOK","Fish",0);

    jlong time = getNanoTime(env, ignored);
    //MessageBoxA(NULL,"2HOOK","Fish",0);

    HookMain(env);
    //MessageBoxA(NULL,"3HOOK","Fish",0);
    return time;
}

static JVM_MonitorNotify MonitorNotify;

static Java_org_lwjgl_system_JNI_callP__J nglFlush = NULL;
static LWJGL_GetTime nGetTime = NULL;


extern JNIEXPORT void JNICALL MonitorNotify_Hook(JNIEnv *env, jobject obj) {
    UnHookFunction64("jvm.dll","JVM_MonitorNotify");
    MonitorNotify(env, obj);
    HookMain(env);

}
extern JNIEXPORT jlong JNICALL nGetTime_Hook(JNIEnv* env, jclass clazz) {
    UnHookFunction64("lwjgl64.dll","Java_org_lwjgl_WindowsSysImplementation_nGetTime");//Java_org_lwjgl_system_JNI_callP__J "lwjgl64.dll"
    //MessageBoxA(NULL,"1HOOK","Fish",0);

    jlong time = nGetTime(env, clazz);
    //MessageBoxA(NULL,"2HOOK","Fish",0);

    HookMain(env);
    //MessageBoxA(NULL,"3HOOK","Fish",0);
    return time;


}



typedef jstring(*JVM_GetSystemPackage)(JNIEnv *env, jstring name);
PVOID WINAPI hook() {
    //MessageBoxA(NULL,"Start HOOK","Fish",0);

    HMODULE jvm = GetModuleHandle("jvm.dll");
    getNanoTime=(LWJGL_GetTime )GetProcAddress(jvm, "JVM_NanoTime");
    //MessageBoxA(NULL,"GET HOOK TARGET","Fish",0);

    HookFunction64("jvm.dll","JVM_NanoTime",(PROC) Hook_NanoTime);

    //MessageBoxA(NULL,"Finish HOOK","Fish",0);

    return NULL;
}
DWORD WINAPI start(){
    JAVA java;
    HMODULE hJvm = GetModuleHandle("jvm.dll");

    typedef jint(JNICALL *fnJNI_GetCreatedJavaVMs)(JavaVM **, jsize, jsize *);
    fnJNI_GetCreatedJavaVMs JNI_GetCreatedJavaVMs;
    JNI_GetCreatedJavaVMs = (fnJNI_GetCreatedJavaVMs) GetProcAddress(hJvm,
                                                                        "JNI_GetCreatedJavaVMs");

    jint num = JNI_GetCreatedJavaVMs(&java.vm, 1, NULL);
    if ((*java.vm)->GetEnv(java.vm,(void**)&java.jniEnv, JNI_VERSION_1_8) == JNI_EDETACHED) {
        MessageBoxA(NULL,"GetEnv","Fish",0);
        (*java.vm)->AttachCurrentThreadAsDaemon(java.vm,(void**)&java.jniEnv, NULL);
    }

    jint num1=(*java.vm)->GetEnv(java.vm, (void **) (&java.jvmtiEnv),JVMTI_VERSION);
    char *errc = (char *) allocate(4);

    if(!java.vm)MessageBoxA(NULL, itoa(num,errc,10),"FishCient",0);
    if(!java.jvmtiEnv)MessageBoxA(NULL,itoa(num1,errc,10),"FishCient",0);


    //*jniEnv = *java.jniEnv;
    //MessageBoxA(NULL,"INJECTED","FishCient",0);
    //MessageBoxA(NULL, "JNI_OnLoad2", "FunGhostClient", 0);
    Inject(java);//CreateThread(NULL,4096,(LPTHREAD_START_ROUTINE)&Inject,NULL,0,NULL);
    (*Java->vm)->DetachCurrentThread(Java->vm);


    return 0;

}



void APIENTRY entry() {
    CreateThread(NULL, 4096, (LPTHREAD_START_ROUTINE) (&hook), NULL, 0, NULL);

}
