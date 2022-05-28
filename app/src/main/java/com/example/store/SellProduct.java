package com.example.store;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class SellProduct extends AppCompatActivity {
    AlertDialog dialog;
    TextView productName;
    TextView productQuantity;
    TextView productPrice;
    EditText Amt;
    Button btnAdd;
    String barcode="";
    String name ="";
    Intent i;
    ListView listView;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference ref =null;
    List<Product> listProduct = new ArrayList<>();
    CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);

        i =getIntent();
        name= i.getStringExtra("Name");
        ref= db.collection(name);

        listView = findViewById(R.id.products_list);

        adapter = new CustomAdapter(listProduct);

        listView.setAdapter(adapter);





    }

    public void addToCart(View view) {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();

        AlertDialog.Builder builder = new AlertDialog.Builder(this );
        View v = getLayoutInflater().inflate(R.layout.add_to_card, null);
        productName = v.findViewById(R.id.product_name);
        productQuantity = v.findViewById(R.id.amount_available);
        productPrice = v.findViewById(R.id.product_price);
        Amt = v.findViewById(R.id.require_amount);
        btnAdd = v.findViewById(R.id.Add_list);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList(barcode);
            }
        });

        builder.setView(v);
        dialog = builder.create();

        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                barcode=(intentResult.getContents());
                setProductName(barcode);

//                messageFormat.setText(intentResult.getFormatName());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

   public void setProductName(String bar)
    {
        ref.document(bar).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            Product product = documentSnapshot.toObject(Product.class);
            productName.setText(product.getName());
            productQuantity.setText(String.valueOf(product.getQuantity()));
            productPrice.setText(String.valueOf(product.getPrice()));
            }
        });
    }
    public void addToList(String bar)
    {
        ref.document(bar).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Toast.makeText(SellProduct.this, "Data Loaded", Toast.LENGTH_SHORT).show();
                    Product product = documentSnapshot.toObject(Product.class);
                    int quantity = Integer.parseInt(Amt.getText().toString());
//                    Product p1 = new Product(product.getName(), quantity, product.getPrice(), product.getCompany());
                    Product p1 = new Product(product.getName(),quantity, product.getPrice(), product.getCompany());
                    p1.setDocumentId(bar);
                    listProduct.add(p1);
                    adapter.notifyDataSetChanged();
                    dialog.cancel();

                }
            }
        });

    }


    public void ConfirmList(View view) {
        int Sum = 0;
        for (int i=0;i<listProduct.size();i++)
        {
            Product p = listProduct.get(i);
            Sum = Sum+p.getPrice()*p.getQuantity();
            ref.document(p.getDocumentId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                 if(documentSnapshot.exists())
                 {
                     Product product = documentSnapshot.toObject(Product.class);
                     if (product.getQuantity()>p.getQuantity())
                     {
                         int quantity = product.getQuantity()-p.getQuantity();
                         Product p2 = new Product(product.getName(),quantity,product.getPrice(),product.getCompany());
                         ref.document(p.getDocumentId()).set(p2, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 Toast.makeText(SellProduct.this, "Data Updated", Toast.LENGTH_SHORT).show();
                                 int k = p.getQuantity()*product.getPrice();
//
                             }
                         });

                     }
                 }
                }
            });
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(SellProduct.this );
        View v =getLayoutInflater().inflate(R.layout.confirm_dailog,null);

        builder.setView(v);
        TextView bill = v.findViewById(R.id.bill_text);
        Button ok = v.findViewById(R.id.ok_btn);
        AlertDialog dialog = builder.create();
        bill.setText("Your Total Bill is "+Sum);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellProduct.this, MainActivity.class);
                intent.putExtra("Name",name);
                startActivity(intent);
                finish();
            }
        });


    }
}