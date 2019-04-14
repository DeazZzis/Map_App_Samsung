package com.example.deathis.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.deathis.myapplication.Models.Chat;
import com.example.deathis.myapplication.Models.Post;
import com.example.deathis.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.RViewHoldeR> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;

    FirebaseUser firebaseUser;

    private String imageURL;


    public MessageAdapter(Context context, List<Chat> mChat, String imageURL) {
        this.mChat = mChat;
        this.mContext = context;
        this.imageURL = imageURL;
    }

    @Override
    public MessageAdapter.RViewHoldeR onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if ( viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.RViewHoldeR(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.RViewHoldeR(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.RViewHoldeR holder, int position) {

        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());
        if (imageURL.equals("def")){
            holder.profile_image.setImageResource(R.drawable.ic_action_img_person);
        } else {
            Glide.with(mContext).load(imageURL).into(holder.profile_image);
        }

         if (position == mChat.size()-1){
             if (chat.isIsseen()){
                 holder.txt_seen.setText("Переглянуто");
             } else {
                 holder.txt_seen.setText("Lоставлено");
             }
         } else {
             holder.txt_seen.setVisibility(View.GONE);
         }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    class RViewHoldeR extends RecyclerView.ViewHolder {

        public TextView show_message;
        public CircleImageView profile_image;

        public TextView txt_seen;


        public RViewHoldeR(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }

        void bind(String s1, String lat2, String lng2, Post post) {

        }


    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}

