package com.example.store;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {
    EditText dateTxt,nameTxt , passwordTxt , emailTxt , addressTxt ;
    Spinner districtTxt;
    Button btnSignup;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ref = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        dateTxt = findViewById(R.id.dob);
        nameTxt = findViewById(R.id.et_signup_name);
        passwordTxt = findViewById(R.id.password);
        emailTxt = findViewById(R.id.email);
        addressTxt = findViewById(R.id.address);

        btnSignup = findViewById(R.id.signup);


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mYear = year;
                        mDay = dayOfMonth;
                        mMonth = monthOfYear;

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserId userId =new UserId(nameTxt.getText().toString(),passwordTxt.getText().toString());
                if(nameTxt.getText().toString().isEmpty()&& passwordTxt.getText().toString().isEmpty())
                {
                    Toast.makeText(SignupActivity.this, "Please Enter User Name and Password", Toast.LENGTH_SHORT).show();
                }else {
                    ref.add(userId).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            View v = getLayoutInflater().inflate(R.layout.confirm_dailog, null);

                            builder.setView(v);
                            TextView bill = v.findViewById(R.id.bill_text);
                            Button ok = v.findViewById(R.id.ok_btn);
                            AlertDialog dialog = builder.create();
                            bill.setText("Your Account Is Created");
                            dialog.show();
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SignupActivity.this, LoginAtivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });



    }
}