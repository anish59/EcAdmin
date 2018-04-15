package ec.com.ecadmin;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import ec.com.ecadmin.helper.AppConstants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anish on 29-08-2017.
 */

public class AppApplication extends Application {
    private static Retrofit retrofit;
    private static Gson gson;
    private static AppApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initRetroFit();
        initGson();
    }


    private void initRetroFit() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BaseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    private void initGson() {
        gson = new GsonBuilder()
                .setLenient()
                .create();
    }

    public static Gson getGson() {
        return gson;
    }
}