package com.example.sheetal.stopandwait;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.UniversalTimeScale;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    public ImageView imageView;

    TextView textView9;

    DatabaseReference databaseReference, db;
    EditText message;

    public boolean isRunning = false;
    Button sendData,Breceiver,Bsender;
    TextView ackl,Tcounter,dialData;

    CountDownTimer myCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("Data");
        db  = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);


        final ImageView imageView = findViewById(R.id.imageView);
        Tcounter = findViewById(R.id.text_counter);
        message = findViewById(R.id.editText);
        ackl = findViewById(R.id.ack);
        sendData = findViewById(R.id.button);
        Breceiver = findViewById(R.id.button1);
        Bsender = findViewById(R.id.button2);
        textView9 = findViewById(R.id.textView9);
        //dialData = findViewById(R.id.textView8);

        final Animation move1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move);


        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialoge();
            }
        });

        sendData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data3 = null;
                Bsender.setBackgroundColor(Color.RED);
                Breceiver.setBackgroundColor(Color.BLACK);
                if (ackl.getText().toString().equals("0")) {
                    Breceiver.setBackgroundColor(Color.BLACK);
                    imageView.setImageResource(R.drawable.acknotrec);
                    Toast.makeText(MainActivity.this, "Last packet's  Acknowledgment not received! Wait for timer to finish.", Toast.LENGTH_SHORT).show();
                }else {

                    //StartProgress();
                    imageView.setImageResource(R.drawable.afterack);
                    Data data = new Data();
                    Toast.makeText(MainActivity.this, "Packet send!", Toast.LENGTH_SHORT).show();
                    Random r = new Random();
                    int i = r.nextInt(3 - 1)+1;
                    Toast.makeText(MainActivity.this,"Random Value  : " +i,Toast.LENGTH_SHORT).show();
                    if(i==2) {
                        Bsender.startAnimation(move1);
                        Toast.makeText(MainActivity.this,"Packet Lost.!!",Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.packetlostimg);
                        Bsender.clearAnimation();
                    }else {
                        data.setAck("0");
                        data3 =message.getText().toString();
                        data.setMessage(data3);
                        //  dialData.setText(message.getText().toString());
                        returnmessage(data);
                        databaseReference.setValue(data);
                        StartCounter();
                        Bsender.startAnimation(move1);
                        message.setText("");
                        //myCounter.start();
                    }

                }
            }
        });


      /*  int secs = 2; // Delay in seconds

        Utils.(secs, new Utils.DelayCallback() {
            @Override
            public void afterDelay() {
                // Do something after delay

            }
        });
*/
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
                    imageView.setImageResource(R.drawable.packetreceived);
                    Bsender.clearAnimation();
                    Breceiver.setBackgroundColor(Color.GREEN);
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

    public String returnmessage(Data data) {
        //Intent intent = new Intent(MainActivity.this,dialoge.class);
        //intent.putExtra("hello",message.getText().toString());
        //startActivity(intent);
        return String.valueOf(data);
    }

    private void openDialoge() {
        dialoge dialoge = new dialoge();
        dialoge.show(getSupportFragmentManager(),"Sender's Message");

        }

    private void StartCounter() {

         CountDownTimer myCounter = new CountDownTimer(25000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Tcounter.setText("Seconds remaining :" + millisUntilFinished / 1000);

            }

            @Override
            public void onFinish() {
                checkdata();
            }
        }.start();
    }

    private void checkdata() {
        if(ackl.getText().toString().equals("0")){
            imageView = findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.acknotrec);
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
