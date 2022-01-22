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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class HistoryPostRecyclerAdapter extends RecyclerView.Adapter<HistoryPostRecyclerAdapter.ViewHolder> {

    public List<HistoryPost> post_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public HistoryPostRecyclerAdapter(List<HistoryPost> post_list){
        this.post_list = post_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_history_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String img_url=post_list.get(position).getPost_img();
        holder.setPost_img(img_url);

        final String rec_by =post_list.get(position).getReceiverMail();
        holder.setReceiverid(rec_by);

        final String deliver = post_list.get(position).getDeliverMail();
        holder.setDeliverid(deliver);

        try {
            long millisecond = post_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
            holder.setDate(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public int getItemCount() {
        return post_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private ImageView post_img;
        private TextView receiverid,deliverid,date;

        public void setPost_img(String post_img_url) {
            post_img =mView.findViewById(R.id.history_post_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(post_img_url).into(post_img);

        }

        public void setReceiverid(String receiverId) {
            receiverid=mView.findViewById(R.id.ReceiverId);
            receiverid.setText(receiverId);
        }

        public void setDeliverid(String deliverId) {
            deliverid=mView.findViewById(R.id.deliverId);
            deliverid.setText(deliverId);
        }

        public void setDate(String tdate) {
            date=mView.findViewById(R.id.post_date);
            date.setText(tdate);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }
}
