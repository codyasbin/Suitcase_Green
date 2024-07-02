package com.example.suitcase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.suitcase.ItemsModel;
import com.example.suitcase.MapActivity;
import com.example.suitcase.Item_Details_Page;
import com.example.suitcase.R;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private ArrayList<ItemsModel> itemsModels;
    private Context context;

    public ItemsAdapter(Context context, ArrayList<ItemsModel> itemsModels) {
        this.context = context;
        this.itemsModels = itemsModels;
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder holder, int position) {
        ItemsModel itemsModel = itemsModels.get(position);
        holder.bind(itemsModel);
    }

    @Override
    public int getItemCount() {
        return itemsModels.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView txt_name, txt_price, txt_description, txt_purchased;
        ImageView mapIcon;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            txt_name = itemView.findViewById(R.id.item_name);
            txt_price = itemView.findViewById(R.id.item_price);
            txt_description = itemView.findViewById(R.id.item_description);
            txt_purchased = itemView.findViewById(R.id.item_purchased);
            mapIcon = itemView.findViewById(R.id.map_icon);
            itemView.setOnClickListener(this);
            mapIcon.setOnClickListener(this);
        }

        public void bind(ItemsModel itemsModel) {
            txt_name.setText(itemsModel.getName());
            txt_price.setText(String.valueOf(itemsModel.getPrice()));
            txt_description.setText(itemsModel.getDescription());
            Uri imageUri = itemsModel.getImage();
            if (imageUri != null) {
                imageView.setImageURI(imageUri);
            }
            if (itemsModel.isPurchased()) {
                txt_purchased.setVisibility(View.VISIBLE);
            } else {
                txt_purchased.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ItemsModel itemsModel = itemsModels.get(position);
            if (view.getId() == R.id.map_icon) {
                // Handle click on map icon
                Intent intent = new Intent(context, MapActivity.class);
                context.startActivity(intent);
            } else {
                // Handle click on item view to open item description
                Intent intent = new Intent(context, Item_Details_Page.class);
                intent.putExtra(Item_Details_Page.ID, itemsModel.getId()); // Pass item ID to details page
                context.startActivity(intent);
            }
        }
    }
}
