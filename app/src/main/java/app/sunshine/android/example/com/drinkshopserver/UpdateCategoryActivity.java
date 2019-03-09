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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.UUID;

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

public class UpdateCategoryActivity extends AppCompatActivity implements ProgressUpdate {

    @BindView(R.id.browseImage)
    ImageView browseImage;
    @BindView(R.id.CategoryName)
    EditText CategoryName;
    @BindView(R.id.updateBtn)
    Button updateBtn;
    @BindView(R.id.deleteBtn)
    Button deleteBtn;
    Uri selectedFile=null;
    String ImgPath="";
    IDrinkShopServer mService;
    CompositeDisposable disposable=new CompositeDisposable();
    private static final String TAG = "UpdateCategoryActivity";
    public final int STORAGE_INTENT_CODE=1444;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);
        ButterKnife.bind(this);
        mService=Common.getApi();
        displayData();
        browseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(),
                        "Select Image"),STORAGE_INTENT_CODE);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCategory();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disposable.add(mService.deleteCategory(Common.CurrentCategory.getID())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(UpdateCategoryActivity.this,s,Toast.LENGTH_SHORT).show();
                                selectedFile=null;
                                ImgPath="";
                                Common.CurrentCategory=null;
                                finish();
                            }
                        }));
            }
        });
    }

    private void updateCategory() {
        if(TextUtils.isEmpty(CategoryName.getText().toString())){
            disposable.add(mService.updateCategory(Common.CurrentCategory.getID()
                ,CategoryName.getText().toString(),ImgPath)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(UpdateCategoryActivity.this,s,Toast.LENGTH_SHORT).show();
                        selectedFile=null;
                        ImgPath="";
                        Common.CurrentCategory=null;
                        finish();
                    }
                }));}
       else {
            Toast.makeText(this, "Please provide a category name!", Toast.LENGTH_SHORT).show();
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


    //upload image to server
    private void uploadImageToServer(Uri selectedFile) {
        //upload file to server
        File file =FileUtils.getFile(this,selectedFile);
        String fileName=new StringBuilder(UUID.randomUUID().toString())
                .append(FileUtils.getExtension(file.toString())).toString();
        ResponseRequestBody responseRequestBody=new ResponseRequestBody(file,this);

        final MultipartBody.Part part=MultipartBody.Part.createFormData("uploaded_file",fileName,responseRequestBody);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mService.uploadCategoryImage(part).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        // now get the image link
                        ImgPath=new StringBuilder(Common.Base_Url)
                                .append("server/category/category_images/")
                                .append(response.body())
                                .toString();
                        Log.d(TAG, "onResponse: "+ImgPath);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(UpdateCategoryActivity.this, "Failed to upload image to server", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: call is"+call.toString());
                        Log.d(TAG, "onFailure: "+t.getMessage());

                    }
                });
            }
        }).start();

    }


    private void displayData() {
        if(Common.CurrentCategory!=null){
            Picasso.with(this).load(Common.CurrentCategory.getLink()).into(browseImage);
            CategoryName.setText(Common.CurrentCategory.getName());
            ImgPath=Common.CurrentCategory.getLink();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();

    }

    @Override
    protected void onPause() {
        super.onPause();
        disposable.clear();
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }
}
