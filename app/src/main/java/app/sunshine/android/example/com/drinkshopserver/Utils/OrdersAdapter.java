package app.sunshine.android.example.com.drinkshopserver.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import app.sunshine.android.example.com.drinkshopserver.Models.Cart;
import app.sunshine.android.example.com.drinkshopserver.Models.Order;
import app.sunshine.android.example.com.drinkshopserver.R;
import app.sunshine.android.example.com.drinkshopserver.UpdateOrderActivity;

public class OrdersAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    List<Order>orders;
    Context context;

    private static final String TAG = "OrderAdapter";
    private List<Cart> cartList;

    public OrdersAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Layout=LayoutInflater.from(context).inflate(R.layout.order_list_item,
                viewGroup,false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new OrderViewHolder(Layout);

    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, final int i) {
        setCartList(orders.get(i));
        orderViewHolder.OrderNumber.setText("# "+String.valueOf(orders.get(i).getOrderID()));
        orderViewHolder.OrderPrice.setText("$ "+String.valueOf(orders.get(i).getOrderPrice()));
        orderViewHolder.OrderStatus.setText(orders.get(i).getOrderText());
        orderViewHolder.OrderComment.setText(orders.get(i).getOrderComment());

        orderViewHolder.UserAddress.setText(orders.get(i).getOrderAddress());
        orderViewHolder.ToggleExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderViewHolder.ToggleExpand.setRotation(180.0f);
                orderViewHolder.ExpandableLayout.toggle(400,new AccelerateDecelerateInterpolator());

            }
        });
  orderViewHolder.callButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          // open dialer
          Intent intent = new Intent(Intent.ACTION_DIAL);
          intent.setData(Uri.parse("tel:"+orders.get(i).getUserPhone()));
          context.startActivity(intent);
      }
  });
  //update order status button
        orderViewHolder.UpdateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open update order activity
                Intent intent=new Intent(context, UpdateOrderActivity.class);
                intent.putExtra("CurrentOrder",orders.get(i));
                context.startActivity(intent);


            }
        });
        // populate the recycler view
        orderViewHolder.orderDetailList.setLayoutManager
                (new GridLayoutManager(context,1));
        OrderDetailAdapter adapter=new OrderDetailAdapter(context,cartList);
        orderViewHolder.orderDetailList.setAdapter(adapter);
}
    @Override
    public int getItemCount() {
        return orders.size();
    }
    public void setCartList(Order CurrentOrder){
        Common.CurrentOrder=CurrentOrder;
        cartList=new Gson().fromJson(CurrentOrder.getOrderDetail(),new TypeToken<List<Cart>>(){}.getType());
        Log.d(TAG, "setCartList: "+cartList.size());
    }
}
