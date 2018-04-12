package com.example.sheetal.stopandwait;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreenActivity extends AppCompatActivity {

    Button sendMsg ,sendAck;
    TextView ackno;
    DatabaseReference db,db1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        db = FirebaseDatabase.getInstance().getReference();
        db1 = FirebaseDatabase.getInstance().getReference("Data");
        ackno=findViewById(R.id.msg);
        sendMsg = findViewById(R.id.send_msg_button);
        sendAck = findViewById(R.id.send_acknowledgment);

        progressDialog = new ProgressDialog(this);

        sendAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Sending Acknowledgment");
                progressDialog.setMessage("Wait for moment.....");
                progressDialog.setProgress(0);
                progressDialog.show();


                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        progressDialog.cancel();
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);

                Data data1 = new Data();
                data1.setAck("1");
                //data1.setMessage("");
                db1.setValue(data1);
            }
        });
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(HomeScreenActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        db.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                String akl = null;
                String msg = null;
                for (DataSnapshot doc : dataSnapshot.getChildren()) {
                    Data data1 = doc.getValue(Data.class);
                    msg = data1.getMessage();
                    akl = data1.getAck();
                }

                ackno.setText(msg);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
