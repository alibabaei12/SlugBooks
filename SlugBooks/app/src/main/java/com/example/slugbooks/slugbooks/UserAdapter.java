package com.example.slugbooks.slugbooks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.slugbooks.slugbooks.R;
import com.example.slugbooks.slugbooks.DataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<DataModel> mUsers;
    String theLastMessage;

    public  UserAdapter(){}

    public UserAdapter(Context mContext, List<DataModel> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DataModel user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageUrl().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else{
            Glide.with(mContext).load(user.getImageUrl()).into(holder.profile_image);
        }
        lastMessage(user.getUserID(),holder.last_message);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userID", user.getUserID());
                intent.putExtra("username", user.getUsername());
                mContext.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        private TextView last_message;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            last_message = itemView.findViewById(R.id.last_message);
        }
    }

    //checks for last message
    private void lastMessage(final String userid, final TextView last_message){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatModel chat = snapshot.getValue(ChatModel.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_message.setText("No Message");
                        break;

                    default:
                        last_message.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}