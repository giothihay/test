package com.es.chatapp.Fragments;





import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.es.chatapp.MessageActivity;
import com.es.chatapp.Model.Friends;
import com.es.chatapp.ProfileActivity;
import com.es.chatapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mRequestList;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;
    TextView show;
    private String mCurrent_user_id;

    private View mMainView;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =inflater.inflate(R.layout.fragment_requests, container, false);
        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_lista);
        mAuth = FirebaseAuth.getInstance();
        show = mMainView.findViewById(R.id.show);
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mRequestList = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrent_user_id);
        mRequestList.keepSynced(true);
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);


        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Friends, FriendsFragment.FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsFragment.FriendsViewHolder>(

                Friends.class,
                R.layout.users_single_layout,
                FriendsFragment.FriendsViewHolder.class,
                mRequestList


        ) {
            @Override
            protected void populateViewHolder(final FriendsFragment.FriendsViewHolder friendsViewHolder, Friends friends, int i) {


                friendsViewHolder.setDate(friends.getDate());

                final String list_user_id = getRef(i).getKey();
               // final String sent = getRef(i).child("request_type").toString();
             //   Log.d("AAAA", "populateViewHolder() called with: friendsViewHolder = [" + friendsViewHolder + "], friends = [" + friends + "], i = [" + i + "]");
                   // if(sent.equalsIgnoreCase("sent")){
                       // friendsViewHolder.setVisible();
                   // } else {
                        mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                              //  String req_type = dataSnapshot.child(list_user_id).child("request_type").getValue().toString();

                                show.setVisibility(View.GONE);
                                final String userName = dataSnapshot.child("username").getValue().toString();
                                String userThumb = dataSnapshot.child("imageURL").getValue().toString();

                                if (dataSnapshot.hasChild("online")) {

                                    String userOnline = dataSnapshot.child("online").getValue().toString();
                                    friendsViewHolder.setUserOnline(userOnline);

                                }

                                friendsViewHolder.setName(userName);
                                friendsViewHolder.setUserImage(userThumb, getContext());
                                friendsViewHolder.setDate("");
                                friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        CharSequence options[] = new CharSequence[]{"Disagree or agree", "Send message"};

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                        builder.setTitle("Choose Options");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                //Click Event for each item.
                                                if (i == 0) {

                                                    Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                                    profileIntent.putExtra("user_id", list_user_id);
                                                    startActivity(profileIntent);

                                                }

                                                if (i == 1) {

                                                    Intent chatIntent = new Intent(getContext(), MessageActivity.class);
                                                    chatIntent.putExtra("userid", list_user_id);
                                                    //chatIntent.putExtra("user_name", userName);
                                                    startActivity(chatIntent);

                                                }

                                            }
                                        });

                                        builder.show();

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                   // }
            }
        };

        mFriendsList.setAdapter(friendsRecyclerViewAdapter);


    }
}
