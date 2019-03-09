package app.sunshine.android.example.com.drinkshopserver.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.sunshine.android.example.com.drinkshopserver.Models.Drink;
import app.sunshine.android.example.com.drinkshopserver.R;
import app.sunshine.android.example.com.drinkshopserver.UpdateDrinkActivity;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder>  implements View.OnClickListener {
    Context context; List<Drink> drinks;
    private static final String TAG = "DrinkAdapter";
    public DrinkAdapter(Context context, List<Drink> drinks) {
        this.context = context;
        this.drinks = drinks;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout=LayoutInflater.from(context).inflate(R.layout.drink_list_item,null,false);
        return new DrinkViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder drinkViewHolder, final int i) {

     drinkViewHolder.DrinkName.setText(drinks.get(i).getName());
     drinkViewHolder.DrinkPrice.setText(drinks.get(i).getPrice());
        Picasso.with(context).load(drinks.get(i).getLink()).into(drinkViewHolder.DrinkImage);
        drinkViewHolder.setItemClickListener(new itemClickListener() {
            @Override
            public void onClick(View view, boolean longPressed) {
                Common.CurrentDrink=drinks.get(i);
                //open update drink activity
                Log.d(TAG, "onClick: ");
                context.startActivity(new Intent(context,UpdateDrinkActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    @Override
    public void onClick(View view) {

    }
}
