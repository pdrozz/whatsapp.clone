package com.pedrox.whatsclone.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrox.whatsclone.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChamadasFragment extends Fragment {


    public ChamadasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chamadas, container, false);
    }

}
