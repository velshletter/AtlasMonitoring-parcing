package com.example.parcing;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import java.sql.Time;

public class TimeDialogFragment extends DialogFragment {

    private ListView timeList;
    TimeListItem timeListItem;
    EditText editText1;

    public String[] timeArray = new String[10];
    private ArrayAdapter<String> adapter;

    public void setListItem(TimeListItem item){
        timeListItem = item;
    }
    public void setListItem(EditText editText){
        editText1 = editText;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.time_dialog_fragment, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        timeList = getView().findViewById(R.id.listTime);
        timeArray = getResources().getStringArray(R.array.depature_time);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, timeArray);
        timeList.setAdapter(adapter);
        timeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                timeListItem.setChosedTime(timeArray[pos]);
                editor.putString(String.valueOf(R.string.time), timeArray[pos]);
                editor.apply();
                dismiss();
                editText1.setText(timeArray[pos]);
            }
        });
    }

}
