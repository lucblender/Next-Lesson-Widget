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

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.lucblender.lucasbonvin.widgettest.Data.DataCsvManager;
import com.lucblender.lucasbonvin.widgettest.Data.LessonLine;
import com.lucblender.lucasbonvin.widgettest.UserInterface.AddLessonDialog;
import com.lucblender.lucasbonvin.widgettest.UserInterface.DeleteCustomDialog;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder>{
    private ArrayList<LessonLine> mLessonLines;

    public LessonAdapter(ArrayList<LessonLine> lessonLines)
    {
        mLessonLines = lessonLines;
    }

    @Override
    public LessonAdapter.LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create the view of item of recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(com.lucblender.lucasbonvin.widgettest.R.layout.lessonweek, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LessonViewHolder holder, int position) {
        LessonLine lessonLine = mLessonLines.get(position);
        //temp string used to concatenate others
        String temp;

        //fulfill all text view from lesson
        temp = lessonLine.lessons[0].hourStart + " - "+lessonLine.lessons[0].hourEnd;
        holder.timeMon.setText(temp);
        holder.lessonMon.setText(lessonLine.lessons[0].lessonName );
        holder.locationMon.setText(lessonLine.lessons[0].city );
        holder.roomMon.setText(lessonLine.lessons[0].room );

        temp = lessonLine.lessons[1].hourStart + " - "+lessonLine.lessons[1].hourEnd;
        holder.timeTue.setText(temp);
        holder.lessonTue.setText(lessonLine.lessons[1].lessonName );
        holder.locationTue.setText(lessonLine.lessons[1].city );
        holder.roomTue.setText(lessonLine.lessons[1].room );


        temp = lessonLine.lessons[2].hourStart + " - "+lessonLine.lessons[2].hourEnd;
        holder.timeWed.setText(temp);
        holder.lessonWed.setText(lessonLine.lessons[2].lessonName );
        holder.locationWed.setText(lessonLine.lessons[2].city );
        holder.roomWed.setText(lessonLine.lessons[2].room );

        temp = lessonLine.lessons[3].hourStart + " - "+lessonLine.lessons[3].hourEnd;
        holder.timeThu.setText(temp);
        holder.lessonThu.setText(lessonLine.lessons[3].lessonName );
        holder.locationThu.setText(lessonLine.lessons[3].city );
        holder.roomThu.setText(lessonLine.lessons[3].room );

        temp = lessonLine.lessons[4].hourStart + " - "+lessonLine.lessons[4].hourEnd;
        holder.timeFri.setText(temp);
        holder.lessonFri.setText(lessonLine.lessons[4].lessonName );
        holder.locationFri.setText(lessonLine.lessons[4].city );
        holder.roomFri.setText(lessonLine.lessons[4].room );

        temp = lessonLine.lessons[5].hourStart + " - "+lessonLine.lessons[5].hourEnd;
        holder.timeSat.setText(temp);
        holder.lessonSat.setText(lessonLine.lessons[5].lessonName );
        holder.locationSat.setText(lessonLine.lessons[5].city );
        holder.roomSat.setText(lessonLine.lessons[5].room );


        temp = lessonLine.lessons[6].hourStart + " - "+lessonLine.lessons[6].hourEnd;
        holder.timeSun.setText(temp);
        holder.lessonSun.setText(lessonLine.lessons[6].lessonName );
        holder.locationSun.setText(lessonLine.lessons[6].city );
        holder.roomSun.setText(lessonLine.lessons[6].room );


        if(lessonLine.lessons[0].isLessonNow())
            holder.layoutMON.setBackground(holder.layoutMON.getContext().getDrawable(R.drawable.custom_border_highlight));
        else
            holder.layoutMON.setBackground(holder.layoutMON.getContext().getDrawable(R.drawable.custom_border_no_highlight));

        if(lessonLine.lessons[1].isLessonNow())
            holder.layoutTUE.setBackground(holder.layoutTUE.getContext().getDrawable(R.drawable.custom_border_highlight));
        else
            holder.layoutTUE.setBackground(holder.layoutTUE.getContext().getDrawable(R.drawable.custom_border_no_highlight));

        if(lessonLine.lessons[2].isLessonNow())
            holder.layoutWED.setBackground(holder.layoutWED.getContext().getDrawable(R.drawable.custom_border_highlight));
        else
            holder.layoutWED.setBackground(holder.layoutWED.getContext().getDrawable(R.drawable.custom_border_no_highlight));

        if(lessonLine.lessons[3].isLessonNow())
            holder.layoutTHU.setBackground(holder.layoutTHU.getContext().getDrawable(R.drawable.custom_border_highlight));
        else
            holder.layoutTHU.setBackground(holder.layoutTHU.getContext().getDrawable(R.drawable.custom_border_no_highlight));

        if(lessonLine.lessons[4].isLessonNow())
            holder.layoutFRI.setBackground(holder.layoutFRI.getContext().getDrawable(R.drawable.custom_border_highlight));
        else
            holder.layoutFRI.setBackground(holder.layoutFRI.getContext().getDrawable(R.drawable.custom_border_no_highlight));

        if(lessonLine.lessons[5].isLessonNow())
            holder.layoutSAT.setBackground(holder.layoutSAT.getContext().getDrawable(R.drawable.custom_border_highlight));
        else
            holder.layoutSAT.setBackground(holder.layoutSAT.getContext().getDrawable(R.drawable.custom_border_no_highlight));

        if(lessonLine.lessons[6].isLessonNow())
            holder.layoutSUN.setBackground(holder.layoutSUN.getContext().getDrawable(R.drawable.custom_border_highlight));
        else
            holder.layoutSUN.setBackground(holder.layoutSUN.getContext().getDrawable(R.drawable.custom_border_no_highlight));


    }

    @Override
    public int getItemCount() {
        return mLessonLines.size();
    }


    public class LessonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final String TAG = LessonViewHolder.class.getName();

        //create all textview and will link them to R
        public TextView timeMon;
        public TextView lessonMon;
        public TextView locationMon;
        public TextView roomMon;

        public TextView timeTue;
        public TextView lessonTue;
        public TextView locationTue;
        public TextView roomTue;

        public TextView timeWed;
        public TextView lessonWed;
        public TextView locationWed;
        public TextView roomWed;

        public TextView timeThu;
        public TextView lessonThu;
        public TextView locationThu;
        public TextView roomThu;

        public TextView timeFri;
        public TextView lessonFri;
        public TextView locationFri;
        public TextView roomFri;

        public TextView timeSat;
        public TextView lessonSat;
        public TextView locationSat;
        public TextView roomSat;

        public TextView timeSun;
        public TextView lessonSun;
        public TextView locationSun;
        public TextView roomSun;

        public LinearLayout layoutMON;
        public LinearLayout layoutTUE;
        public LinearLayout layoutWED;
        public LinearLayout layoutTHU;
        public LinearLayout layoutFRI;
        public LinearLayout layoutSAT;
        public LinearLayout layoutSUN;

        private final SparseArray<String> idToDay = new SparseArray<>();
        private final SparseArray<TextView> idToTimeTextView = new SparseArray<>();
        private final SparseArray<TextView> idToLessonTextView = new SparseArray<>();
        private final SparseArray<TextView> idToRoomTextView = new SparseArray<>();
        private final SparseArray<TextView> idToCityTextView = new SparseArray<>();

        public LessonViewHolder(View itemView) {

            super(itemView);

            //link all text view
            timeMon = itemView.findViewById(R.id.timeMon);
            lessonMon = itemView.findViewById(R.id.lessonMon);
            locationMon = itemView.findViewById(R.id.locationMon);
            roomMon = itemView.findViewById(R.id.roomMon);

            timeTue = itemView.findViewById(R.id.timeTue);
            lessonTue = itemView.findViewById(R.id.lessonTue);
            locationTue = itemView.findViewById(R.id.locationTue);
            roomTue = itemView.findViewById(R.id.roomTue);

            timeWed = itemView.findViewById(R.id.timeWed);
            lessonWed = itemView.findViewById(R.id.lessonWed);
            locationWed = itemView.findViewById(R.id.locationWed);
            roomWed = itemView.findViewById(R.id.roomWed);

            timeThu = itemView.findViewById(R.id.timeThu);
            lessonThu = itemView.findViewById(R.id.lessonThu);
            locationThu = itemView.findViewById(R.id.locationThu);
            roomThu = itemView.findViewById(R.id.roomThu);

            timeFri = itemView.findViewById(R.id.timeFri);
            lessonFri = itemView.findViewById(R.id.lessonFri);
            locationFri = itemView.findViewById(R.id.locationFri);
            roomFri = itemView.findViewById(R.id.roomFri);

            timeSat = itemView.findViewById(R.id.timeSat);
            lessonSat = itemView.findViewById(R.id.lessonSat);
            locationSat = itemView.findViewById(R.id.locationSat);
            roomSat = itemView.findViewById(R.id.roomSat);

            timeSun = itemView.findViewById(R.id.timeSun);
            lessonSun = itemView.findViewById(R.id.lessonSun);
            locationSun = itemView.findViewById(R.id.locationSun);
            roomSun = itemView.findViewById(R.id.roomSun);

            lessonMon.setSelected(true);
            lessonTue.setSelected(true);
            lessonWed.setSelected(true);
            lessonThu.setSelected(true);
            lessonFri.setSelected(true);
            lessonSat.setSelected(true);
            lessonSun.setSelected(true);

            locationMon.setSelected(true);
            locationTue.setSelected(true);
            locationWed.setSelected(true);
            locationThu.setSelected(true);
            locationFri.setSelected(true);
            locationSat.setSelected(true);
            locationSun.setSelected(true);

            roomMon.setSelected(true);
            roomTue.setSelected(true);
            roomWed.setSelected(true);
            roomThu.setSelected(true);
            roomFri.setSelected(true);
            roomSat.setSelected(true);
            roomSun.setSelected(true);

            layoutMON = itemView.findViewById(R.id.layoutMON);
            layoutTUE = itemView.findViewById(R.id.layoutTUE);
            layoutWED = itemView.findViewById(R.id.layoutWED);
            layoutTHU = itemView.findViewById(R.id.layoutTHU);
            layoutFRI = itemView.findViewById(R.id.layoutFRI);
            layoutSAT = itemView.findViewById(R.id.layoutSAT);
            layoutSUN = itemView.findViewById(R.id.layoutSUN);

            layoutMON.setOnClickListener(this);
            layoutTUE.setOnClickListener(this);
            layoutWED.setOnClickListener(this);
            layoutTHU.setOnClickListener(this);
            layoutFRI.setOnClickListener(this);
            layoutSAT.setOnClickListener(this);
            layoutSUN.setOnClickListener(this);

            layoutMON.setOnLongClickListener(this);
            layoutTUE.setOnLongClickListener(this);
            layoutWED.setOnLongClickListener(this);
            layoutTHU.setOnLongClickListener(this);
            layoutFRI.setOnLongClickListener(this);
            layoutSAT.setOnLongClickListener(this);
            layoutSUN.setOnLongClickListener(this);

            idToDay.put(R.id.layoutMON, "Mon");
            idToDay.put(R.id.layoutTUE, "Tue");
            idToDay.put(R.id.layoutWED, "Wed");
            idToDay.put(R.id.layoutTHU, "Thu");
            idToDay.put(R.id.layoutFRI, "Fri");
            idToDay.put(R.id.layoutSAT, "Sat");
            idToDay.put(R.id.layoutSUN, "Sun");

            idToTimeTextView.put(R.id.layoutMON, timeMon);
            idToTimeTextView.put(R.id.layoutTUE, timeTue);
            idToTimeTextView.put(R.id.layoutWED, timeWed);
            idToTimeTextView.put(R.id.layoutTHU, timeThu);
            idToTimeTextView.put(R.id.layoutFRI, timeFri);
            idToTimeTextView.put(R.id.layoutSAT, timeSat);
            idToTimeTextView.put(R.id.layoutSUN, timeSun);

            idToLessonTextView.put(R.id.layoutMON, lessonMon);
            idToLessonTextView.put(R.id.layoutTUE, lessonTue);
            idToLessonTextView.put(R.id.layoutWED, lessonWed);
            idToLessonTextView.put(R.id.layoutTHU, lessonThu);
            idToLessonTextView.put(R.id.layoutFRI, lessonFri);
            idToLessonTextView.put(R.id.layoutSAT, lessonSat);
            idToLessonTextView.put(R.id.layoutSUN, lessonSun);

            idToCityTextView.put(R.id.layoutMON, locationMon);
            idToCityTextView.put(R.id.layoutTUE, locationTue);
            idToCityTextView.put(R.id.layoutWED, locationWed);
            idToCityTextView.put(R.id.layoutTHU, locationThu);
            idToCityTextView.put(R.id.layoutFRI, locationFri);
            idToCityTextView.put(R.id.layoutSAT, locationSat);
            idToCityTextView.put(R.id.layoutSUN, locationSun);

            idToRoomTextView.put(R.id.layoutMON, roomMon);
            idToRoomTextView.put(R.id.layoutTUE, roomTue);
            idToRoomTextView.put(R.id.layoutWED, roomWed);
            idToRoomTextView.put(R.id.layoutTHU, roomThu);
            idToRoomTextView.put(R.id.layoutFRI, roomFri);
            idToRoomTextView.put(R.id.layoutSAT, roomSat);
            idToRoomTextView.put(R.id.layoutSUN, roomSun);

        }



        private boolean deleteItem(View v) {
            final int position = this.getLayoutPosition();
            final View view = v;
            switch (v.getId()) {
                case R.id.layoutMON:
                case R.id.layoutTUE:
                case R.id.layoutWED:
                case R.id.layoutTHU:
                case R.id.layoutFRI:
                case R.id.layoutSAT:
                case R.id.layoutSUN:
                    //when clicked on a layout, will check if the hours is not '-' --> mean lesson
                    if (!idToTimeTextView.get(v.getId()).getText().toString().equals(" - ")) {
                        //setup a popup to prevent miss-click
                        new DeleteCustomDialog(v.getContext())
                                .setCustomDialogTitle(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.delete))
                                .setCustomDialogText(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.delete_warning_message) + idToLessonTextView.get(view.getId()).getText() + "\" ?")
                                .setTextLeftButton(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.yes))
                                .setTextRightButton(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.cancel))
                                .setCustomOnClickListener( new DeleteCustomDialog.OnClickListener() {
                                    @Override
                                    public void buttonRightClick() {
                                        DataCsvManager.getInstance().deleteLine(view.getContext(), idToDay.get(view.getId()), position);
                                    }

                                    @Override
                                    public void buttonLeftClick() {

                                    }
                                })
                                .show();
                    }
                    return true;
            }
            return false;
        }

        private boolean modifyItem(View v) {
            final int position = this.getLayoutPosition();
            switch (v.getId()) {
                case R.id.layoutMON:
                case R.id.layoutTUE:
                case R.id.layoutWED:
                case R.id.layoutTHU:
                case R.id.layoutFRI:
                case R.id.layoutSAT:
                case R.id.layoutSUN:
                    //when clicked on a layout, will check if the hours is not '-' --> mean lesson
                    if (!idToTimeTextView.get(v.getId()).getText().toString().equals(" - ")) {
                        String lesson = idToLessonTextView.get(v.getId()).getText().toString();
                        String hour = idToTimeTextView.get(v.getId()).getText().toString().replace(" ", "");
                        String startHour = hour.split("-")[0];
                        String endHour = hour.split("-")[1];
                        String city = idToCityTextView.get(v.getId()).getText().toString();
                        String room = idToRoomTextView.get(v.getId()).getText().toString();
                        String day = idToDay.get(v.getId());
                        AddLessonDialog addLessonDialog = new AddLessonDialog(v.getContext());
                        addLessonDialog.initEditText(day, lesson, startHour, endHour, city, room, position);
                        addLessonDialog.show();

                    }
            }
            return true;
        }


        private boolean duplicateItem(View v) {
            switch (v.getId()) {
                case R.id.layoutMON:
                case R.id.layoutTUE:
                case R.id.layoutWED:
                case R.id.layoutTHU:
                case R.id.layoutFRI:
                case R.id.layoutSAT:
                case R.id.layoutSUN:
                    //when clicked on a layout, will check if the hours is not '-' --> mean lesson
                    if (!idToTimeTextView.get(v.getId()).getText().toString().equals(" - ")) {
                        String lesson = idToLessonTextView.get(v.getId()).getText().toString();
                        String hour = idToTimeTextView.get(v.getId()).getText().toString().replace(" ", "");
                        String startHour = hour.split("-")[0];
                        String endHour = hour.split("-")[1];
                        String city = idToCityTextView.get(v.getId()).getText().toString();
                        String room = idToRoomTextView.get(v.getId()).getText().toString();
                        String day = idToDay.get(v.getId());
                        AddLessonDialog addLessonDialog = new AddLessonDialog(v.getContext());
                        addLessonDialog.initEditText(day, lesson, startHour, endHour, city, room);
                        addLessonDialog.show();

                    }
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            showPopupFullItem(v);
        }

        @Override
        public boolean onLongClick(View v) {
            showPopupFullItem(v);
            return true;
        }

        void showPopupFullItem(View v)
        {
            if (!idToTimeTextView.get(v.getId()).getText().toString().equals(" - ")) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                //inflating menu from xml resource
                popup.inflate(R.menu.context_menu_lesson);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.contextDelete:
                            return deleteItem(v);
                        case R.id.contextDuplicate:
                            return duplicateItem(v);
                        case R.id.contextModify:
                            return modifyItem(v);
                        default:
                            return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        }
    }


}
