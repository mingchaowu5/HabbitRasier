package com.example.liu.habbitrasier;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 3/10/19.
 */

public class CustomListView extends ArrayAdapter<Habit> {
    //Class for custom view adapter of listview

    private Context mContext;
    private List<Habit> habitsList = new ArrayList<>();

    public CustomListView(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Habit> list) {
        super(context, 0, list);
        mContext = context;
        habitsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.listview_layout, parent, false);

        Habit currentHabit = habitsList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.tvHabitName);
        name.setText(currentHabit.getHabitName());

        TextView des = (TextView) listItem.findViewById(R.id.tvDescription);
        des.setText(currentHabit.getDescription());

        return listItem;
    }

    public void removeItem(int position) {

    }

    public long getItemId(int position) {
        return position;
    }
}
