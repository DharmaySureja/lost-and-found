package com.example.lostandfound;

import androidx.annotation.NonNull;

public class HistoryPostId {

    public String HistoryPostId;

    public <T extends HistoryPostId> T withId(@NonNull final String id) {
        this.HistoryPostId = id;
        return (T) this;
    }
}
