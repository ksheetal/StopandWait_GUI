package com.example.sheetal.stopandwait;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sheetal on 4/23/2018.
 */

public class dialoge extends AppCompatDialogFragment  {
public TextView textView;
   // Intent intent = Intent.g();
    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Data");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialouge_layout,null);
        textView = view.findViewById(R.id.textView8);

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            String hi = null;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // String akl = null;
                String msg = null;
                //for (DataSnapshot doc : dataSnapshot.getChildren()) {
                    msg = (String) dataSnapshot.child("message").getValue();
                   // msg = data1.getMessage();
                 //   akl = data1.getAck();
                    textView.setText(msg);
                }


            //}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        builder.setView(view)
                .setTitle("Your Message")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
     }

}
