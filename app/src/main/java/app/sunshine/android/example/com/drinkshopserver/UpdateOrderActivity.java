package app.sunshine.android.example.com.drinkshopserver;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import app.sunshine.android.example.com.drinkshopserver.Models.DataMessage;
import app.sunshine.android.example.com.drinkshopserver.Models.MyResponse;
import app.sunshine.android.example.com.drinkshopserver.Models.Order;
import app.sunshine.android.example.com.drinkshopserver.Models.Token;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.IDrinkShopServer;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.IFCMServices;
import app.sunshine.android.example.com.drinkshopserver.Utils.Common;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateOrderActivity extends AppCompatActivity {
    private static final String TAG = "UpdateOrderActivity";
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.saveChanges)
    ImageView saveChanges;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.OrderNumber)
    TextView OrderNumber;
    @BindView(R.id.OrderPrice)
    TextView OrderPrice;
    @BindView(R.id.OrderComment)
    TextView OrderComment;
    @BindView(R.id.UserAddress)
    TextView UserAddress;

    ImageView SaveChanges;
    Order CurrentOrder;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
IDrinkShopServer mService;
IFCMServices mIFCMServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order);
        ButterKnife.bind(this);
        SaveChanges = findViewById(R.id.saveChanges);
        CurrentOrder = (Order) getIntent().getSerializableExtra("CurrentOrder");
        setupWidgets();
        mService= Common.getApi();
        mIFCMServices=Common.getFCMService();

 
    SaveChanges.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           int i= radioGroup.getCheckedRadioButtonId();
            Log.d(TAG, "onClick: "+i);
            //change order status in database
            switch (i){
                case R.id.processing:
                    UpdateOrder("1");
                    break;
                case R.id.Shipping:
                   UpdateOrder("2");
                    break;
                case R.id.Delivered:
                    UpdateOrder("3");
                    break;
                case R.id.Cancelled:
                    UpdateOrder("-1");
                    break;


            }

        }
    });}

    private void setupWidgets() {
        if (CurrentOrder != null) {
            OrderNumber.setText(String.valueOf(CurrentOrder.getOrderID()));
            OrderComment.setText(CurrentOrder.getOrderComment());
            OrderPrice.setText(String.valueOf(CurrentOrder.getOrderPrice()));
            UserAddress.setText(CurrentOrder.getOrderAddress());}

    }
private void UpdateOrder(final String status){
   mService.CancelOrder
            (CurrentOrder.getUserPhone(),String.valueOf(CurrentOrder.getOrderID()), status).enqueue(new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {

            //send notification
            SendNotification(Common.CurrentOrder,status);

        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {

        }
    });
}

    private void SendNotification(final Order CurrentOrder, String status) {
        mService.getTokenFromServer(CurrentOrder.getUserPhone(),"0").enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.d(TAG, "onResponse: token is "+response.body().getToken());
                Token userToken=response.body();
                DataMessage dataMessage=new DataMessage();
                dataMessage.to=userToken.getToken();
                HashMap<String,String> dataSend=new HashMap<>();
                dataSend.put("title","YOUR ORDER HAS BEEN UPDATED ");
                dataSend.put("message","Order number : "+CurrentOrder.getOrderID());
                dataMessage.setData(dataSend);
                mIFCMServices.sendNotification(dataMessage).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if(response.body().success==1){
                            Log.d(TAG, "onResponse: "+response.code());
                            Toast.makeText(UpdateOrderActivity.this, "Order has been updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
        Toast.makeText(UpdateOrderActivity.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Toast.makeText(UpdateOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(UpdateOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
