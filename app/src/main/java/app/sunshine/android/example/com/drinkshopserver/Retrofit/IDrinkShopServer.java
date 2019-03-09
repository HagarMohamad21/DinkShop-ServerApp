package app.sunshine.android.example.com.drinkshopserver.Retrofit;


import java.util.List;
import app.sunshine.android.example.com.drinkshopserver.Models.Category;
import app.sunshine.android.example.com.drinkshopserver.Models.Drink;
import app.sunshine.android.example.com.drinkshopserver.Models.Order;
import app.sunshine.android.example.com.drinkshopserver.Models.Token;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface IDrinkShopServer {

@GET("getMenu.php")
Observable<List<Category>> getCategories();
@Multipart
@POST("server/category/uploadCategoryImage.php")
 Call<String> uploadCategoryImage( @Part MultipartBody.Part file);
@FormUrlEncoded
@POST("server/category/addCategory.php")
Observable<String> addNewCategory(@Field("name")String name,@Field("imagePath")String imagePath);

@FormUrlEncoded
@POST("server/category/updateCategory.php")
Observable<String> updateCategory(@Field("id")String id,@Field("name")String name,@Field("imagePath")String imagePath);

@FormUrlEncoded
@POST("server/category/deleteCategory.php")
Observable<String> deleteCategory(@Field("id")String id);
 @FormUrlEncoded
@POST("getDrink.php")
 Observable<List<Drink>>getDrinks(@Field("menuId") String menuId);

    @Multipart
    @POST("server/product/uploadDrinkImage.php")
    Call<String> uploadDrinkImage( @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("server/product/addDrink.php")
    Observable<String> addNewDrink(@Field("name")String name,
                                   @Field("imagePath")String imagePath ,
                                   @Field("menuId")String menuId,
                                   @Field("price")String price);

    @FormUrlEncoded
    @POST("server/product/updateDrink.php")
    Observable<String> UpdateDrink(@Field("name")String name,
                                   @Field("imagePath")String imagePath ,
                                   @Field("menuId")String menuId,
                                   @Field("id")String id,
                                   @Field("price")String price);


    @FormUrlEncoded
    @POST("server/product/deleteDrink.php")
    Observable<String> deleteDrink(@Field("id")String id);

    @FormUrlEncoded
    @POST("getOrdersByStatus.php")
    io.reactivex.Observable<List<Order>>
    getOrders(@Field("order_status")String orderStaus);


    @FormUrlEncoded
    @POST("getToken.php")
    Call<String>
    getToken(@Field("user_phone")String phone,
             @Field("token")String token,
             @Field("is_server_token")String isServerToken);

 @FormUrlEncoded
    @POST("CancelOrder.php")
    Call<String>
    CancelOrder(@Field("user_phone")String phone,
             @Field("order_id")String order_id,
             @Field("order_status")String order_status);

@FormUrlEncoded
   @POST("getTokenFromServer.php")
   Call<Token> getTokenFromServer(@Field("user_phone") String phone,  @Field("is_server_token")String isServerToken);
}
