package org.techtown.blockgame;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    View initBg;
    TextView sentence;

    // 메모리에 올리면서 연결해준다
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        initBg = rootView.findViewById(R.id.initBg);
        sentence = rootView.findViewById(R.id.sentence);

        return rootView;
    }

    public void setInitBackground(int imageResource) {
        initBg.setBackgroundResource(imageResource);
    }

    public void setSentence(String s) {
        sentence.setText(s);
    }

    public void setFontColor(int r, int g, int b){
        sentence.setTextColor(Color.rgb(r,g,b));
    }

}