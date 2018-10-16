package com.lucblender.lucasbonvin.widgettest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.lucblender.lucasbonvin.widgettest.Data.DataCsvManager;
import com.lucblender.lucasbonvin.widgettest.Data.LessonLine;
import com.lucblender.lucasbonvin.widgettest.UserInterface.AddLessonDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


    public class LessonViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener {
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

        private final Map<Integer, String> idToDay = new HashMap<>();
        private final Map<Integer, TextView> idToTimeTextView = new HashMap<>();
        private final Map<Integer, TextView> idToLessonTextView = new HashMap<>();
        private final Map<Integer, TextView> idToRoomTextView = new HashMap<>();
        private final Map<Integer, TextView> idToCityTextView = new HashMap<>();

        public LessonViewHolder(View itemView) {

            super(itemView);

            //link all text view
            timeMon = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.timeMon);
            lessonMon = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.lessonMon);
            locationMon = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.locationMon);
            roomMon = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.roomMon);

            timeTue = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.timeTue);
            lessonTue = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.lessonTue);
            locationTue = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.locationTue);
            roomTue = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.roomTue);

            timeWed = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.timeWed);
            lessonWed = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.lessonWed);
            locationWed = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.locationWed);
            roomWed = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.roomWed);

            timeThu = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.timeThu);
            lessonThu = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.lessonThu);
            locationThu = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.locationThu);
            roomThu = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.roomThu);

            timeFri = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.timeFri);
            lessonFri = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.lessonFri);
            locationFri = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.locationFri);
            roomFri = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.roomFri);

            timeSat = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.timeSat);
            lessonSat = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.lessonSat);
            locationSat = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.locationSat);
            roomSat = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.roomSat);

            timeSun = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.timeSun);
            lessonSun = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.lessonSun);
            locationSun = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.locationSun);
            roomSun = (TextView) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.roomSun);

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

            layoutMON = (LinearLayout) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.layoutMON);
            layoutTUE = (LinearLayout) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.layoutTUE);
            layoutWED = (LinearLayout) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.layoutWED);
            layoutTHU = (LinearLayout) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.layoutTHU);
            layoutFRI = (LinearLayout) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.layoutFRI);
            layoutSAT = (LinearLayout) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.layoutSAT);
            layoutSUN = (LinearLayout) itemView.findViewById(com.lucblender.lucasbonvin.widgettest.R.id.layoutSUN);

            layoutMON.setOnLongClickListener(this);
            layoutTUE.setOnLongClickListener(this);
            layoutWED.setOnLongClickListener(this);
            layoutTHU.setOnLongClickListener(this);
            layoutFRI.setOnLongClickListener(this);
            layoutSAT.setOnLongClickListener(this);
            layoutSUN.setOnLongClickListener(this);


            layoutMON.setOnCreateContextMenuListener(this);
            layoutTUE.setOnCreateContextMenuListener(this);
            layoutWED.setOnCreateContextMenuListener(this);
            layoutTHU.setOnCreateContextMenuListener(this);
            layoutFRI.setOnCreateContextMenuListener(this);
            layoutSAT.setOnCreateContextMenuListener(this);
            layoutSUN.setOnCreateContextMenuListener(this);

            idToDay.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutMON, "Mon");
            idToDay.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTUE, "Tue");
            idToDay.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutWED, "Wed");
            idToDay.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTHU, "Thu");
            idToDay.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutFRI, "Fri");
            idToDay.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSAT, "Sat");
            idToDay.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSUN, "Sun");

            idToTimeTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutMON, timeMon);
            idToTimeTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTUE, timeTue);
            idToTimeTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutWED, timeWed);
            idToTimeTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTHU, timeThu);
            idToTimeTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutFRI, timeFri);
            idToTimeTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSAT, timeSat);
            idToTimeTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSUN, timeSun);

            idToLessonTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutMON, lessonMon);
            idToLessonTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTUE, lessonTue);
            idToLessonTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutWED, lessonWed);
            idToLessonTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTHU, lessonThu);
            idToLessonTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutFRI, lessonFri);
            idToLessonTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSAT, lessonSat);
            idToLessonTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSUN, lessonSun);

            idToCityTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutMON, locationMon);
            idToCityTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTUE, locationTue);
            idToCityTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutWED, locationWed);
            idToCityTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTHU, locationThu);
            idToCityTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutFRI, locationFri);
            idToCityTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSAT, locationSat);
            idToCityTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSUN, locationSun);

            idToRoomTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutMON, roomMon);
            idToRoomTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTUE, roomTue);
            idToRoomTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutWED, roomWed);
            idToRoomTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutTHU, roomThu);
            idToRoomTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutFRI, roomFri);
            idToRoomTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSAT, roomSat);
            idToRoomTextView.put(com.lucblender.lucasbonvin.widgettest.R.id.layoutSUN, roomSun);

        }


        @Override
        public boolean onLongClick(View v) {

            if (!idToTimeTextView.get(v.getId()).getText().toString().equals(" - ")) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                //inflating menu from xml resource
                popup.inflate(R.menu.context_menu_lesson);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
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
                    }
                });
                //displaying the popup
                popup.show();
            }
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "SMS");
        }

        public boolean deleteItem(View v) {
            final int position = this.getLayoutPosition();
            final View view = v;
            switch (v.getId()) {
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutMON:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutTUE:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutWED:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutTHU:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutFRI:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutSAT:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutSUN:
                    //when clicked on a layout, will check if the hours is not '-' --> mean lesson
                    if (!idToTimeTextView.get(v.getId()).getText().toString().equals(" - ")) {
                        //setup a popup to prevent miss-click
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.MyDialogTheme);
                        builder.setTitle(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.delete));
                        builder.setMessage(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.delete_warning_message) + idToLessonTextView.get(view.getId()).getText() + "\" ?");
                        builder.setPositiveButton(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DataCsvManager.getInstance().deleteLine(view.getContext(), idToDay.get(view.getId()), position);
                            }
                        });
                        builder.setNegativeButton(v.getContext().getString(com.lucblender.lucasbonvin.widgettest.R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        builder.show();
                    }
                    return true;
            }
            return false;
        }

        public boolean modifyItem(View v) {
            final int position = this.getLayoutPosition();
            switch (v.getId()) {
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutMON:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutTUE:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutWED:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutTHU:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutFRI:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutSAT:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutSUN:
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


        public boolean duplicateItem(View v) {
            switch (v.getId()) {
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutMON:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutTUE:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutWED:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutTHU:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutFRI:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutSAT:
                case com.lucblender.lucasbonvin.widgettest.R.id.layoutSUN:
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
    }

}
