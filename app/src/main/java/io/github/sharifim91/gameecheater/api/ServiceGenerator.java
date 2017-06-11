package io.github.sharifim91.gameecheater.api;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User1 on 10/3/2016.
 */

public final class ServiceGenerator {
    private static final String TAG = ServiceGenerator.class.getSimpleName();
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final Gson gson = new GsonBuilder()
            .setDateFormat(DATE_FORMAT)
            .create();
    private static final Retrofit.Builder retrofit = new Retrofit.Builder()
            .baseUrl("https://bots.gameeapp.com")
            .addConverterFactory(buildConverterFactory());

    private static Converter.Factory buildConverterFactory() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat(DATE_FORMAT);

        return GsonConverterFactory.create(gsonBuilder.create());
    }

    /**
     * Create service for using in other class
     * Accept-Encoding: gzip
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass) {
        final OkHttpClient build = new OkHttpClient
                .Builder()
                .readTimeout(200, TimeUnit.SECONDS)
                .addInterceptor(new LoggerInterceptor())
                .build();

        return retrofit.client(build).build().create(serviceClass);
    }


    /**
     * Just for calculate nano time of request sending and response receiving
     * and implementation of {@link Interceptor}
     */
    private static class LoggerInterceptor implements Interceptor {

        public LoggerInterceptor() {
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final long t1 = System.nanoTime();
            final Request original = chain.request();
            final Request.Builder requestBuilder = original.newBuilder();
            final Request request = requestBuilder
                    .url(original.url())
                    .header("Accept-Language", "fa")
//                    .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
//                    .header("host", "bots.gameeapp.com")
                    .method(original.method(), original.body()).build();

            Log.d(TAG, String.format("Sending Request %s on %s%n%s ",
                    request.url(), chain.connection(), request.headers()));
            final Response response = chain.proceed(requestBuilder.build());
            long t2 = System.nanoTime();
            Log.d(TAG, String.format("Received Response for %s in %.1fms %n %s",
                    response.request().url(), (t2 - t1) / 1e6d, response.body()
            ));
            return response;
        }
    }

}
