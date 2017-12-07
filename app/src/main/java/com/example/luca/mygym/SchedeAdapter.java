package com.example.luca.mygym;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class SchedeAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<>();
    private Context context;

    SchedeAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                view = inflater.inflate(R.layout.listview_row, null);
            }
        }

        //Handle buttons and add onClickListeners
        Button btn = view.findViewById(R.id.button_row);
        btn.setText("Scheda  " + list.get(position));

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Object obj = list.get(position);
                String value = obj.toString();
                Intent intent = new Intent(context, EserciziActivity.class);
                intent.putExtra("Sezione", value);
                context.startActivity(intent);
            }
        });

        return view;
    }
}