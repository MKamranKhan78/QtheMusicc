#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_techswivel_baseproject_source_remote_retrofit_DataInterceptor_getStagingApiKey(JNIEnv *env,
                                                                                        jobject instance) {

    return (*env)->NewStringUTF(env, "Staging API Key here");
}

JNIEXPORT jstring JNICALL
Java_com_techswivel_baseproject_source_remote_retrofit_DataInterceptor_getDevelopmentApiKey(
        JNIEnv *env, jobject instance) {

    return (*env)->NewStringUTF(env, "Development API Key here");
}

JNIEXPORT jstring JNICALL
Java_com_techswivel_baseproject_source_remote_retrofit_DataInterceptor_getAcceptanceApiKey(
        JNIEnv *env, jobject instance) {

    return (*env)->NewStringUTF(env, "Acceptance API Key here");
}

JNIEXPORT jstring JNICALL
Java_com_techswivel_baseproject_source_remote_retrofit_DataInterceptor_getProductionApiKey(
        JNIEnv *env, jobject instance) {

    return (*env)->NewStringUTF(env, "Production API Key here");
}