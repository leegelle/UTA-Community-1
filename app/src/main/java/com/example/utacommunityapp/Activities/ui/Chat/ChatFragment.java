package com.example.utacommunityapp.Activities.ui.Chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.utacommunityapp.Activities.ListUser;
import com.example.utacommunityapp.Activities.SearchActivity;
import com.example.utacommunityapp.Activities.ui.Chat.ChatViewModel;
import com.example.utacommunityapp.R;

public class ChatFragment extends Fragment {

    private ChatViewModel communityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        communityViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        final TextView textView = root.findViewById(R.id.text);
        communityViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                Intent intent = new Intent(getContext(), ListUser.class);
                startActivity(intent);
            }
        });


        return root;
    }
}