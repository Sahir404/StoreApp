package com.example.store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class AddProduct extends AppCompatActivity {
    EditText barcode;
    EditText pName;
    EditText pCompany;
    EditText pPrice;
    EditText pQuantity;
    Button pAdd;
    String Name ="";
    Intent i;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        barcode =findViewById(R.id.barcode);
        pName = findViewById(R.id.add_name);
        pCompany = findViewById(R.id.add_company);
        pPrice = findViewById(R.id.add_price);
        pQuantity = findViewById(R.id.add_quantity);
        pAdd = findViewById(R.id.add_to_db);

        i =getIntent();
        Name = i.getStringExtra("Name");

        List<Product> list = new ArrayList<>();



        pAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addProduct();
                barcode.setText("");
                pName.setText("");
                pCompany.setText("");
                pQuantity.setText("");
                pPrice.setText("");

            }
        });
        
    }

    private void addProduct() {
        if (barcode.getText().toString().isEmpty()) {
            Toast.makeText(AddProduct.this, "Please Enter Barcode", Toast.LENGTH_SHORT).show();
        } else {
            String barcod = barcode.getText().toString();
            String name = pName.getText().toString();
            String company = pCompany.getText().toString();
            int price = Integer.parseInt(pPrice.getText().toString());
            int quantity = Integer.parseInt(pQuantity.getText().toString());

            Product product = new Product(name, quantity, price, company);
            db.collection(Name).document(barcod).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Toast.makeText(AddProduct.this, "This Product Already Exists", Toast.LENGTH_SHORT).show();
                    } else {

                        db.collection(Name).document(barcod).set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddProduct.this, "Product Added", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProduct.this, "Pelaase Your Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    public void getBarcode(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }
    @Override
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
                barcode.setText(intentResult.getContents());
//                messageFormat.setText(intentResult.getFormatName());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}