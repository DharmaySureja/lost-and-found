package com.example.lostandfound;

import androidx.annotation.NonNull;

public class LostPostId {

    public String LostPostId;

    public <T extends LostPostId> T withId(@NonNull final String id) {
        this.LostPostId = id;
        return (T) this;
    }
}
