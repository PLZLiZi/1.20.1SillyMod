/**
* Java 17 JNI
*/

#include "pch.h"

#include <jni.h>
#include <windows.h>
#include <iostream>

using namespace std;

static jstring A2JSTR(JNIEnv* env, const char* pat){
	jclass strClass = (env)->FindClass("Ljava/lang/String;");
	jmethodID ctorID = (env)->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
	jbyteArray bytes = (env)->NewByteArray(strlen(pat));
	(env)->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*)pat);
	jstring encoding = (env)->NewStringUTF("GB2312");

	return (jstring)(env)->NewObject(strClass, ctorID, bytes, encoding);
}

static char* JSTR2A(JNIEnv* env, jstring jstr){
	char* rtn = NULL;
	jclass clsstring = env->FindClass("java/lang/String");
	jmethodID mid = env->GetMethodID(clsstring, "getBytes", "()[B");
	jbyteArray barr = (jbyteArray)env->CallObjectMethod(jstr, mid);
	jsize alen = env->GetArrayLength(barr);
	jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
	if (alen > 0)
	{
		rtn = (char*)malloc(alen + 1);
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	env->ReleaseByteArrayElements(barr, ba, 0);

	return rtn;
}

static jobject GetStaticField(JNIEnv* env, jclass clazz, char* name, char* sig) {
	jfieldID id = env->GetStaticFieldID(clazz, name, sig);
	jobject obj = env->GetStaticObjectField(clazz, id);

	return obj;
}

static void SetStaticField(JNIEnv* env, jclass clazz, char* name, char* sig, jobject value) {
	jfieldID id = env->GetStaticFieldID(clazz, name, sig);
	env->SetStaticObjectField(clazz, id, value);
}

static jobject GetField(JNIEnv* env, jclass clazz, jobject zhis, char* name, char* sig) {
	jfieldID id = env->GetFieldID(clazz, name, sig);
	jobject obj = env->GetObjectField(zhis, id);

	return obj;
}

static void SetField(JNIEnv* env, jclass clazz, jobject zhis, char* name, char* type, jobject value) {
	jfieldID id = env->GetFieldID(clazz, name, type);
	env->SetObjectField(zhis, id, value);
}

static jobject InvokeStaticMethod(JNIEnv* env, jclass clazz, char* name, char* sig, va_list l) {
	jmethodID method = env->GetStaticMethodID(clazz, name, sig);
	return env->CallStaticObjectMethodV(clazz, method, l);
}

static void PutClass(JNIEnv* env, jobject obj, jclass clazz) {
	//这个直接setfield
}

