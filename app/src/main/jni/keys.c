#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_techswivel_qthemusic_source_remote_retrofit_DataInterceptor_getStagingApiKey(JNIEnv *env,
                                                                                      jobject instance) {

    return (*env)->NewStringUTF(env, "Staging API Key here");
}

JNIEXPORT jstring JNICALL
Java_com_techswivel_qthemusic_source_remote_retrofit_DataInterceptor_getDevelopmentApiKey(
        JNIEnv *env, jobject instance) {

    return (*env)->NewStringUTF(env, "Development API Key here");
}

JNIEXPORT jstring JNICALL
Java_com_techswivel_qthemusic_source_remote_retrofit_DataInterceptor_getAcceptanceApiKey(
        JNIEnv *env, jobject instance) {

    return (*env)->NewStringUTF(env, "Acceptance API Key here");
}

JNIEXPORT jstring JNICALL
Java_com_techswivel_qthemusic_source_remote_retrofit_DataInterceptor_getProductionApiKey(
        JNIEnv *env, jobject instance) {

    return (*env)->NewStringUTF(env, "Production API Key here");
}
JNIEXPORT jstring JNICALL
Java_com_techswivel_qthemusic_application_QTheMusicApplication_getGoogleClientIdStaging(JNIEnv *env,
                                                                                      jobject instance) {
    return (*env)->NewStringUTF(env, "74605093159-funodu4lv47i9u57h2meqahlcan5elo3.apps.googleusercontent.com");
}