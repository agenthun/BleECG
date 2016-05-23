package com.agenthun.bleecg.connectivity.manager;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/6 下午6:04.
 */
public class XMLGsonConverterFactory extends Converter.Factory {
    public static XMLGsonConverterFactory create() {
        return create(new Gson());
    }

    public static XMLGsonConverterFactory create(Gson gson) {
        return new XMLGsonConverterFactory(gson);
    }

    private final Gson gson;

    private XMLGsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new XMLGsonResponseBodyConverter<>(gson, adapter);
    }
}
