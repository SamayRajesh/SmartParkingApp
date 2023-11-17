package com.example.smartparkingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    EditText L_plate, mno;
    Button submit, status;
    FirebaseDatabase inf;
    DatabaseReference myRef;
    @Override
    /*
    *Method that executes when the app opens
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Identification of input boxes
        L_plate = findViewById(R.id.entered_plate);
        L_plate.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mno = findViewById(R.id.Phoneno);

        //Identification of buttons
        submit = findViewById(R.id.button);

        //Firebase instance and referencing
        inf = FirebaseDatabase.getInstance();
        myRef = inf.getReference("Users");

        /*
         * Method that executes when "Submit" button is pressed
         */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setting value into: "User"->L_plate(variable)->"Mobile number"->(value to be entered which is stored in 'mno')
                myRef.child(L_plate.getText().toString()).child("Mobile number").setValue(mno.getText().toString());

                //setting value into: "User"->L_plate(variable)->"Plate"->(value to be entered which is stored in 'L_plate')
                myRef.child(L_plate.getText().toString()).child("Plate").setValue(L_plate.getText().toString());
                myRef.child(L_plate.getText().toString()).child("Entry Time").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful())
                        {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else
                        {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            if (String.valueOf(task.getResult().getValue()) == "null")
                            {
                                submit.setText("Invalid Details");
                                submit.setBackgroundColor(getResources().getColor(R.color.red));
                                myRef.child(L_plate.getText().toString()).removeValue();
                            } else
                            {
                                submit.setText("Submitted");
                                submit.setBackgroundColor(getResources().getColor(R.color.green));
                                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                intent.putExtra("plate", L_plate.getText().toString());
                                startActivity(intent);
                            }


                        }
                    }
                });

            }

        });

    }



}