package com.example.smartparkingsystem;

import static com.example.smartparkingsystem.Help.convertHoursToMinutes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {
    FirebaseDatabase inf;
    DatabaseReference myRef1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //licence plate fetching
        String value1 = super.getIntent().getExtras().getString("plate");
        TextView plate=findViewById(R.id.plate_no);
        String lp="Plate: ";
        String b=lp.concat(value1);
        plate.setText(b);

       inf = FirebaseDatabase.getInstance();
        myRef1 = inf.getReference("Users");
        //Entry time pull
        myRef1.child(value1).child("Entry Time").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                   if (!task.isSuccessful()) {
                       Log.e("firebase", "Error getting data", task.getException());
                   } else {
                       Log.d("firebase", String.valueOf(task.getResult().getValue()));
                       if(String.valueOf(task.getResult().getValue())=="null"){
                           TextView time = findViewById(R.id.approx_time);
                           time.setText("Error");
                           TextView approx_cost_text=findViewById(R.id.capproxcost);
                           approx_cost_text.setText("Error");

                       }
                       else {
                           TextView entry = findViewById(R.id.entry_temp);
                           entry.setText(String.valueOf(task.getResult().getValue()));
                           Date currentTime = Calendar.getInstance().getTime();
                           String start = convertHoursToMinutes(entry.getText().toString());
                           String end = convertHoursToMinutes(String.valueOf(currentTime).substring(11, 20));
                           int current_time = Integer.parseInt(end);
                           int start_time = Integer.parseInt(start);
                           int i_approx_time = current_time - start_time;
                           int i_approx_time_h = i_approx_time / 60;
                           int i_approx_time_m = i_approx_time % 60;
                           String final_string = String.format("Time Spent: %2d h %2d m", i_approx_time_h, i_approx_time_m);
                           TextView time = findViewById(R.id.approx_time);
                           time.setText(final_string);
                           int cost = 0;
                           if (i_approx_time >= 0 && i_approx_time < 60) {
                               cost = 30;
                           } else if (i_approx_time >= 60 && i_approx_time < 120) {
                               cost = 40;
                           } else if (i_approx_time >= 120 && i_approx_time < 180) {
                               cost = 60;
                           } else if (i_approx_time >= 180 && i_approx_time < 360) {
                               cost = 100;
                           } else {
                               cost = 100;
                               int loop_int = i_approx_time - 360;
                               loop_int = loop_int / 60;
                               for (int i = 1; i <= loop_int; i++) {
                                   cost = cost + 50;
                               }
                           }
                           TextView approx_cost_text = findViewById(R.id.capproxcost);
                           String prefix = "Approx. Cost: ₹";
                           approx_cost_text.setText(prefix.concat(String.valueOf(cost)));

                       }
                   }
            }
        });




        //back button
        Button b1=findViewById(R.id.back1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        //payment button
        Button b2=findViewById(R.id.payment);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                intent.putExtra("plate1", value1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,1);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}


