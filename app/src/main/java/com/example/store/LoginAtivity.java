package com.example.store;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginAtivity extends AppCompatActivity {

    TextView loginText;
    EditText name;
    EditText pass;
    Button btn;
    Animation textAnim , loginAnim , btnAnim , text;
    ImageView img;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ref = db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ativity);

        loginText = findViewById(R.id.login);
        name = findViewById(R.id.et_name);
        pass = findViewById(R.id.pass);
        btn = findViewById(R.id.loin_btn);
        img = findViewById(R.id.imageView2);


       textAnim = AnimationUtils.loadAnimation(this , R.anim.text_anim);
        loginAnim = AnimationUtils.loadAnimation(this , R.anim.logintext_anim);
        btnAnim = AnimationUtils.loadAnimation(this , R.anim.btn_anim);
        text = AnimationUtils.loadAnimation(this , R.anim.textview_anim);



            btn.setAnimation(btnAnim);
            name.setAnimation(textAnim);
            pass.setAnimation(textAnim);
            img.setAnimation(loginAnim);
            loginText.setAnimation(text);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    List<UserId> userIds = new ArrayList<UserId>();
                    List<String> Tname = new ArrayList<>();
                    List<String> Tpass = new ArrayList<>();
                    db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Toast.makeText(LoginAtivity.this, "Got the data", Toast.LENGTH_SHORT).show();

                            for( DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                               UserId userId= new UserId();
                                      userId= doc.toObject(UserId.class);
                                      String Name =doc.getString("username");
                                      String Pass = doc.getString("password");

                                      Tname.add(Name);
                                      Tpass.add(Pass);

//                               userIds.add(userId);

                            }

                            

//                            UserId id = new UserId(name.getText().toString(),pass.getText().toString());

                                if (Tname.contains(name.getText().toString())&& Tpass.contains(pass.getText().toString())) {
                                    Toast.makeText(LoginAtivity.this, "Done", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginAtivity.this, MainActivity.class);
                                intent.putExtra("Name",name.getText().toString());
                                    startActivity(intent);

                                }



                        }
                    });

                }
            });

    }

    public void CreateCount(View view) {
        Intent intent = new Intent(LoginAtivity.this,SignupActivity.class);
        startActivity(intent);
    }
}