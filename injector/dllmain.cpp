//
// Created by admin on 2024/4/7.
//
#include <windows.h>

#include "jni.h"
#include "jvmti.h"

#define true 1
#define false 0

extern "C" {
typedef struct {
    JavaVM *vm;
    JNIEnv *jniEnv;
    jvmtiEnv *jvmtiEnv;
} JAVA;
JAVA *Java;
jobject *classLoader;

/*jobject GetClassLoader(JNIEnv *env,jvmtiEnv *jvmti){
    jint classcount = 0;
    jclass *classes = NULL;
    (*jvmti)->GetLoadedClasses((jvmtiEnv *)jvmti, &classcount, &classes);

    for(int i=0;i<classcount;i++){
        jclass klz=classes[i];
        jmethodID getName=env->GetStaticMethodID(klz,"getName","()V");

    }
}*/
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

jclass JNICALL findClass(JNIEnv *jniEnv, const char *name) {
    jclass ClassLoader = (*jniEnv)->FindClass(jniEnv, "java/lang/ClassLoader");

    //jmethodID findClass = (*jniEnv)->GetMethodID(jniEnv, URLClassLoader, "findClass", "(Ljava/lang/String;)Ljava/lang/Class;");
    jmethodID getSystemClassLoader = (*jniEnv)->GetStaticMethodID(jniEnv, ClassLoader, "getSystemClassLoader",
                                                                            "()Ljava/lang/ClassLoader;");

    jobject classloader;
    classloader = (*jniEnv)->CallStaticObjectMethod(jniEnv, ClassLoader, getSystemClassLoader);
    jmethodID loadClass = (*jniEnv)->GetMethodID(jniEnv, jniEnv->GetObjectClass(classloader), "loadClass",
                                                           "(Ljava/lang/String;)Ljava/lang/Class;");

    return (jclass) (*jniEnv)->CallObjectMethod(jniEnv, classloader, loadClass,
                                                          (*jniEnv)->NewStringUTF(jniEnv, name));
}

jclass JNICALL findClass1(JNIEnv *jniEnv, const char *name, jobject classloader) {

    jmethodID loadClass = (*jniEnv)->GetMethodID(jniEnv, jniEnv->GetObjectClass(classloader), "loadClass",
                                                           "(Ljava/lang/String;)Ljava/lang/Class;");

    return (jclass) (*jniEnv)->CallObjectMethod(jniEnv, classloader, loadClass,
                                                          (*jniEnv)->NewStringUTF(jniEnv, name));
}


void JNICALL loadJar(JNIEnv *jniEnv, const char *path, jobject thread, bool useThread) {
    if (!useThread) {
        jclass URLClassLoader = (*jniEnv)->FindClass(jniEnv, "java/net/URLClassLoader");
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
        jmethodID addURL = (*jniEnv)->GetMethodID(jniEnv, jniEnv->GetObjectClass(classloader), "addURL",
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

void JNICALL classFileLoadHook(jvmtiEnv * jvmti_env, JNIEnv * env,
                               jclass
class_being_redefined ,
jobject loader,
const char *name, jobject
protection_domain ,
jint class_data_len,
const unsigned char *class_data,
        jint
* new_class_data_len ,
unsigned char **new_class_data
)
{
if ( name == NULL || ( name [ 0 ] == 'f' &&
name [ 1 ] == 'u' &&
name [ 2 ] == 'n' )) {

return ;
}
//MessageBoxA(NULL, name, "FunGhostClient", NULL);
jclass nativeUtils = findClass(env, "fun.inject.NativeUtils");

if(!nativeUtils){
return;
}
jmethodID transform = env->GetStaticMethodID(nativeUtils, "transform",
                                             "(Ljava/lang/ClassLoader;Ljava/lang/String;Ljava/lang/Class;Ljava/security/ProtectionDomain;[B)[B");//todo
//MessageBoxA(NULL,name,"FunGhostClient",NULL);

jbyteArray oldBytes = (*env).NewByteArray(class_data_len);
(*env).
SetByteArrayRegion( oldBytes,
0, class_data_len, (jbyte *)class_data);
//MessageBoxA(NULL,"$","FunGhostClient",NULL);

auto newBytes = (jbyteArray) (env->CallStaticObjectMethod(nativeUtils, transform,
                                                          loader, env->NewStringUTF(name), class_being_redefined,
                                                          protection_domain, oldBytes));
//MessageBoxA(NULL,"%","FunGhostClient",NULL);

jsize size = env->GetArrayLength(newBytes);
jbyte *classByteArray = (*env).GetByteArrayElements(newBytes, NULL);
*
new_class_data = (unsigned char *) classByteArray;
*
new_class_data_len = size;

//    env->ReleaseByteArrayElements(newBytes,classByteArray,0);


}

char *cut_str(char *dest, const char *src, int size) {
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
JNIEXPORT jobject JNICALL Java_fun_inject_NativeUtils_getAllLoadedClasses
        (JNIEnv *env, jclass) {
    JavaVM *vm;
    jvmtiEnv *jvmti;
    env->GetJavaVM(&vm);
    vm->GetEnv((void **) &jvmti, JVMTI_VERSION);
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
JNIEXPORT void JNICALL Java_fun_inject_NativeUtils_redefineClass
        (JNIEnv *env, jclass, jclass clazz, jbyteArray bytes) {
    JAVA java;
    env->GetJavaVM(&java.vm);
    java.vm->GetEnv((void **) &(java.jvmtiEnv), JVMTI_VERSION);
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

    java.jvmtiEnv->AddCapabilities(&capabilities);
    ///////
    jint size = env->GetArrayLength(bytes);
    jbyte *classByteArray = (*env).GetByteArrayElements(bytes, NULL);

    jvmtiClassDefinition definition[] = {
            {clazz, size, (unsigned char *) classByteArray}
    };
    java.jvmtiEnv->RedefineClasses(1, definition);
    env->ReleaseByteArrayElements(bytes, classByteArray, 0);
}

void *allocate(jlong size) {
    void *resultBuffer = malloc(size);
    return resultBuffer;
}

jvmtiCapabilities getCapabilities() {
    jvmtiCapabilities capabilities = {
            1,
            1,
            1,
            1,
            1, 1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,


    };
    return capabilities;
}


/*
 * Class:     fun_inject_NativeUtils
 * Method:    retransformClass0
 * Signature: (Ljava/lang/Class;)V
 */
JNIEXPORT void JNICALL Java_fun_inject_NativeUtils_retransformClass0
        (JNIEnv *env, jclass, jclass target) {

    if (!Java->jvmtiEnv)MessageBoxA(NULL, "S.B.Bob", "FunGhostClient", NULL);
    jclass *clzzs = (jclass *) allocate(sizeof(jclass) * 1);
    clzzs[0] = target;
    char *c = (char *) allocate(4);
    jvmtiError error = Java->jvmtiEnv->functions->RetransformClasses(Java->jvmtiEnv, 1, clzzs);
    if (error > 0)MessageBoxA(NULL, itoa(error, c, 10), "Fish", NULL);
    free(c);
    free(clzzs);//env->ReleasePrimitiveArrayCritical(classesArray,clzzs,0);


}


DWORD WINAPI Main() {
    //MessageBoxA(NULL,"INJECTED","FishCient",NULL);
    HMODULE hJvm = GetModuleHandle("jvm.dll");
    JAVA java;
    JavaVM *jvm;
    JNIEnv *jniEnv;
    typedef jint(JNICALL *fnJNI_GetCreatedJavaVMs)(JavaVM **, jsize, jsize *);
    fnJNI_GetCreatedJavaVMs JNI_GetCreatedJavaVMs = (fnJNI_GetCreatedJavaVMs) GetProcAddress(hJvm,
                                                                                             "JNI_GetCreatedJavaVMs");
    jint num = JNI_GetCreatedJavaVMs(&jvm, 1, NULL);
    if (num != JNI_OK)return 0;
    java.vm = jvm;
    if (java.vm->functions->AttachCurrentThread(java.vm, reinterpret_cast<void **>(&java.jniEnv), NULL) != JNI_OK) {
        MessageBoxA(NULL, "NO", "FishCient", NULL);
        return 0;
    }
    java.vm->functions->GetEnv(java.vm, (void **) (&java.jvmtiEnv), JVMTI_VERSION);
    jniEnv = java.jniEnv;
    Java = &java;
    jclass threadClass = (*jniEnv)->FindClass(jniEnv, "java/lang/Thread");
    jmethodID getAllStackTraces = (*jniEnv)->GetStaticMethodID(jniEnv, threadClass, "getAllStackTraces",
                                                                         "()Ljava/util/Map;");
    if (!getAllStackTraces)
        return 0;
    jobjectArray threads = (jobjectArray) (*jniEnv)->CallObjectMethod(jniEnv,
                                                                                (*jniEnv)->CallObjectMethod(
                                                                                        jniEnv,
                                                                                        (*jniEnv)->CallStaticObjectMethod(
                                                                                                jniEnv, threadClass,
                                                                                                getAllStackTraces),
                                                                                        (*jniEnv)->GetMethodID(
                                                                                                jniEnv,
                                                                                                (*jniEnv)->FindClass(
                                                                                                        jniEnv,
                                                                                                        "java/util/Map"),
                                                                                                "keySet",
                                                                                                "()Ljava/util/Set;")),
                                                                                (*jniEnv)->GetMethodID(jniEnv,
                                                                                                                 (*jniEnv)->FindClass(
                                                                                                                         jniEnv,
                                                                                                                         "java/util/Set"),
                                                                                                                 "toArray",
                                                                                                                 "()[Ljava/lang/Object;"));
    if (!threads)
        return 0;
    jsize arrlength = (*jniEnv)->GetArrayLength(jniEnv, threads);
    jobject clientThread = NULL;
    for (int i = 0; i < arrlength; i++) {
        jobject thread = (*jniEnv)->GetObjectArrayElement(jniEnv, threads, i);
        if (thread == NULL)
            continue;
        jclass threadClass = (*jniEnv)->GetObjectClass(jniEnv, thread);
        jstring name = (jstring) (*jniEnv)->CallObjectMethod(jniEnv, thread,
                                                                       (*jniEnv)->GetMethodID(jniEnv,
                                                                                                        threadClass,
                                                                                                        "getName",
                                                                                                        "()Ljava/lang/String;"));
        const char *str = (*jniEnv)->GetStringUTFChars(jniEnv, name, (jboolean *) false);
        if (!strcmp(str, "Client thread")) {
            clientThread = thread;
            (*jniEnv)->ReleaseStringUTFChars(jniEnv, name, str);
            break;
        }
        (*jniEnv)->ReleaseStringUTFChars(jniEnv, name, str);
    }

    if (!clientThread)
        return 0;
    //MessageBoxA(NULL,"GETTHREAD","FISHClient",MB_OK);

    char fileName[MAX_PATH];

    GetModuleFileNameA(GetModuleHandle("libagent.dll"), fileName, MAX_PATH);
    char dirName[sizeof(fileName)];
    int last = 0;
    for (int i = 0; i < sizeof(fileName); i++) {
        if (fileName[i] == '\\') {
            last = i;
        }
    }

    cut_str(dirName, fileName, last + 1);
    strcat(dirName, "FunGhostClient.jar");
    loadJar(java.jniEnv, dirName, NULL, false);

    jclass nativeUtils = findClass(Java->jniEnv, "fun.inject.NativeUtils");


    jvmtiCapabilities capabilities;
    memset(&capabilities, 0, sizeof(jvmtiCapabilities));



    //(Java->jvmtiEnv)->GetCapabilities(capabilities);
    capabilities.can_get_bytecodes = 1;
    capabilities.can_redefine_classes = 1;
    capabilities.can_redefine_any_class = 1;
    capabilities.can_generate_all_class_hook_events = 1;
    capabilities.can_retransform_classes = 1;
    capabilities.can_retransform_any_class = 1;

    Java->jvmtiEnv->AddCapabilities(&capabilities);

    jvmtiEventCallbacks callbacks;
    memset(&callbacks, 0, sizeof(jvmtiEventCallbacks));

    callbacks.ClassFileLoadHook = &classFileLoadHook;

    (Java->jvmtiEnv)->SetEventCallbacks(&callbacks, sizeof(jvmtiEventCallbacks));
    (Java->jvmtiEnv)->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);
    JNINativeMethod methods[] = {
            //{"getClassBytes", "(Ljava/lang/Class;)[B", (void *)&GetClassBytes},
            {"redefineClass",       "(Ljava/lang/Class;[B)V",  (void *) &Java_fun_inject_NativeUtils_redefineClass},
            {"getAllLoadedClasses", "()Ljava/util/ArrayList;", (void *) &Java_fun_inject_NativeUtils_getAllLoadedClasses},
            {"retransformClass0",   "(Ljava/lang/Class;)V",    (void *) &Java_fun_inject_NativeUtils_retransformClass0}

    };
    Java->jniEnv->RegisterNatives(nativeUtils, methods, 3);


    //jmethodID transform=java.jniEnv->GetStaticMethodID(nativeutils,"transform","(Ljava/lang/ClassLoader;Ljava/lang/String;Ljava/lang/Class;Ljava/security/ProtectionDomain;[B)[B");//todo

    //MessageBoxA(NULL,"loaded jar","FishCient",NULL);
    jclass agent = findClass(java.jniEnv, "fun.inject.Agent");
    if (!agent) {
        MessageBoxA(NULL, "no zuo no die why i try", "FishCient", NULL);

    }

    jmethodID start = java.jniEnv->GetStaticMethodID(agent, "start", "()V");//todo

    java.jniEnv->CallStaticVoidMethod(agent, start);
    (Java->jvmtiEnv)->SetEventNotificationMode(JVMTI_DISABLE, JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, NULL);

    jclass fishFrame = findClass(jniEnv, "fun.gui.FishFrame");
    jmethodID init = jniEnv->GetStaticMethodID(fishFrame, "init", "()V");
    jniEnv->CallStaticVoidMethod(fishFrame, init);

    (*Java->vm).DetachCurrentThread();
    return 0;

}


BOOL APIENTRY DllMain(HMODULE, DWORD reason, LPVOID lpReserved) {
    if (reason == DLL_PROCESS_ATTACH) {
        CreateThread(NULL, 4096, (LPTHREAD_START_ROUTINE) (&Main), NULL, 0, NULL);
    }
    return TRUE;
}

}