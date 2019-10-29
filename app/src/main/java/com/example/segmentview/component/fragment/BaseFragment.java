package com.example.segmentview.component.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.segmentview.R;

public class BaseFragment extends Fragment {
    private String content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deserializeBundle();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        TextView tv = view.findViewById(R.id.textView);
        tv.setText(content);
        return view;
    }

    /* bundle methods */
    public static Bundle serializeBundle(String content) {
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        return bundle;
    }

    private void deserializeBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            content = bundle.getString("content");
        }
    }
}
