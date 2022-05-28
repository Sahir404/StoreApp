package com.example.store;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Animation logoAnim , btnsAnim;
    ImageView img;
    Context c = this;
    String name ="";
    Intent i;
    TextView wlcm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoAnim = AnimationUtils.loadAnimation(c,R.anim.logo_anim);
        btnsAnim = AnimationUtils.loadAnimation(c,R.anim.btns_anim);
        wlcm = findViewById(R.id.weclmtext);

        i =getIntent();
        name = i.getStringExtra("Name");

        wlcm.setText("Welcome "+name);



//        layout = findViewById(R.id.linn);
        img = findViewById(R.id.logo);

//        layout.setAnimation(btnsAnim);
        img.setAnimation(logoAnim);


    }

    public void addproduct(View view) {

        Intent intent = new Intent(MainActivity.this,AddProduct.class);
        intent.putExtra("Name",name);
        startActivity(intent);

    }

    public void SellProduct(View view) {
        Intent intent = new Intent(MainActivity.this,SellProduct.class);
        intent.putExtra("Name",name);
        startActivity(intent);
    }

    public void updatStock(View view) {

        Intent intent = new Intent(MainActivity.this,UpdateStock.class);
        intent.putExtra("Name",name);
        startActivity(intent);


    }

    public void LogOut(View view) {
        Intent intent = new Intent(MainActivity.this,LoginAtivity.class);
        startActivity(intent);
        finish();
    }
}