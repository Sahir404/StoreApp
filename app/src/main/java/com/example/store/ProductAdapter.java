package com.example.store;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

public class ProductAdapter extends FirestoreRecyclerAdapter <Product, ProductAdapter.ProductHolder>{


    private OnItemClickListener listener;
    Context c;
    public ProductAdapter(Context c,@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
        this.c = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, Product model) {
        holder.etName.setText(model.getName());
        holder.etQuantity.setText(String.valueOf(model.getQuantity()));
        holder.etPrice.setText(String.valueOf(model.getPrice()));
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(model,position);
            }
        });
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        return new  ProductHolder(v);
    }

    public class ProductHolder extends RecyclerView.ViewHolder  {

        TextView etName;
        TextView etQuantity;
        TextView etPrice;
        ImageView update;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.name);
            etQuantity = itemView.findViewById(R.id.quantity);
            etPrice = itemView.findViewById(R.id.price);
            update = itemView.findViewById(R.id.update);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    void showDialog(Product model,int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        View view = LayoutInflater.from(c).inflate(R.layout.updatedialog,null);
       builder.setView(view);
        TextView etName = view.findViewById(R.id.edit_name);
        EditText etQuantity = view.findViewById(R.id.edit_quantity);
        EditText etPrice = view.findViewById(R.id.edit_price);
        Button update = view.findViewById(R.id.update_btn);

        etName.setText(model.getName());
        etQuantity.setText(String.valueOf(model.getQuantity()));
        etPrice.setText(String.valueOf(model.getPrice()));

       AlertDialog dialog =  builder.create();
       dialog.show();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                int quantity = Integer.parseInt(etQuantity.getText().toString());
                int price =Integer.parseInt( etPrice.getText().toString());

                Product product = new Product(name,quantity,price);
                getSnapshots().getSnapshot(position).getReference().set(product, SetOptions.merge());
                dialog.cancel();
                notifyDataSetChanged();
            }
        });




    }

    void fetch(){

//        showDialog();
    }

}
