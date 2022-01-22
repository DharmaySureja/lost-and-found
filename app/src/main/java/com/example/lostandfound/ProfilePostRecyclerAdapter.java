package com.example.lostandfound;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lostandfound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilePostRecyclerAdapter extends RecyclerView.Adapter<ProfilePostRecyclerAdapter.ViewHolder> {

    public List<ProfilePost> post_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public ProfilePostRecyclerAdapter(List<ProfilePost> post_list){
        this.post_list = post_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_profile_list, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String desc=post_list.get(position).getDesc();

        final String currentUser = firebaseAuth.getCurrentUser().getUid();

        final String claimUser = post_list.get(position).claim_user_uid;
        final String PostId = post_list.get(position).getPost_id();
        final String currentuserMail = firebaseAuth.getCurrentUser().getEmail();

        final String claim_user_id = post_list.get(position).getClaim_user_id();
        holder.setClaimId(claim_user_id);

        final String image_url = post_list.get(position).getPost_img_url();
        holder.setImg(image_url);

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String document = PostId+claimUser;

                firebaseFirestore.collection(currentUser).document(document).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(context,MainActivity.class);
                            i.putExtra("position",3);
                            context.startActivity(i);
                        }else {
                            Toast.makeText(context,"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String receiverMail = claim_user_id;
                final String receiverUid = claimUser;



                Map<String, Object> userMap = new HashMap<>();
                userMap.put("post_id", PostId);
                userMap.put("deliverby", currentUser);
                userMap.put("desc",desc);
                userMap.put("deliverMail", currentuserMail);
                userMap.put("receiverMail",receiverMail);
                userMap.put("receiverUid",receiverUid);
                userMap.put("post_img",image_url);
                userMap.put("timestamp", FieldValue.serverTimestamp());

                String documentpath=PostId+receiverUid;

                firebaseFirestore.collection("History").document(documentpath).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            String document = PostId+claimUser;
                            firebaseFirestore.collection(currentUser).document(document).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        firebaseFirestore.collection("Posts").document(PostId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(context,"Post Store Done..",Toast.LENGTH_SHORT).show();
                                                    Intent i=new Intent(context,MainActivity.class);
                                                    i.putExtra("position",3);
                                                    context.startActivity(i);
                                                }else {
                                                    Toast.makeText(context,"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(context,"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else {

                        }
                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return post_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private ImageView post_img;
        private Button accept,deny;
        private TextView claim_mail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             mView = itemView;

             accept = mView.findViewById(R.id.Accept);
             deny = mView.findViewById(R.id.deny);
        }

        public void setClaimId(String claim_user_id) {
            claim_mail = mView.findViewById(R.id.ReceiverId);
            claim_mail.setText(claim_user_id);
        }

        public void setImg(String image_url) {
            post_img=mView.findViewById(R.id.history_post_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(image_url).into(post_img);
        }
    }
}
