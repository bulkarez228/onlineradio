package com.example.onlineradio;

import android.view.View;

public interface ActionPlaying {
    void prepare (String uri);
    void OnMedia (View view);
    void stopPlayer();
}
