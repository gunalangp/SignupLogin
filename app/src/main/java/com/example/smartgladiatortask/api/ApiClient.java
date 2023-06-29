package com.example.smartgladiatortask.api;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder adapterBuilder;

    public ApiClient() {
        createDefaultAdapter();
    }

    private void createDefaultAdapter() {
        Gson gson = new Gson();

        okBuilder = new OkHttpClient.Builder();

        // connection timeout 60 seconds/ 1 minute
        okBuilder.connectTimeout(240, TimeUnit.SECONDS);
        okBuilder.readTimeout(240, TimeUnit.SECONDS);
        okBuilder.build();

        adapterBuilder = new Retrofit
                .Builder()
                .baseUrl(APIConstant.API_BASE_URL)
                .addConverterFactory(GsonCustomConverterFactory.create(gson));

    }

    public <S> S createService(Class<S> serviceClass) {
        return adapterBuilder
                .client(okBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(serviceClass);
    }
}

/**
 * This wrapper is to take care of this case:
 * when the deserialization fails due to JsonParseException and the
 * expected type is String, then just return the body string.
 */
class GsonResponseBodyConverterToString<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverterToString(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        String returned = value.string();
        try {
            return gson.fromJson(returned, type);
        } catch (JsonParseException e) {
            return (T) returned;
        }
    }
}

class GsonCustomConverterFactory extends Converter.Factory {
    public static GsonCustomConverterFactory create(Gson gson) {
        return new GsonCustomConverterFactory(gson);
    }

    private final Gson gson;
    private final GsonConverterFactory gsonConverterFactory;

    private GsonCustomConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
        this.gsonConverterFactory = GsonConverterFactory.create(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type.equals(String.class))
            return new GsonResponseBodyConverterToString<>(gson, type);
        else
            return gsonConverterFactory.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return gsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}


