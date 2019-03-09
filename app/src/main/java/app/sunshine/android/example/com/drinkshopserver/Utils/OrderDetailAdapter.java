package app.sunshine.android.example.com.drinkshopserver.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.sunshine.android.example.com.drinkshopserver.Models.Cart;
import app.sunshine.android.example.com.drinkshopserver.Models.Order;
import app.sunshine.android.example.com.drinkshopserver.R;


public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    Context context;
    List<Cart> cartList;
    private static final String TAG = "OrderDetailAdapter";

    public OrderDetailAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
        Log.d(TAG, "OrderDetailAdapter: ");
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Layout=LayoutInflater.from(context).inflate(R.layout.order_detail_list_item,null,false);
        return new OrderDetailViewHolder(Layout);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder orderDetailViewHolder, int i) {
     orderDetailViewHolder.dringIce.setText(cartList.get(i).Ice==50?"Half":cartList.get(i).Ice==0?"Free":
             cartList.get(i).Ice==100?"Full":"Medium");
     orderDetailViewHolder.drinkName.setText(cartList.get(i).name);
     orderDetailViewHolder.drinkPrice.setText(String.valueOf(cartList.get(i).Price));
     orderDetailViewHolder.drinkSize.setText(cartList.get(i).size==0?"Medium":"Large");
     orderDetailViewHolder.drinkQuantity.setText(String.valueOf(cartList.get(i).Quantity));
     orderDetailViewHolder.drinkSugar.setText(cartList.get(i).Sugar==50?"Half":cartList.get(i).Sugar==0?"Free":
             cartList.get(i).Sugar==100?"Full":"Medium");
     orderDetailViewHolder.drinkToppings.setText(cartList.get(i).Toppings);
     Picasso.with(context).load(cartList.get(i).Link).into(orderDetailViewHolder.drinkImage);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+cartList.size());
        return cartList.size();
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView drinkImage;
        TextView drinkName,drinkPrice,drinkSize,drinkSugar,dringIce,drinkQuantity,drinkToppings;
        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkImage  =itemView.findViewById(R.id.drinkImage);
            drinkName =itemView.findViewById(R.id.drinkName);
            drinkPrice =itemView.findViewById(R.id.drinkPrice);
            drinkSize =itemView.findViewById(R.id.drinkSize);
            drinkSugar=itemView.findViewById(R.id.drinkSugar);
            dringIce=itemView.findViewById(R.id.drinkIce);
            drinkQuantity=itemView.findViewById(R.id.drinkQuantity);
            drinkToppings=itemView.findViewById(R.id.drinkToppings);

        }
    }

}
