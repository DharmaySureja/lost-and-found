package com.example.lostandfound;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.ViewHolder> {

    public List<AllPost> post_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public PostRecyclerAdapter(List<AllPost> post_list){

        this.post_list = post_list;
    }

    @NonNull
    @Override
    public PostRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostRecyclerAdapter.ViewHolder holder, int position) {

        final String PostId = post_list.get(position).AllPostId;

        final String currentUserName = post_list.get(position).getEmailId();
        holder.setEmai(currentUserName);

        final String desc_data = post_list.get(position).getDesc();
        holder.setDescText(desc_data);

        final String image_url = post_list.get(position).getImage_url();
        String thumbUri = post_list.get(position).getImage_thumb();
        holder.setPostImage(image_url, thumbUri);


        final String user_id = post_list.get(position).getUser_id();
        //User Data will be retrieved here...
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    holder.setUserData(userName, userImage);


                } else {

                    //Firebase Exception

                }

            }
        });

        try {
            long millisecond = post_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        holder.ClamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUser = firebaseAuth.getCurrentUser().getEmail(),currentUserUid = firebaseAuth.getCurrentUser().getUid(),documentPath = PostId+currentUserUid;

                if(currentUserUid.trim() != user_id.trim()) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("post_id", PostId);
                    userMap.put("claim_user_uid", currentUserUid);
                    userMap.put("claim_user_id", currentUser);
                    userMap.put("post_img_url",image_url);
                    userMap.put("desc",desc_data);
                    userMap.put("timestamp",FieldValue.serverTimestamp());

                    firebaseFirestore.collection(user_id).document(documentPath).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(context,"Claimed request sent",Toast.LENGTH_LONG).show();
                                holder.ClamBtn.setEnabled(false);
                            }
                            else {
                                Toast.makeText(context,"Claimed request Failed",Toast.LENGTH_LONG).show();
                                holder.ClamBtn.setEnabled(true);
                            }
                        }
                    });

                }else {
                    Toast.makeText(context,"Claimed request Failed This Is Your Post",Toast.LENGTH_LONG).show();
                    holder.ClamBtn.setEnabled(true);

                }

            }
        });



//        holder.postCommentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent commentIntent = new Intent(context, CommentsActivity.class);
//                commentIntent.putExtra("blog_post_id", PostId);
//                context.startActivity(commentIntent);
//
//            }
//        });


    }

    @Override
    public int getItemCount() {

        return post_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView descView,Email,ClamBtn;
        private ImageView postImageView;
        private TextView postDate;

        private TextView postUserName;
        private CircleImageView postUserImage;

        //private ImageView blogLikeBtn;
        //private TextView blogLikeCount;

        private ImageView postCommentBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            ClamBtn = mView.findViewById(R.id.clam_btn);
            //blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            //postCommentBtn = mView.findViewById(R.id.post_comment_icon);
        }

        public void setDescText(String descText){

        descView = mView.findViewById(R.id.new_post_desc);
        descView.setText(descText);

        }

        public void setEmai(String mail){

            Email = mView.findViewById(R.id.post_emailId);
            Email.setText(mail);

        }

        public void setPostImage(String downloadUri, String thumbUri){

            postImageView = mView.findViewById(R.id.history_post_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(postImageView);

        }

        public void setTime(String date) {

            postDate=mView.findViewById(R.id.post_date);

            postDate.setText(date);

        }

        public void setUserData(String name, String image){

            postUserImage = mView.findViewById(R.id.post_user_image);
            postUserName = mView.findViewById(R.id.post_full_name);

            postUserName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.mipmap.profile_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(postUserImage);

        }

        public void updateLikesCount(int count){

            //blogLikeCount = mView.findViewById(R.id.blog_like_count);
            //blogLikeCount.setText(count + " Likes");

        }
    }
}
