package mobi.huibao.notebook.api.net;

import com.ayvytr.okhttploginterceptor.LoggingInterceptor;
import com.ayvytr.okhttploginterceptor.LoggingLevel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient<T> {

    private static Retrofit retrofit;

    static {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        LoggingInterceptor interceptor = new LoggingInterceptor(LoggingLevel.ALL);
        OkHttpClient client = new OkHttpClient.Builder().
                addInterceptor(interceptor).
                connectTimeout(3, TimeUnit.SECONDS).
                readTimeout(5, TimeUnit.SECONDS).
                build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://192.168.11.105:7988")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    public T  getApi(Class<T> cls) {
        return retrofit.create(cls);
    }
}
