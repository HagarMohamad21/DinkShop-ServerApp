package app.sunshine.android.example.com.drinkshopserver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.UUID;

import app.sunshine.android.example.com.drinkshopserver.Models.Drink;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.IDrinkShopServer;
import app.sunshine.android.example.com.drinkshopserver.Utils.Common;
import app.sunshine.android.example.com.drinkshopserver.Utils.DrinkAdapter;
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

public class DrinkListActivity extends AppCompatActivity implements ProgressUpdate {
    private static final String TAG = "DrinkListActivity";
    @BindView(R.id.drinkList)
    RecyclerView drinkList;
    CompositeDisposable disposable = new CompositeDisposable();
    IDrinkShopServer mService;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    EditText DrinkNameEdit,DrinkPriceEdit;
    ImageView BrowseImage;
    public final int STORAGE_INTENT_CODE=1244;
    public String Drink_Image_Path ="";
    Uri selectedFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_list);
        ButterKnife.bind(this);
        drinkList.setHasFixedSize(true);
        drinkList.setLayoutManager(new GridLayoutManager(this, 2));
        mService = Common.getApi();
        getDrinks();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAddDrinkDialog();
            }
        });
    }

    private void viewAddDrinkDialog() {
        View DialogLayout=LayoutInflater.from(this).inflate(R.layout.add_drink_dialog,null,false);
        BrowseImage=DialogLayout.findViewById(R.id.browseImage);
       DrinkNameEdit=DialogLayout.findViewById(R.id.DrinkName);
       DrinkPriceEdit=DialogLayout.findViewById(R.id.DrinkPrice);
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Add new product");
        dialog.setView(DialogLayout);

        BrowseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(),
                        "Select Image"),STORAGE_INTENT_CODE);
            }
        });
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(DrinkNameEdit.getText())){
                    Toast.makeText(DrinkListActivity.this, "Please provide drink name!", Toast.LENGTH_SHORT).show();
                    return;
                } if(TextUtils.isEmpty(DrinkPriceEdit.getText())){
                    Toast.makeText(DrinkListActivity.this, "Please provide drink price!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Drink_Image_Path.isEmpty()){
                    Toast.makeText(DrinkListActivity.this, "Please provide an image!", Toast.LENGTH_SHORT).show();
                    return; }

             disposable.add(mService.addNewDrink(DrinkNameEdit.getText().toString()
                     ,Drink_Image_Path,Common.CurrentCategory.getID(),DrinkPriceEdit.getText().toString())
                     .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).
                             subscribe(new Consumer<String>() {
                                 @Override
                                 public void accept(String s) throws Exception {
                                     Log.d(TAG, "accept: " + s);
                                     getDrinks();
                                     Drink_Image_Path = "";
                                     selectedFile = null;
                                     Toast.makeText(DrinkListActivity.this, s, Toast.LENGTH_SHORT).show();
                                 }
                             }, new Consumer<Throwable>() {
                                 @Override
                                 public void accept(Throwable throwable) throws Exception {
                                     Toast.makeText(DrinkListActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                 }
                             }));
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }


    private void getDrinks() {
        disposable.add(mService.getDrinks(Common.CurrentCategory.getID())
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        displayDrinks(drinks);
                    }
                }));
    }


    public void displayDrinks(List<Drink> drinks) {
        DrinkAdapter adapter = new DrinkAdapter(this, drinks);
        drinkList.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        disposable.clear();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        getDrinks();
        super.onResume();
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
                    BrowseImage.setImageURI(selectedFile);
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
        ResponseRequestBody responseRequestBody
                =new ResponseRequestBody(file,this);

        final MultipartBody.Part part=MultipartBody.Part.createFormData("uploaded_file",fileName,responseRequestBody);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mService.uploadDrinkImage(part).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        // now get the image link
                        Drink_Image_Path =new StringBuilder(Common.Base_Url)
                                .append("server/product/drink_images/")
                                .append(response.body())
                                .toString();
                        Log.d(TAG, "onResponse: "+ Drink_Image_Path);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(DrinkListActivity.this, "Failed to upload image to server", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: call is"+call.toString());
                        Log.d(TAG, "onFailure: "+t.getMessage());

                    }
                });
            }
        }).start();

    }

    @Override
    public void onProgressUpdate(int percentage) {

    }
}
