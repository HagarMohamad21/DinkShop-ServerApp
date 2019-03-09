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

import app.sunshine.android.example.com.drinkshopserver.DrinkListActivity;
import app.sunshine.android.example.com.drinkshopserver.Models.Category;
import app.sunshine.android.example.com.drinkshopserver.R;
import app.sunshine.android.example.com.drinkshopserver.UpdateCategoryActivity;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    List<Category>categories;
    Context context;

    public CategoryAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    private static final String TAG = "CategoryAdapter";
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout=LayoutInflater.from(context).inflate(R.layout.category_list_item,null,false);
       return new CategoryViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, final int i) {
      categoryViewHolder.categoryName.setText(categories.get(i).getName());
        Picasso.with(context).load(categories.get(i).getLink()).into(categoryViewHolder.categoryImage);
        categoryViewHolder.setItemClickListener(new itemClickListener() {

            @Override
            public void onClick(View view, boolean longPressed) {
                if(longPressed){
                    //assign current category to position
                    Common.CurrentCategory=categories.get(i);
                    Log.d(TAG, "onClick:long ");
                    // start new activity
                    context.startActivity(new Intent(context,UpdateCategoryActivity.class));
                }
                else {
                    Common.CurrentCategory=categories.get(i);
                    context.startActivity(new Intent(context,DrinkListActivity.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
