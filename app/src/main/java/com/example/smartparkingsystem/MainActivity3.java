package com.example.smartparkingsystem;

import static com.example.smartparkingsystem.Help.convertHoursToMinutes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity3 extends AppCompatActivity {
    FirebaseDatabase inf;
    DatabaseReference myRef1;
ImageView QR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        String value1 = super.getIntent().getExtras().getString("plate1");
        QR=findViewById(R.id.imageView2);
        QR.setImageResource(R.drawable.img);
        inf = FirebaseDatabase.getInstance();
        myRef1 = inf.getReference("Users");
        myRef1.child(value1).child("Entry Time").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else
                {
                    Log.d("firebase1", String.valueOf(task.getResult().getValue()));
                    if(String.valueOf(task.getResult().getValue())!="null") {
                        TextView entry = findViewById(R.id.entrytime);
                        entry.setText(String.valueOf(task.getResult().getValue()));
                        myRef1.child(value1).child("Exit Time").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                } else {
                                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                    if (String.valueOf(task.getResult().getValue()) == "null") {
                                        TextView appxcost = findViewById(R.id.Phoneno);
                                        appxcost.setText("Error");
                                        TextView time = findViewById(R.id.enteredplate);
                                        time.setText("Error");

                                    } else {
                                        TextView exit = findViewById(R.id.exittime);
                                        TextView entry1 = findViewById(R.id.entrytime);
                                        exit.setText(String.valueOf(task.getResult().getValue()));
                                        String start = convertHoursToMinutes(entry1.getText().toString());
                                        String end = convertHoursToMinutes(exit.getText().toString());
                                        int ends = Integer.parseInt(end);
                                        int starts = Integer.parseInt(start);
                                        int i_total_time = ends - starts;
                                        int i_total_time_h = i_total_time / 60;
                                        int i_total_time_m = i_total_time % 60;
                                        String final_string = String.format("Total time: %2d h %2d m", i_total_time_h, i_total_time_m);
                                        TextView time = findViewById(R.id.enteredplate);
                                        time.setText(final_string);
                                        int cost = 0;
                                        if (i_total_time >= 0 && i_total_time < 60) {
                                            cost = 30;
                                        } else if (i_total_time >= 60 && i_total_time < 120) {
                                            cost = 40;
                                        } else if (i_total_time >= 120 && i_total_time < 180) {
                                            cost = 60;
                                        } else if (i_total_time >= 180 && i_total_time < 360) {
                                            cost = 100;
                                        } else {
                                            cost = 100;
                                            int loop_int = i_total_time - 360;
                                            loop_int = loop_int / 60;
                                            for (int i = 1; i <= loop_int; i++) {
                                                cost = cost + 50;
                                            }
                                        }
                                        TextView appxcost = findViewById(R.id.Phoneno);
                                        String prefix = "Total Cost: ₹";
                                        appxcost.setText(prefix.concat(String.valueOf(cost)));
                                    }
                                }
                            }
                        });
                    }
                    else{
                        TextView appxcost = findViewById(R.id.Phoneno);
                        appxcost.setText("Error");
                        TextView time = findViewById(R.id.enteredplate);
                        time.setText("Error");
                    }
                }
            }
        });

        //back button
        Button b1=findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity3.this,MainActivity2.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(0);
                finish();
            }
        });
        //logout button
        Button b2=findViewById(R.id.logoutb);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef1.child(value1).removeValue();
                setResult(RESULT_OK);
                finish();
            }
        });
    }


}