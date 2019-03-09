package app.sunshine.android.example.com.drinkshopserver.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import org.w3c.dom.Text;

import app.sunshine.android.example.com.drinkshopserver.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {
   TextView UserAddress;
    TextView OrderNumber,OrderStatus,OrderPrice,OrderComment,UserPhone;
    ImageView ToggleExpand;
    ExpandableLinearLayout ExpandableLayout;
    RecyclerView orderDetailList;
    Button callButton,UpdateOrder;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        OrderNumber=itemView.findViewById(R.id.OrderNumber);
        OrderStatus=itemView.findViewById(R.id.OrderStaus);
        OrderPrice=itemView.findViewById(R.id.OrderPrice);
        OrderComment=itemView.findViewById(R.id.OrderComment);
        ToggleExpand=itemView.findViewById(R.id.toggleExpandBtn);
        ExpandableLayout=itemView.findViewById(R.id.expandableLayout);
        orderDetailList=itemView.findViewById(R.id.orderDetailsList);
        UserAddress=itemView.findViewById(R.id.UserAddress);
        callButton=itemView.findViewById(R.id.CallButton);
        UpdateOrder=itemView.findViewById(R.id.UpdateOrderButton);


    }
}
