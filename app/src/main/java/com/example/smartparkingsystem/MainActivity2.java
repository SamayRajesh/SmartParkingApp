package com.example.smartparkingsystem;

import static com.example.smartparkingsystem.Help.convertHoursToMinutes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    FirebaseDatabase inf1;
    DatabaseReference myRef1;

    ImageView map;

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
        final boolean[] A1free = new boolean[1];
        inf1 = FirebaseDatabase.getInstance();
        myRef1 = inf1.getReference("Lot_1");
        map=findViewById(R.id.imageView);
        myRef1.child("A1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful())
                {
                    Log.e("firebase", "Error getting data", task.getException());
                } else
                {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    if (String.valueOf(task.getResult().getValue()) == "false")
                    {
                       TextView plot=findViewById(R.id.plot);
                       String pl="Plot: ";
                       String p=pl.concat("B1");
                       TextView plotv=findViewById(R.id.plot);
                       plotv.setText(p);

                       A1free[0] =false;
                        System.out.println(A1free[0]+"in b1");



                    } else
                    {
                        TextView plot=findViewById(R.id.plot);
                        String pl="Plot: ";
                        String p=pl.concat("A1");
                        TextView plotv=findViewById(R.id.plot);
                        plotv.setText(p);
                        A1free[0]=true;
                        System.out.println(A1free[0]+"in a1");

                    }


                }
            }
        });
        System.out.println(A1free[0]+"out");
    map.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
       if(A1free[0]){
           map.setImageResource(R.drawable.a1);
       }
       else {
           map.setImageResource(R.drawable.b1);
       }
        return false;
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
                startActivity(intent);
            }
        });

    }

}


