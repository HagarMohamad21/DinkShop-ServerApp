package app.sunshine.android.example.com.drinkshopserver.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.sunshine.android.example.com.drinkshopserver.R;

public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView DrinkImage;
    TextView DrinkPrice,DrinkName;

    public void setItemClickListener(itemClickListener itemClickListener) {
        ItemClickListener = itemClickListener;
    }

    itemClickListener ItemClickListener;
    public DrinkViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        DrinkImage=itemView.findViewById(R.id.drinkImage);
        DrinkPrice=itemView.findViewById(R.id.price);
        DrinkName=itemView.findViewById(R.id.name);

    }

    @Override
    public void onClick(View view) {
        ItemClickListener.onClick(view,false);
    }
}
