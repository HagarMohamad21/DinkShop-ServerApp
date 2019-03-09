package app.sunshine.android.example.com.drinkshopserver.Models;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Order implements Serializable {
    private long OrderID;
    private int OrderStatus;
    private float OrderPrice;
    private String OrderDetail,OrderComment,OrderAddress,UserPhone;

    public Order() {
    }

    public long getOrderID() {
        return OrderID;
    }

    public void setOrderID(long orderID) {
        OrderID = orderID;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }
    public String getOrderText(){

        int OrderNumber=getOrderStatus();
        switch (OrderNumber)
        { case 0:
            return "Placed";

            case 1:
                return "Processing" ;
            case 2:
                return "Shipping";

            case 3:
                return "Delivered";

            case -1:
                return "Cancelled";

            default:
                return "Order Error";
        }
    }


    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public float getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getOrderDetail() {
        return OrderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        OrderDetail = orderDetail;
    }

    public String getOrderComment() {
        return OrderComment;
    }

    public void setOrderComment(String orderComment) {
        OrderComment = orderComment;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        OrderAddress = orderAddress;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }


}
