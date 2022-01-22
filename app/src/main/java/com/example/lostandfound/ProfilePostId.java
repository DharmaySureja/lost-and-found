package com.example.lostandfound;

import androidx.annotation.NonNull;

public class ProfilePostId {

    public String ProfilePostId;

    public <T extends ProfilePostId> T withId(@NonNull final String id) {
        this.ProfilePostId = id;
        return (T) this;
    }
}
