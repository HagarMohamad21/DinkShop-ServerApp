package app.sunshine.android.example.com.drinkshopserver.Retrofit;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FCMRetrofitClient {
    public static Retrofit retrofit=null;
    public static Retrofit getClient(String url){
        if(retrofit==null)
            retrofit=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    return retrofit;}

}
