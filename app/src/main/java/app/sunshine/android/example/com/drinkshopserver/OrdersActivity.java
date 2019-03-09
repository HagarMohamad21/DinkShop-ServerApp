package app.sunshine.android.example.com.drinkshopserver;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Collections;
import java.util.List;

import app.sunshine.android.example.com.drinkshopserver.Models.Order;
import app.sunshine.android.example.com.drinkshopserver.Retrofit.IDrinkShopServer;
import app.sunshine.android.example.com.drinkshopserver.Utils.Common;
import app.sunshine.android.example.com.drinkshopserver.Utils.OrdersAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrdersActivity extends AppCompatActivity {
    IDrinkShopServer mService;
    @BindView(R.id.ordersList)
    RecyclerView ordersList;
    @BindView(R.id.bottom_nav)
    BottomNavigationViewEx bottomNav;
String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);
        mService = Common.getApi();
        LoadOrders("0");
        bottomNav.setOnNavigationItemSelectedListener(new
                      BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.newIcon:
                                LoadOrders("0");
                                status="0";
                                return true;
                            case R.id.ProssingIcon:
                                LoadOrders("1");
                                status="1";
                                return true;
                            case R.id.ShippingIcon:
                                LoadOrders("2");
                                status="2";
                                return true;
                            case R.id.DeliveredIcon:
                                LoadOrders("3");
                                status="3";
                                return true;
                            case R.id.Cancelled:
                                LoadOrders("-1");
                                status="-1";
                                return true;

                        }
                        return true;
                    }
                });



    }

    private void LoadOrders(String OrderStatus) {
        mService.getOrders(OrderStatus)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer<List<Order>>() {
            @Override
            public void accept(List<Order> orders) throws Exception {
             displayOrders(orders);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
          }

    private void displayOrders(List<Order> orders) {
        Collections.reverse(orders);
        OrdersAdapter adapter=new OrdersAdapter(orders,this);
        ordersList.setLayoutManager(new GridLayoutManager(this,1));
        ordersList.setHasFixedSize(true);
        ordersList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        LoadOrders(status);
        super.onResume();

    }
}
