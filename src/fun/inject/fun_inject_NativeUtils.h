/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class fun_inject_NativeUtils */

#ifndef _Included_fun_inject_NativeUtils
#define _Included_fun_inject_NativeUtils
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     fun_inject_NativeUtils
 * Method:    getAllLoadedClasses
 * Signature: ()Ljava/util/ArrayList;
 */
JNIEXPORT jobject JNICALL Java_fun_inject_NativeUtils_getAllLoadedClasses
  (JNIEnv *, jclass);

/*
 * Class:     fun_inject_NativeUtils
 * Method:    redefineClass
 * Signature: (Ljava/lang/Class;[B)V
 */
JNIEXPORT void JNICALL Java_fun_inject_NativeUtils_redefineClass
  (JNIEnv *, jclass, jclass, jbyteArray);

/*
 * Class:     fun_inject_NativeUtils
 * Method:    retransformClass0
 * Signature: ([Ljava/lang/Class;)V
 */
JNIEXPORT void JNICALL Java_fun_inject_NativeUtils_retransformClass0
  (JNIEnv *, jclass, jobjectArray);

#ifdef __cplusplus
}
#endif
#endif
