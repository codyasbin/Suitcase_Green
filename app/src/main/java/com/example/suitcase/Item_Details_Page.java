package com.example.suitcase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.suitcase.databinding.ActivityItemDetailsPageBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class Item_Details_Page extends AppCompatActivity {
    CircleImageView circleImageView;
    ActivityItemDetailsPageBinding binding;

    // Define variable
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";
    public static final String IS_PURCHASED = "purchased";
    public static final int EDIT_ITEM_REQUEST = 10001;

    ItemsModel item;
    DatabaseHelper items_dbHelper;

    // Get Intent
    public static Intent getIntent(Context context, int id){
        Intent intent = new Intent(context, Item_Details_Page.class);
        intent.putExtra(ID,id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityItemDetailsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        item = new ItemsModel();
        items_dbHelper = new DatabaseHelper(this);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt(Item_Details_Page.ID);
        Log.d("Items_Details_Page", id+"");

        item = retrieveData(id);
        binding.imageViewItem.setImageURI(item.getImage());
        binding.textViewName.setText(item.getName());
        binding.textViewPrice.setText(String.valueOf(item.getPrice()));
        binding.textViewDescription.setText(item.getDescription());

        // Click Method on Edit Button
        binding.buttonEditItem.setOnClickListener(v -> startEditItems(item));

        // Click Method of Share Button
        binding.buttonShareItem.setOnClickListener(v -> startShareItemActivity(item));

        circleImageView = findViewById(R.id.backBtn);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Item_Details_Page.this, MainActivity.class));
            }
        });
    }

    private void startEditItems(ItemsModel item){
        startActivity(EditItem.getIntent(getApplicationContext(), item));
    }

    private ItemsModel retrieveData(int id){
        Cursor cursor = items_dbHelper.getItemById(id);
        cursor.moveToNext();
        ItemsModel itemsModel = new ItemsModel();
        itemsModel.setId(cursor.getInt(0));
        itemsModel.setName(cursor.getString(1));
        itemsModel.setPrice(cursor.getDouble(2));
        itemsModel.setDescription(cursor.getString(3));
        itemsModel.setImage(Uri.EMPTY);

        try {
            Uri imageUri = Uri.parse(cursor.getString(4));
            itemsModel.setImage(imageUri);
        } catch (NullPointerException e) {
            Toast.makeText(this, "Error occurred in identifying image resource", Toast.LENGTH_SHORT).show();
        }

        itemsModel.setPurchased(cursor.getInt(5) == 1);
        Log.d("Items_Details_Page", itemsModel.toString());
        return itemsModel;
    }

    private void startShareItemActivity(ItemsModel item){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this item: " + item.getName());
        intent.putExtra(Intent.EXTRA_TEXT, "Item Name: " + item.getName() +
                "\nPrice: " + item.getPrice() +
                "\nDescription: " + item.getDescription());
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}
