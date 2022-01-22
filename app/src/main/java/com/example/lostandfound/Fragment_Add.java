package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lostandfound.R;

public class Fragment_Add extends Fragment {

    ImageView found,lost;

    public Fragment_Add(){
        //Empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View frag_add = inflater.inflate(R.layout.fragment_add,container,false);

        found = frag_add.findViewById(R.id.found);
        lost = frag_add.findViewById(R.id.lost);

        found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),FoundActivity.class));
            }
        });

        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),LostActivity.class));
            }
        });

        return frag_add;

    }
}
