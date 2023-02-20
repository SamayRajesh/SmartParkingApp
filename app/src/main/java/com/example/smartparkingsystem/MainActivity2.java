package com.example.smartparkingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {
    FirebaseDatabase inf;
    DatabaseReference myRef1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //licence plate fetching
        String value1 = super.getIntent().getExtras().getString("plate");
        TextView plate=findViewById(R.id.textView2);
        String lp="Licence plate: ";
        String b=lp.concat(value1);
        plate.setText(b);

       inf = FirebaseDatabase.getInstance();
        myRef1 = inf.getReference("Users");
        myRef1.child(value1).child("time").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    TextView entry=findViewById(R.id.editTextPhone);
                    String str="Entry time: ";
                    String str2=str.concat(String.valueOf(task.getResult().getValue()));
                    entry.setText(str2);
                }
            }
        });


        //back button
        Button b1=findViewById(R.id.button3);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}

