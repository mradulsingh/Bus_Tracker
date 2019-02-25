package com.example.ms.gps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircleActivity extends AppCompatActivity {

    Pinview pinview;
    DatabaseReference reference, currentReference, nameReference;
    FirebaseUser user, user1;
    FirebaseAuth auth, auth1;
    String current_user_id, join_user_id, current_user_name, current_user_email, current_user_lng;
    DatabaseReference circleReference;
    LatLng latLng;
    String lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);

//        Intent intent = getIntent();
//        if (intent!=null){
//            lat = intent.getStringExtra("lat");
//            lng = intent.getStringExtra("lng");
//        }

        pinview = findViewById(R.id.pinView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        currentReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("name");
        current_user_id = user.getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_user_name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                current_user_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);

//                current_user_lat = dataSnapshot.child(user.getUid()).child("lat").getValue(String.class);
//                current_user_lng = dataSnapshot.child(user.getUid()).child("lng").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void submitButtonClick(View view) {
        // 1. to check if input code is present or not in database
        // 2. if present, find that user and create a node
        Query query = reference.orderByChild("code").equalTo(pinview.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    CreateUser createUser = null;
                    for (DataSnapshot childDss : dataSnapshot.getChildren()){
                        createUser = childDss.getValue(CreateUser.class);
                        join_user_id = createUser.userId;

                        circleReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(join_user_id).child("CircleMembers");//current id(mmm)

                        CircleJoin circleJoin = new CircleJoin( current_user_name, current_user_email );//current id(mmm)
                        CircleJoin circleJoin1 = new CircleJoin( current_user_name, current_user_email);//parent id(ms)

                        circleReference.child(current_user_id).setValue(circleJoin)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "you joined"+join_user_id, Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "wrong code entered", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "circle code is invalid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
