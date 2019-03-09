package app.sunshine.android.example.com.drinkshopserver.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;



@Entity(tableName = "Cart")
    public class Cart {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        public int id;
        @ColumnInfo(name = "name")
        public String name;
        @ColumnInfo(name = "size")
        public int size;
        @ColumnInfo(name = "link")
        public String Link;
        @ColumnInfo(name = "sugar")
        public   int Sugar;
        @ColumnInfo(name = "ice")
        public  int Ice;
        @ColumnInfo(name = "price")
        public   double Price;
        @ColumnInfo(name = "quantity")
        public int Quantity;
        @ColumnInfo(name = "toppings")
        public  String Toppings;
}
