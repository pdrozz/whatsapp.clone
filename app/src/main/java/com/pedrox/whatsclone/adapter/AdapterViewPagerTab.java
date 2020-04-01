package com.pedrox.whatsclone.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.pedrox.whatsclone.fragment.CameraFragment;
import com.pedrox.whatsclone.fragment.ChamadasFragment;
import com.pedrox.whatsclone.fragment.ConversasFragment;
import com.pedrox.whatsclone.fragment.StatusFragment;

public class AdapterViewPagerTab extends FragmentPagerAdapter {


    //construtores
    public AdapterViewPagerTab(@NonNull FragmentManager fm) {
        super(fm);
    }

    public AdapterViewPagerTab(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    //retorna os fragments para o viewPager
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CameraFragment();
            case 1:
                return new ConversasFragment();
            case 2:
                return new StatusFragment();
            case 3:
                return new ChamadasFragment();
        }
        return null;
    }
    //retorna quantidade de tabs/abas
    @Override
    public int getCount() {
        return 4;
    }
}
