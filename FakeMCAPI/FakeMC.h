// 下列 ifdef 块是创建使从 DLL 导出更简单的
// 宏的标准方法。此 DLL 中的所有文件都是用命令行上定义的 FAKEMC_EXPORTS
// 符号编译的。在使用此 DLL 的
// 任何项目上不应定义此符号。这样，源文件中包含此文件的任何其他项目都会将
// FAKEMC_API 函数视为是从 DLL 导入的，而此 DLL 则将用此宏定义的
// 符号视为是被导出的。
#ifdef FAKEMC_EXPORTS
#define FAKEMC_API __declspec(dllexport)
#else
#define FAKEMC_API __declspec(dllimport)
#endif

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif // __cplusplus



#ifdef __cplusplus
}
#endif // __cplusplus

#ifndef _Included_plz_lizi_api_FakeMC_JNI
#define _Included_plz_lizi_api_FakeMC_JNI
#ifdef __cplusplus
extern "C" {
#endif
	/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    PutClass
 * Signature: (Ljava/lang/Object;Ljava/lang/Class;)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_PutClass
  (JNIEnv *, jclass, jobject, jclass);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    FakeClient
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FakeClient
  (JNIEnv *, jclass);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    FakeLevel
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FakeLevel
  (JNIEnv *, jclass, jobject);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    FakeSafePlayer
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FakeSafePlayer
  (JNIEnv *, jclass, jobject);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    FakeDeadPlayer
 * Signature: (Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FakeDeadPlayer
  (JNIEnv *, jclass, jobject);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    KillJVM
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_KillJVM
  (JNIEnv *, jclass);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    FuckTask
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FuckTask
  (JNIEnv *, jclass);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    FuckWindow
 * Signature: (Ljava/lang/String)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FuckWindow
  (JNIEnv *, jclass, jstring);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    LockWindow
 * Signature: (Ljava/lang/String)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_LockWindow
  (JNIEnv *, jclass, jstring);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    UnlockWindow
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_UnlockWindow
  (JNIEnv *, jclass);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    FuckSwapBuffers
 * Signature: (Ljava/lang/String;J)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_FuckSwapBuffers
  (JNIEnv *, jclass, jstring, jlong);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    HookToEmpty
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_HookToEmpty
  (JNIEnv *, jclass, jstring, jstring);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    UnHook
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_UnHook
  (JNIEnv *, jclass, jstring, jstring);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    IsKeyDown
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_plz_lizi_api_FakeMC_IsKeyDown
(JNIEnv*, jclass, jint key);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    PosCursor
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_PosCursor
(JNIEnv* env, jclass, jint x, jint y);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    CursorX
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_plz_lizi_api_FakeMC_CursorX
(JNIEnv* env, jclass);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    CursorY
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_plz_lizi_api_FakeMC_CursorY
(JNIEnv* env, jclass);

/*
* Class:     plz_lizi_api_FakeMC
* Method:    LoadedClasses
* Signature: (Ljava/lang/ClassLoader;)Ljava/util/ArrayList;
*/
JNIEXPORT jobject JNICALL Java_plz_lizi_api_FakeMC_LoadedClasses
(JNIEnv* env, jclass, jobject loader) {
	jclass ldrclazz = env->GetObjectClass(loader);
	jfieldID lcsID = env->GetFieldID(ldrclazz, "classes", "Ljava/util/ArrayList;");
	return env->GetObjectField(loader, lcsID);
}

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    RenderObj
 * Signature: (Ljava/lang/String;Ljava/lang/String;IIII)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_RObj
  (JNIEnv * env, jclass, jstring, jstring, jint, jint, jint, jint);

/*
 * Class:     plz_lizi_api_FakeMC
 * Method:    RenderObj
 * Signature: (Ljava/lang/String;IIII)V
 */
JNIEXPORT void JNICALL Java_plz_lizi_api_FakeMC_RObjP
(JNIEnv* env, jclass, jstring jimgp, jint x, jint y, jint w, jint h);

#ifdef __cplusplus
}
#endif
#endif
