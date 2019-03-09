package app.sunshine.android.example.com.drinkshopserver.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit=null;

    public static Retrofit getClient(String url) {
        if(retrofit==null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit=new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(url).build();

        }
        return retrofit;
    }
}
