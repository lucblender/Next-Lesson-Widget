/*
Copyright © 2018, Lucas Bonvin

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the “Software”), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

The Software is provided “as is”, without warranty of any kind, express or implied, including but
not limited to the warranties of merchantability, fitness for a particular purpose and
noninfringement. In no event shall the authors or copyright holders be liable for any claim,
damages or other liability, whether in an action of contract, tort or otherwise, arising from,
out of or in connection with the software or the use or other dealings in the Software.

Except as contained in this notice, the name of Lucas Bonvin shall not be used in
advertising or otherwise to promote the sale, use or other dealings in this Software without
prior written authorization from Lucas Bonvin.
 */

package com.lucblender.lucasbonvin.widgettest;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class PathAdaptater extends ArrayAdapter<PathItem> implements View.OnClickListener, View.OnLongClickListener {


    private final String TAG = PathAdaptater.class.getName();

    private ArrayList<PathItem> dataSet;
    private Context mContext;



    // View lookup cache
    private static class ViewHolder {
        TextView path;
        RadioButton selectedButton;
        LinearLayout itemLayout;
    }

    public PathAdaptater(ArrayList<PathItem> data, Context context) {
        super(context, R.layout.filerow, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        Log.e(TAG, "onClick: " );

        int position=(Integer) v.getTag();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("filePicker", dataSet.get(position).getPath());
        editor.apply();

        for(int i = 0; i< dataSet.size(); i++)
        {
            if(i == position) {
                dataSet.get(i).setSelected(true);
            }
            else
                dataSet.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean onLongClick(View v) {

        int positionLongClick = (Integer) v.getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogThemeShared);
        builder.setTitle(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.delete));

        String toto []= dataSet.get(positionLongClick).getPath().split("/");
        String fileName = toto[toto.length -1];

        builder.setMessage(mContext.getString(R.string.warning_delete_file_1) + fileName + mContext.getString(R.string.warning_delete_file_2));
        builder.setPositiveButton(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.yes), (dialog, id) -> {
            int position=(Integer) v.getTag();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = preferences.edit();


            if(position == 0 && dataSet.size() == 1)        {
                message(mContext.getString(R.string.cannot_delete_planning));
            }
            else {

                if(dataSet.get(position).getSelected()) {
                    if(position == 0)
                    {
                        dataSet.get(1).setSelected(true);
                        editor.putString("filePicker", dataSet.get(1).getPath());
                        editor.apply();
                    }
                    else
                    {
                        dataSet.get(0).setSelected(true);
                        editor.putString("filePicker", dataSet.get(0).getPath());
                    }
                }
                Set<String> favoritePath = preferences.getStringSet("favoritePath",null);

                favoritePath.remove(dataSet.get(position).getPath());
                editor.putStringSet("favoritePath",  favoritePath);
                editor.apply();

                dataSet.remove(position);
            }

            notifyDataSetChanged();
        });
        builder.setNegativeButton(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.cancel), (dialog, id) -> {
        });
        builder.show();

        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PathItem dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.filerow, parent, false);
            viewHolder.path = convertView.findViewById(R.id.listText);
            viewHolder.selectedButton = convertView.findViewById(R.id.listRadioButton);
            viewHolder.itemLayout = convertView.findViewById(R.id.itemLayout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.path.setText(dataModel.getPath());

        if(dataModel.isPathValid()) {
            final TypedValue value = new TypedValue ();
            getContext().getTheme().resolveAttribute (android.R.attr.textColorPrimary, value, true);
            viewHolder.path.setTextColor(value.data);
        }
        else
            viewHolder.path.setTextColor(getContext().getResources().getColor(R.color.colorError));

        viewHolder.selectedButton.setChecked(dataModel.getSelected());

        viewHolder.itemLayout.setTag(position);
        viewHolder.selectedButton.setTag(position);
        viewHolder.path.setTag(position);


        viewHolder.itemLayout.setOnClickListener(this);
        viewHolder.selectedButton.setOnClickListener(this);
        viewHolder.path.setOnClickListener(this);

        viewHolder.itemLayout.setOnLongClickListener(this);
        viewHolder.selectedButton.setOnLongClickListener(this);
        viewHolder.path.setOnLongClickListener(this);
        // Return the completed view to render on screen
        return convertView;
    }


    void message(String s)
    {
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }
}
