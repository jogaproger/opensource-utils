/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class hu_qgears_opengl_osmesa_OSMesa */

#ifndef _Included_hu_qgears_opengl_osmesa_OSMesa
#define _Included_hu_qgears_opengl_osmesa_OSMesa
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     hu_qgears_opengl_osmesa_OSMesa
 * Method:    createContext
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_hu_qgears_opengl_osmesa_OSMesa_createContext
  (JNIEnv *, jobject);

/*
 * Class:     hu_qgears_opengl_osmesa_OSMesa
 * Method:    makeCurrentPrivate
 * Signature: (Ljava/nio/ByteBuffer;II)V
 */
JNIEXPORT void JNICALL Java_hu_qgears_opengl_osmesa_OSMesa_makeCurrentPrivate
  (JNIEnv *, jobject, jobject, jint, jint);

/*
 * Class:     hu_qgears_opengl_osmesa_OSMesa
 * Method:    disposeContext
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_hu_qgears_opengl_osmesa_OSMesa_disposeContext
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif