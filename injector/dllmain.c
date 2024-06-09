//
// Created by admin on 2024/4/7.
//
#include <windows.h>
#include "jni.h"
#include "utils.h"
#include "jvmti.h"

#define true 1
#define bool int
#define false 0
#define NEW_STR_SIZE 1024

// 替换字符串中的oldSubstr为newSubstr
char* replace(const char* text, const char* oldSubstr, const char* newSubstr) {
    char *result = malloc(NEW_STR_SIZE); // 初始分配
    if (result == NULL) return NULL; // 检查内存分配是否成功

    char *insertPoint = result; // 指向结果字符串中下一个可插入字符的位置
    const char *temp = text; // 指向当前文本的读入点

    while (1) {
        const char *p = strstr(temp, oldSubstr);
        // 如果没有找到oldSubstr，复制剩余的文本并退出循环
        if (p == NULL) {
            strcpy(insertPoint, temp);
            break;
        }

        // 计算oldSubstr出现之前文本的长度并复制
        size_t len = p - temp;
        memcpy(insertPoint, temp, len);
        insertPoint += len;
        temp = p;

        // 复制newSubstr到结果字符串中
        memcpy(insertPoint, newSubstr, strlen(newSubstr));
        insertPoint += strlen(newSubstr);
        temp += strlen(oldSubstr);
    }

    // 确保结果字符串以空字符结尾
    *insertPoint = '\0';

    // 调整结果字符串的大小以适应实际内容
    char *finalResult = realloc(result, strlen(result) + 1);
    return finalResult; // 返回动态调整后的内存
}




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

extern JNIEXPORT jclass JNICALL findClass(JNIEnv *jniEnv, const char *name) {
    jclass ClassLoader = (*jniEnv)->FindClass(jniEnv, "java/lang/ClassLoader");
    if(!ClassLoader)MessageBoxA(NULL, "no zuo no die why classLoader try", "FishCient", 0);


    //jmethodID findClass = (*jniEnv)->GetMethodID(jniEnv, URLClassLoader, "findClass", "(Ljava/lang/String;)Ljava/lang/Class;");
    jmethodID getSystemClassLoader = (*jniEnv)->GetStaticMethodID(jniEnv, ClassLoader, "getSystemClassLoader",
                                                                            "()Ljava/lang/ClassLoader;");

    jobject classloader;
    classloader = (*jniEnv)->CallStaticObjectMethod(jniEnv, ClassLoader, getSystemClassLoader);
    jmethodID loadClass = (*jniEnv)->GetMethodID(jniEnv, (*jniEnv)->GetObjectClass(jniEnv,classloader), "loadClass",
                                                           "(Ljava/lang/String;)Ljava/lang/Class;");

    jclass c = (jclass) (*jniEnv)->CallObjectMethod(jniEnv, classloader, loadClass,
                                                          (*jniEnv)->NewStringUTF(jniEnv, name));
    return c;
    //return (*jniEnv)->FindClass(jniEnv,name);
}

jclass JNICALL findClass2(JNIEnv *jniEnv, const char *name, jobject classloader) {

    jmethodID loadClass = (*jniEnv)->GetMethodID(jniEnv, (*jniEnv)->GetObjectClass(jniEnv,classloader), "loadClass",
                                                           "(Ljava/lang/String;)Ljava/lang/Class;");

    return (jclass) (*jniEnv)->CallObjectMethod(jniEnv, classloader, loadClass,
                                                          (*jniEnv)->NewStringUTF(jniEnv, name));
}


void JNICALL loadJar(JNIEnv *jniEnv, const char *path, jobject thread, bool useThread) {
    if (!useThread) {

        //jclass URLClassLoader = (*jniEnv)->FindClass(jniEnv, "java/net/URLClassLoader");
        jclass ClassLoader = (*jniEnv)->FindClass(jniEnv, "java/lang/ClassLoader");

        if (!ClassLoader) {
            return;
        }
        jclass URI = (*jniEnv)->FindClass(jniEnv, "java/net/URI");
        jclass File = (*jniEnv)->FindClass(jniEnv, "java/io/File");

        jmethodID init = (*jniEnv)->GetMethodID(jniEnv, File, "<init>", "(Ljava/lang/String;)V");
        jmethodID toURI = (*jniEnv)->GetMethodID(jniEnv, File, "toURI", "()Ljava/net/URI;");
        jmethodID toURL = (*jniEnv)->GetMethodID(jniEnv, URI, "toURL", "()Ljava/net/URL;");
        jmethodID getSystemClassLoader = (*jniEnv)->GetStaticMethodID(jniEnv, ClassLoader,
                                                                                "getSystemClassLoader",
                                                                                "()Ljava/lang/ClassLoader;");

        jstring filePath = (*jniEnv)->NewStringUTF(jniEnv, path);
        jobject file = (*jniEnv)->NewObject(jniEnv, File, init, filePath);
        jobject uri = (*jniEnv)->CallObjectMethod(jniEnv, file, toURI);
        jobject url = (*jniEnv)->CallObjectMethod(jniEnv, uri, toURL);
        jobject classloader;

        classloader = (*jniEnv)->CallStaticObjectMethod(jniEnv, ClassLoader, getSystemClassLoader);
        jmethodID addURL = (*jniEnv)->GetMethodID(jniEnv, (*jniEnv)->GetObjectClass(jniEnv,classloader), "addURL",
                                                            "(Ljava/net/URL;)V");

        (*jniEnv)->CallVoidMethod(jniEnv, classloader, addURL, url);

    } else {
        jclass URLClassLoader = (*jniEnv)->FindClass(jniEnv, "java/net/URLClassLoader");
        jclass ClassLoader = (*jniEnv)->FindClass(jniEnv, "java/lang/ClassLoader");
        jclass URI = (*jniEnv)->FindClass(jniEnv, "java/net/URI");
        jclass File = (*jniEnv)->FindClass(jniEnv, "java/io/File");
        jclass Thread = (*jniEnv)->GetObjectClass(jniEnv, thread);

        jmethodID init = (*jniEnv)->GetMethodID(jniEnv, File, "<init>", "(Ljava/lang/String;)V");
        jmethodID addURL = (*jniEnv)->GetMethodID(jniEnv, URLClassLoader, "addURL", "(Ljava/net/URL;)V");
        jmethodID toURI = (*jniEnv)->GetMethodID(jniEnv, File, "toURI", "()Ljava/net/URI;");
        jmethodID toURL = (*jniEnv)->GetMethodID(jniEnv, URI, "toURL", "()Ljava/net/URL;");
        jmethodID getContextClassLoader = (*jniEnv)->GetMethodID(jniEnv, Thread, "getContextClassLoader",
                                                                           "()Ljava/lang/ClassLoader;");
        jmethodID getSystemClassLoader = (*jniEnv)->GetMethodID(jniEnv, ClassLoader,
                                                                          "getSystemClassLoader",
                                                                          "()Ljava/lang/ClassLoader;");

        jstring filePath = (*jniEnv)->NewStringUTF(jniEnv, path);
        jobject file = (*jniEnv)->NewObject(jniEnv, File, init, filePath);
        jobject uri = (*jniEnv)->CallObjectMethod(jniEnv, file, toURI);
        jobject url = (*jniEnv)->CallObjectMethod(jniEnv, uri, toURL);
        jobject classloader;

        if (useThread) {
            classloader = (*jniEnv)->CallObjectMethod(jniEnv, thread, getContextClassLoader);
        } else {
            classloader = (*jniEnv)->CallStaticObjectMethod(jniEnv, ClassLoader, getSystemClassLoader);
        }

        (*jniEnv)->CallVoidMethod(jniEnv, classloader, addURL, url);
    }


}


//
int str_endwith(const char *str, const char *reg) {
    int l1 = strlen(str), l2 = strlen(reg);
    if (l1 < l2)
        return 0;
    str += l1 - l2;
    while (*str && *reg && *str == *reg) {
        str++;
        reg++;
    }
    if (!*str && !*reg)
        return 1;
    return 0;
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
    if(name==NULL||(name[0]=='f'&&
                    name[1]=='u'&&
                    name[2]=='n')||(
                    name[0]=='j'&&
                    name[1]=='a'&&
                    name[2]=='v'&&
                    name[3]=='a')){
        *new_class_data=class_data;
        *new_class_data_len=class_data_len;
        return;
    }
//MessageBoxA(NULL, name, "FunGhostClient", NULL);
    jclass nativeUtils = findClass(env, "fun.inject.NativeUtils");

    if(!nativeUtils){
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
extern JNIEXPORT void JNICALL Java_fun_inject_NativeUtils_retransformClass0
        (JNIEnv *env, jclass _, jclass target) {

    if (!Java->jvmtiEnv)MessageBoxA(NULL, "S.B.Bob", "FunGhostClient", 0);
    jclass *clzzs = (jclass *) allocate(sizeof(jclass) * 1);
    clzzs[0] = target;
    char *c = (char *) allocate(4);
    jvmtiError error = (*Java->jvmtiEnv)->RetransformClasses(Java->jvmtiEnv, 1, clzzs);
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




DWORD WINAPI Main() {
    //MessageBoxA(NULL, "JNI_OnLoad", "FunGhostClient", 0);
    HMODULE hJvm = GetModuleHandle("jvm.dll");

    JAVA java;
    JavaVM *jvm;
    //JNIEnv *jniEnv;
    typedef jint(JNICALL *fnJNI_GetCreatedJavaVMs)(JavaVM **, jsize, jsize *);
    fnJNI_GetCreatedJavaVMs JNI_GetCreatedJavaVMs;
    JNI_GetCreatedJavaVMs = (fnJNI_GetCreatedJavaVMs) GetProcAddressPeb(hJvm,
                                                                        "JNI_GetCreatedJavaVMs");

    jint num = JNI_GetCreatedJavaVMs(&jvm, 1, NULL);

    if (num != JNI_OK)return 0;
    java.vm = jvm;
    if ((*java.vm)->AttachCurrentThread(java.vm, (void **)(&java.jniEnv), NULL) != JNI_OK) {
        MessageBoxA(NULL, "NO", "FishCient", 0);
        return 0;
    }
    //

    (*java.vm)->GetEnv(java.vm, (void **) (&java.jvmtiEnv),JVMTI_VERSION);
    //*jniEnv = *java.jniEnv;
    //MessageBoxA(NULL,"INJECTED","FishCient",0);
    //MessageBoxA(NULL, "JNI_OnLoad2", "FunGhostClient", 0);
    Java = &java;
    //



    char fileName[MAX_PATH];
    HANDLE libAgent=GetModuleHandle("libagent.dll");
    GetModuleFileName(libAgent, fileName, MAX_PATH);
    char dirName[sizeof(fileName)];
    int last = 0;
    for (int i = 0; i < sizeof(fileName); i++) {
        if (fileName[i] == '\\') {
            //fileName[i]='/';
            last = i;
        }
    }


    cut_str(dirName, fileName, last + 1);

    strcat(dirName, "FunGhostClient.jar");

    //



     char *c = (char *) allocate(4);
     jvmtiError error=(*java.jvmtiEnv)->AddToSystemClassLoaderSearch(java.jvmtiEnv,dirName);
     if (error) {
         MessageBoxA(NULL, itoa(error, c, 10), "Fish", 0);
         loadJar(java.jniEnv, dirName, NULL,false);
     }
     free(c);

    //

    //MessageBoxA(NULL, "JNI_OnLoad3", "FunGhostClient", 0);
    jclass nativeUtils = (*Java->jniEnv)->FindClass(Java->jniEnv,"fun/inject/NativeUtils");//findClass(Java->jniEnv, "fun.inject.NativeUtils");
    if((*Java->jniEnv)->ExceptionCheck(Java->jniEnv)){
        jthrowable e=(*Java->jniEnv)->ExceptionOccurred((Java->jniEnv));
        jclass e_class=(*Java->jniEnv)->GetObjectClass(Java->jniEnv,e);
        jmethodID e_toString_methodID = (*Java->jniEnv)->GetMethodID(Java->jniEnv, e_class, "toString", "()Ljava/lang/String;");



        jstring e_string_object = (*Java->jniEnv)->CallObjectMethod(Java->jniEnv, e, e_toString_methodID);



        MessageBoxA(NULL,(*Java->jniEnv)->GetStringUTFChars(Java->jniEnv, e_string_object, NULL),"FishCient",0);
        (*Java->jniEnv)->ExceptionClear(Java->jniEnv);
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
    JNINativeMethod methods[6] = {
            //{"getClassBytes", "(Ljava/lang/Class;)[B", (void *)&GetClassBytes},
            {"redefineClass",       "(Ljava/lang/Class;[B)V",  (void *) &Java_fun_inject_NativeUtils_redefineClass},
            {"getAllLoadedClasses", "()Ljava/util/ArrayList;", (void *) &Java_fun_inject_NativeUtils_getAllLoadedClasses},
            {"retransformClass0",   "(Ljava/lang/Class;)V",    (void *) &Java_fun_inject_NativeUtils_retransformClass0},
            {"setEventNotificationMode","(II)V",(void*) &setEventNotificationMode},
            {"clickMouse","(I)V",(void*)&clickMouse},
            {"freeLibrary","()V",(void*)&freeLibrary}

    };

    if(nativeUtils){
        (*Java->jniEnv)->RegisterNatives(java.jniEnv,nativeUtils, methods, 6);

        //MessageBoxA(NULL,"reg natives","FishCient",0);
    }

    jclass agent = (*Java->jniEnv)->FindClass(Java->jniEnv,"fun/inject/Agent");//findClass(java.jniEnv, "fun.inject.Agent");

    if (!agent) {
        MessageBoxA(NULL, "no zuo no die why i try", "FishCient", 0);

    }

    jmethodID start = (*java.jniEnv)->GetStaticMethodID(java.jniEnv,agent, "start", "()V");//todo

    (*java.jniEnv)->CallStaticVoidMethod(java.jniEnv,agent, start);
    //MessageBoxA(NULL, "started", "FunGhostClient", 0);


    (*Java->vm)->DetachCurrentThread(Java->vm);
    //FreeLibrary(libAgent);
    return 0;

}



BOOL APIENTRY DllMain(HMODULE _h, DWORD reason, LPVOID lpReserved) {
    if (reason == DLL_PROCESS_ATTACH) {
        Java=NULL;
        CreateThread(NULL, 4096, (LPTHREAD_START_ROUTINE) (&Main), NULL, 0, NULL);
    }
    return TRUE;
}