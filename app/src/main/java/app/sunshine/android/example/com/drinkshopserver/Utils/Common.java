package app.sunshine.android.example.com.drinkshopserver.Utils;

import java.util.ArrayList;
import java.util.List;

import app.sunshine.android.example.com.drinkshopserver.Models.Category;
import app.sunshine.android.example.com.drinkshopserver.Models.Drink;
import app.sunshine.android.example.com.drinkshopserver.Models.Order;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.FCMRetrofitClient;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.IDrinkShopServer;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.IFCMServices;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.RetrofitClient;
//local host 10.0.2.2
//genymotion 10.0.3.2
//  http://localhost/drinkShop/
public class Common {
    public static final String Base_Url="http://10.0.2.2/drinkShop/";
     public static final String FCM_API="https://fcm.googleapis.com/";

    public static  Category CurrentCategory;
    public static Drink CurrentDrink;
    public static List<Category> categories;
    public static Order CurrentOrder;
    public static IDrinkShopServer getApi(){
        return RetrofitClient.getClient(Base_Url).create(IDrinkShopServer.class);
    }
    public static IFCMServices getFCMService(){
        return FCMRetrofitClient.getClient(FCM_API).create(IFCMServices.class);
    }


}
