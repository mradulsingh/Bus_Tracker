package com.example.ms.gps;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.ms.gps.R.layout.card_layout;

public class Mycircle2 extends AppCompatActivity {
    //firebase
//    DatabaseReference onlineRef, currentUserRef, counterRef;
//    FirebaseRecyclerAdapter<CreateUser, ListOnlineViewHolder> adapter;

    //View
    RecyclerView listOnline;
    RecyclerView.LayoutManager layoutManager;

//////////////////////////////////////////////////////////////////////////////////////////////////
    String userId, joined_user_name, joined_user_id, user_name, user_email;
    DatabaseReference databaseReference, reference;


//    RecyclerView recyclerView;
//    RecyclerView.Adapter adapter;
//    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth auth;
    FirebaseUser user;
//    CreateUser createUser;
//    ArrayList<CreateUser> namelist;
//    DatabaseReference reference, userReference;

    private ListView li;


    ArrayAdapter<String> Adapter;
    List<String> itemList;
    //////////////////////////////jugad//////////////
//    Firebase mRef;
//    private ListView mListView;
    ArrayList<String> mUsername = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycircle2);

//        //init view
//        listOnline = (RecyclerView)findViewById(R.id.listOnline);
//        listOnline.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        listOnline.setLayoutManager(layoutManager);
//
//        //fiebase
//        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
//        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");//create new child name lastOnline
//        currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
//                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        setupSystem();
//        //Apply setup system, we just load all user from counterRef and display on recyclerView
//        //this is online list
//        updateList();

        //////////////experts//////////////////
//        recyclerView = findViewById(R.id.recyclerView);
//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//        namelist = new ArrayList<>();
//
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//
//        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
//        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMembers");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                namelist.clear();
//                for (DataSnapshot items:dataSnapshot.getChildren()){
//                    user_name = items.child("name").getValue(String.class);
//                    itemList.add(user_name);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        /////////////////////experts//////////////

//        mRef = new Firebase("https://gps-tracker-7b97e.firebaseio.com");
//        mListView = findViewById(R.id.listt);
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUsername);
//        mListView.setAdapter(arrayAdapter);
//        mRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
//                String value = dataSnapshot.getValue(String.class);
//                mUsername.add(value);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//////////////////////////////////////////////////////////////////////////////////////
        li = findViewById(R.id.listt);
        li.setAdapter(Adapter);
        itemList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("CircleMembers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot items:dataSnapshot.getChildren()){
                    //user_name = items.child("name").getValue(String.class);
                    user_email = items.child("name").getValue(String.class);
                    itemList.add(user_email);
                }

                Adapter = new ArrayAdapter<>(Mycircle2.this, android.R.layout.simple_list_item_1, itemList);
                li.setAdapter(Adapter);
                li.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(),user_email, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Mycircle2.this, UserLocationMainActivity.class);
                        intent.putExtra("email", user_email);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void updateList() {
//        adapter = new FirebaseRecyclerAdapter<CreateUser, ListOnlineViewHolder>() {
//            @Override
//            protected void onBindViewHolder(@NonNull ListOnlineViewHolder holder, int position, @NonNull CreateUser model) {
//
//            }
//
//            @NonNull
//            @Override
//            public ListOnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//        };
    }

//    private void setupSystem(){
//        onlineRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue(Boolean.class)){
//                    currentUserRef.onDisconnect().removeValue();//delete old value
//                    //set online user in list
//                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(new CreateUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Online"));
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        counterRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
//                    CreateUser createUser = postSnapshot.getValue(CreateUser.class);
//                    Log.d("LOG", ""+createUser.getEmail()+ " is "+createUser.getStatus());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//}
