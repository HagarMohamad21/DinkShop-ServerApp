package app.sunshine.android.example.com.drinkshopserver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import app.sunshine.android.example.com.drinkshopserver.Models.Category;
import app.sunshine.android.example.com.drinkshopserver.Models.Drink;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.IDrinkShopServer;
import app.sunshine.android.example.com.drinkshopserver.Utils.Common;
import app.sunshine.android.example.com.drinkshopserver.Utils.ProgressUpdate;
import app.sunshine.android.example.com.drinkshopserver.Utils.ResponseRequestBody;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDrinkActivity extends AppCompatActivity implements ProgressUpdate {

    @BindView(R.id.browseImage)
    ImageView browseImage;
    @BindView(R.id.DrinkName)
    EditText DrinkName;
    @BindView(R.id.DrinkPrice)
    EditText DrinkPrice;
    @BindView(R.id.updateBtn)
    Button updateBtn;
    @BindView(R.id.deleteBtn)
    Button deleteBtn;
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    CompositeDisposable disposable=new CompositeDisposable();
    IDrinkShopServer mService;
    public final int STORAGE_INTENT_CODE=1244;
    public String Drink_Image_Path ="";
    Uri selectedFile;
    private static final String TAG = "UpdateDrinkActivity";
    HashMap<String,String> menu_data_to_get_key=new HashMap<>();
    HashMap<String,String>menu_data_to_get_value=new HashMap<>();
    List<String>menu_data=new ArrayList<>();
    public String selected_category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_drink);
        ButterKnife.bind(this);
        mService=Common.getApi();
        setSpinnerMenu();

        if(Common.CurrentDrink!=null){
            Drink_Image_Path=Common.CurrentDrink.getLink();
            selected_category=Common.CurrentDrink.getMenuId();
            Picasso.with(this).load(Common.CurrentDrink.getLink()).into(browseImage);
            DrinkName.setText(Common.CurrentDrink.getName());
            DrinkPrice.setText(Common.CurrentDrink.getPrice());
            spinner.setSelectedIndex(menu_data.indexOf(menu_data_to_get_value.get(Common.CurrentCategory.getID())));

        }
        browseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open file chooser
                startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(),
                        "Select Image"),STORAGE_INTENT_CODE);
            }
        });
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selected_category=menu_data_to_get_key.get(menu_data.get(position));
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDrink();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDrink();
            }
        });
    }

    private void deleteDrink() {
        disposable.add(mService.deleteDrink(Common.CurrentDrink.getID()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(UpdateDrinkActivity.this, s, Toast.LENGTH_SHORT).show();
                       finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(UpdateDrinkActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }));
    }

    private void setSpinnerMenu() {
        for (Category category:Common.categories){
         menu_data_to_get_key.put(category.getName(),category.getID());
         menu_data_to_get_value.put(category.getID(),category.getName());
         menu_data.add(category.getName());
        }
        spinner.setItems(menu_data);

    }

    private void updateDrink() {
        if(!TextUtils.isEmpty(DrinkName.getText().toString())&&!TextUtils.isEmpty(DrinkPrice.getText().toString())){
            disposable.add(mService.UpdateDrink(DrinkName.getText().toString(),
                    Drink_Image_Path,selected_category,Common.CurrentDrink.getID(),DrinkPrice.getText().toString())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Toast.makeText(UpdateDrinkActivity.this, s, Toast.LENGTH_SHORT).show();
                            Drink_Image_Path="";
                            selectedFile=null;
                            finish();

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(UpdateDrinkActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                          finish();
                        }
                    }));
        }
        else{
            Toast.makeText(this, "Please provide both name and price for the product!",
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if(resultCode==RESULT_OK){
            Log.d(TAG, "onActivityResult: "+"result is okay");
            if(requestCode==STORAGE_INTENT_CODE){
                selectedFile=data.getData();
                if(!selectedFile.getPath().isEmpty()&&selectedFile!=null){
                    browseImage.setImageURI(selectedFile);
                    Log.d(TAG, "onActivityResult: "+selectedFile);
                    uploadImageToServer(selectedFile);

                }
            }
        }
    }

    private void uploadImageToServer(Uri selectedFile) {
        //upload file to server
        File file =FileUtils.getFile(this,selectedFile);
        String fileName=new StringBuilder(UUID.randomUUID().toString())
                .append(FileUtils.getExtension(file.toString())).toString();
        ResponseRequestBody responseRequestBody=new ResponseRequestBody(file,this);

        final MultipartBody.Part part=MultipartBody.Part.createFormData
                ("uploaded_file",fileName,responseRequestBody);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mService.uploadCategoryImage(part).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        // now get the image link
                        Drink_Image_Path =new StringBuilder(Common.Base_Url)
                                .append("server/category/category_images/")
                                .append(response.body())
                                .toString();
                        Log.d(TAG, "onResponse: "+ Drink_Image_Path);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(UpdateDrinkActivity.this,
                                "Failed to upload image to server", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: call is"+call.toString());
                        Log.d(TAG, "onFailure: "+t.getMessage());

                    }
                });
            }
        }).start();

    }
    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        disposable.clear();
        super.onStop();
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

}
