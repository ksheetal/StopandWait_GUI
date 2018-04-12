package com.example.sheetal.stopandwait;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    DatabaseReference databaseReference, db;
    EditText message;

    public boolean isRunning = false;
    Button sendData;
    TextView ackl,Tcounter;

    CountDownTimer myCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("Data");
        db  = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        Tcounter = findViewById(R.id.text_counter);
        message = findViewById(R.id.editText);
        ackl = findViewById(R.id.ack);
        sendData = findViewById(R.id.button);

        sendData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ackl.getText().toString().equals("0")) {
                    Toast.makeText(MainActivity.this, "Last Acknowledgment not received! Wait for timer to finish.", Toast.LENGTH_SHORT).show();
                }else {

                    StartProgress();
                    Data data = new Data();
                    data.setAck("0");
                    data.setMessage(message.getText().toString());
                    databaseReference.setValue(data);
                    StartCounter();
                    //myCounter.start();
                }
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
                ackl.setText(akl);
                if(ackl.getText().toString().equals("1")){
                   // isRunning = true;
           //         StartCounter();
                    Toast.makeText(MainActivity.this,"Acknowledgment Updated.",Toast.LENGTH_SHORT).show();
                    //isRunning = true;
                    //                myCounter.cancel();
                    Tcounter.setText("Done!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void StartCounter() {

         CountDownTimer myCounter = new CountDownTimer(25000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Tcounter.setText("Seconds remaining :" + millisUntilFinished / 1000);

            }

            @Override
            public void onFinish() {
                if(ackl.getText().toString().equals("0")){
                    Toast.makeText(MainActivity.this,"Acknowledgment was not received. :(",Toast.LENGTH_SHORT).show();
                    Data data = new Data();
                    data.setAck("1");
                    data.setMessage(message.getText().toString());
                    databaseReference.setValue(data);
                }else{
                    Toast.makeText(MainActivity.this,"Acknowledgment was received in time :)",Toast.LENGTH_SHORT).show();
                }
                Tcounter.setText("Done!");
            }
        }.start();
    }


    private void StartProgress() {

        progressDialog.setTitle("Sending Package");
        progressDialog.setMessage("Wait for moment...");
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
    }
}
