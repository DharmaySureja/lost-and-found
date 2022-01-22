package com.example.lostandfound;

import androidx.annotation.NonNull;

public class AllPostId {

    public String AllPostId;

    public <T extends AllPostId> T withId(@NonNull final String id) {
        this.AllPostId = id;
        return (T) this;
    }
}
