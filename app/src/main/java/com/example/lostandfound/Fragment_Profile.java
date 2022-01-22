package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lostandfound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Profile extends Fragment {

    TextView logout,setup,userName;
    String user_id=null;
    CircleImageView user_img;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private RecyclerView profile_post_list_view;
    private List<ProfilePost> post_list;

    private ProfilePostRecyclerAdapter profilePostRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

    public Fragment_Profile(){
        //Empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View frag_profile = inflater.inflate(R.layout.fragment_profile,container,false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        user_id = firebaseAuth.getCurrentUser().getUid();

        post_list = new ArrayList<>();
        profile_post_list_view = frag_profile.findViewById(R.id.profile_post_list_view);

        userName =frag_profile.findViewById(R.id.user_name);
        user_img = frag_profile.findViewById(R.id.userImg);
        logout =frag_profile.findViewById(R.id.lout);
        setup = frag_profile.findViewById(R.id.set);

        profilePostRecyclerAdapter = new ProfilePostRecyclerAdapter(post_list);
        profile_post_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        profile_post_list_view.setAdapter(profilePostRecyclerAdapter);
        profile_post_list_view.setHasFixedSize(true);

        if(firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            profile_post_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if(reachedBottom){

                        loadMorePost();

                    }

                }
            });

            Query firstQuery = firebaseFirestore.collection(user_id).orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        if (isFirstPageFirstLoad) {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            post_list.clear();

                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String ProfilePostId = doc.getDocument().getId();
                                ProfilePost profilePost = doc.getDocument().toObject(ProfilePost.class).withId(ProfilePostId);

                                if (isFirstPageFirstLoad) {

                                    post_list.add(profilePost);

                                } else {

                                    post_list.add(0, profilePost);

                                }


                                profilePostRecyclerAdapter.notifyDataSetChanged();

                            }
                        }

                        isFirstPageFirstLoad = false;

                    }

                }

            });

        }

        // Get and Set Current User Details
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");


                        userName.setText(name);


                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.mipmap.default_image);

                        Glide.with(getActivity()).setDefaultRequestOptions(placeholderRequest).load(image).into(user_img);

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }
            }
        });

        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SetupActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),Login.class));
            }
        });
        return frag_profile;
    }

    private void loadMorePost() {
        if(firebaseAuth.getCurrentUser() != null) {

            Query nextQuery = firebaseFirestore.collection(user_id)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(3);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String ProfilePostId = doc.getDocument().getId();
                                ProfilePost profilePost = doc.getDocument().toObject(ProfilePost.class).withId(ProfilePostId);
                                post_list.add(profilePost);

                                profilePostRecyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    }

                }
            });

        }

    }
}
