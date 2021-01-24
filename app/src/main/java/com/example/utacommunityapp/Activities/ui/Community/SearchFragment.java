package com.example.utacommunityapp.Activities.ui.Community;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.utacommunityapp.Activities.Messaging;
import com.example.utacommunityapp.Activities.SearchActivity;
import com.example.utacommunityapp.R;

public class SearchFragment extends Fragment {

    private SearchViewModel communityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        communityViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_comm, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        communityViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }
}