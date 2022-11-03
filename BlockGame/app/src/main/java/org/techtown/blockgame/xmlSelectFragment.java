package org.techtown.blockgame;

import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class xmlSelectFragment extends Fragment {

    Button fileNameView[] = new Button[100000];
    int xmlCount = 0;
    LinearLayout xmlFile;
    Handler handler = new Handler();
    ViewGroup rootView;
    String[] list;

    //private List<String> FileList("assets");

    public void xmlFileRead() {

        try {
            AssetManager assetMgr = MainActivity.mContext.getAssets();
            list = assetMgr.list("");


        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < list.length; i++) {
            if (list[i].substring(list[i].length() - 4, list[i].length()).equals(".xml")) {
                fileNameView[i] = new Button(rootView.getContext());
                fileNameView[i].setText(list[i]);
                int finalI = i;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        xmlFile.addView(fileNameView[finalI]);
                    }
                });
                fileNameView[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setXmlFile((Button) view);
                    }
                });
                xmlCount++;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_xml_select, container, false);

        xmlFile = rootView.findViewById(R.id.xmlFile);

        xmlFileRead();

        return rootView;
    }

    public void setXmlFile(Button view){
        ((MainActivity)MainActivity.mContext).setXmlFileName((String) view.getText());
        ((MainActivity)MainActivity.mContext).onFragmentChange(0);
    }
}