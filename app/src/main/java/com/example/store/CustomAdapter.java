package com.example.store;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

   List <Product> data;



    public CustomAdapter(List<Product> data) {
        this.data = data;
    }



    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items,viewGroup,false);

        TextView name = view.findViewById(R.id.name);
        TextView quantity = view.findViewById(R.id.quantity);
        TextView price = view.findViewById(R.id.price);
//        Button deleteBtn = view.findViewById(R.id.item_button);
        ImageView UpdateBtn = view.findViewById(R.id.update);
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                data.remove(position);
//                notifyDataSetChanged();
//            }
//        });

        Product product = data.get(position);

        name.setText(product.getName());
        quantity.setText(String.valueOf(product.getQuantity()));
        price.setText(String.valueOf(product.getPrice()));


        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext() );
        View v =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.updatedialog_for_sale,viewGroup,false);

        builder.setView(v);

        AlertDialog dialog = builder.create();
        dialog.setTitle("Update Product");

        // Getting Id for Compenents in Alert Dialog From View

       TextView etName = v.findViewById(R.id.edit_name);
       TextView etPrice = v.findViewById(R.id.edit_price);
       EditText etQuantity =v.findViewById(R.id.edit_quantity);
       Button Addbtn =v.findViewById(R.id.update_btn);



       UpdateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               etName.setText(product.getName());
               etPrice.setText(String.valueOf(product.getPrice()));
               etQuantity.setText(String.valueOf(product.getQuantity()));
               dialog.show();



           }
       });

       Addbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               name.setText(etName.getText().toString());
               quantity.setText(etQuantity.getText().toString());
               price.setText(etPrice.getText().toString());

               product.setName(etName.getText().toString());
               product.setPrice(Integer.parseInt(etPrice.getText().toString()));
               product.setQuantity(Integer.parseInt(etQuantity.getText().toString()));

               dialog.cancel();

               notifyDataSetChanged();

           }
       });


        return view;
    }


    }

