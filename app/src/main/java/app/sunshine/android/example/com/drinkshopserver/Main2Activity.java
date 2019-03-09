package app.sunshine.android.example.com.drinkshopserver;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import app.sunshine.android.example.com.drinkshopserver.Models.Category;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.IDrinkShopServer;
import app.sunshine.android.example.com.drinkshopserver.Utils.CategoryAdapter;
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

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProgressUpdate {
    private static final String TAG = "Main2Activity";
    CompositeDisposable disposable = new CompositeDisposable();
    IDrinkShopServer mService;
    @BindView(R.id.category_list)
    RecyclerView categoryList;
    ImageView BrowseImage;
    EditText CategoryNameEdit;
    String CategoryName;
 public final int STORAGE_PERMISSION_CODE=1234;
 public final int STORAGE_INTENT_CODE=1244;
 public String Category_Image_Path="";
    Uri selectedFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show add category dialog
                showAddCategoryDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mService = Common.getApi();
        updateFirebaseToken();
        getCategories();

        askForStoragePermission();
    }

    private void updateFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                mService.getToken
                        ("CaffinaShop",instanceIdResult.getToken(),"1")
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.d(TAG, "onResponse:from main activity "+response.body());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d(TAG, "onFailure:from main activity "+t.getStackTrace());
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Main2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showAddCategoryDialog() {

    View DialogLayout=LayoutInflater.from(this).inflate(R.layout.add_category_layout,null,false);
    BrowseImage=DialogLayout.findViewById(R.id.browseImage);
    CategoryNameEdit=DialogLayout.findViewById(R.id.CategoryName);
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
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
                if(TextUtils.isEmpty(CategoryNameEdit.getText())){
                    Toast.makeText(Main2Activity.this, "Please provide category name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Category_Image_Path.isEmpty()){
                    Toast.makeText(Main2Activity.this, "Please provide an image!", Toast.LENGTH_SHORT).show();
                    return; }

                disposable.add(mService.addNewCategory(CategoryNameEdit.getText().toString(),Category_Image_Path)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String result) throws Exception {
                                getCategories();
                                Category_Image_Path = "";
                                selectedFile = null;

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(Main2Activity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                getCategories();
                                Category_Image_Path = "";
                                selectedFile = null;
                            }
                        }));

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            Category_Image_Path="";
            selectedFile=null;
            }
        });
        dialog.show();
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
        ResponseRequestBody responseRequestBody=new ResponseRequestBody(file,this);

        final MultipartBody.Part part=MultipartBody.Part.createFormData("uploaded_file",fileName,responseRequestBody);
    new Thread(new Runnable() {
        @Override
        public void run() {
         mService.uploadCategoryImage(part).enqueue(new Callback<String>() {
             @Override
             public void onResponse(Call<String> call, Response<String> response) {
           // now get the image link
                 Category_Image_Path=new StringBuilder(Common.Base_Url)
                         .append("server/category/category_images/")
                         .append(response.body())
                         .toString();
                 Log.d(TAG, "onResponse: "+Category_Image_Path);
             }

             @Override
             public void onFailure(Call<String> call, Throwable t) {
                 Toast.makeText(Main2Activity.this, "Failed to upload image to server", Toast.LENGTH_SHORT).show();
                 Log.d(TAG, "onFailure: call is"+call.toString());
                 Log.d(TAG, "onFailure: "+t.getMessage());

             }
         });
        }
    }).start();

    }

    private void askForStoragePermission() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED){

            //ask for permission
            ActivityCompat.requestPermissions(this,new String[]{
                     Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,Manifest.permission.READ_EXTERNAL_STORAGE
            },STORAGE_PERMISSION_CODE);
        }
    }

    private void getCategories() {
        disposable.add(mService.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {
                        showCategories(categories);
                    }
                }));

    }

    private void showCategories(List<Category> categories) {
        Common.categories=categories;
     categoryList.setLayoutManager(new GridLayoutManager(this,2));
     categoryList.setHasFixedSize(true);
        CategoryAdapter adapter=new CategoryAdapter(categories,this);
     categoryList.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Orders) {
            // Handle the camera action
            //open order activity
            startActivity(new Intent(this,OrdersActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_PERMISSION_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG);
            }
        }
    }


    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    protected void onResume() {
        getCategories();
        super.onResume();

    }
}
