package app.sunshine.android.example.com.drinkshopserver.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.sunshine.android.example.com.drinkshopserver.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
    ImageView categoryImage;
    TextView categoryName;
 itemClickListener ItemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryImage=itemView.findViewById(R.id.item_image);
        categoryName=itemView.findViewById(R.id.item_name);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(itemClickListener ItemClickListener) {
        this.ItemClickListener = ItemClickListener;
    }

    @Override
    public void onClick(View view) {
    ItemClickListener.onClick(view,false);
    }

    @Override
    public boolean onLongClick(View view) {

       ItemClickListener.onClick(view,true);
       return true;
    }
}
