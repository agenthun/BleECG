package com.agenthun.bleecg.connectivity.manager;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/6 下午8:11.
 */
public class XMLGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    XMLGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Document document = Jsoup.parse(value.string());
        String gsonString = document.tagName("string").text();

        JsonReader jsonReader = gson.newJsonReader(new StringReader(gsonString));
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
