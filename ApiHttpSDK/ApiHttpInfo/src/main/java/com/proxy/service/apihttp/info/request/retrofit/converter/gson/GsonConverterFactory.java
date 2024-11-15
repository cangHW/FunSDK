package com.proxy.service.apihttp.info.request.retrofit.converter.gson;


import androidx.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author: cangHX
 * @data: 2024/6/22 17:56
 * @desc:
 */
public class GsonConverterFactory extends Converter.Factory {

    public static GsonConverterFactory create() {
        return new GsonConverterFactory();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            @NonNull Type type,
            @NonNull Annotation[] parameterAnnotations,
            @NonNull Annotation[] methodAnnotations,
            @NonNull Retrofit retrofit
    ) {
        return new GsonRequestBodyConverter<>(type);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            @NonNull Type type,
            @NonNull Annotation[] annotations,
            @NonNull Retrofit retrofit
    ) {
        return new GsonResponseBodyConverter<>(type);
    }

}
