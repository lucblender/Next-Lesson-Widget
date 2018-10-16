package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucblender.lucasbonvin.widgettest.Data.DataCsvManager;
import com.lucblender.lucasbonvin.widgettest.Data.LessonLine;
import com.lucblender.lucasbonvin.widgettest.LessonAdapter;
import com.lucblender.lucasbonvin.widgettest.R;
import com.lucblender.lucasbonvin.widgettest.UpdateService;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class EditorFragment extends Fragment implements View.OnClickListener, Observer{

    private static final String TAG = EditorFragment.class.getName();

    //array of lessons line that gonna be displayed by the recycler view
    private ArrayList<LessonLine> mLessonLines = new ArrayList<>();

    private RecyclerView rv;
    private LessonAdapter lessonAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get the view from layout
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.editorlayout, container, false);

        //init recyclerview and fill the lessonlines ArrayList with createLessonLineFromCSV
        rv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        createLessonLineFromCSV();
        //create adapter for recycler view and link them
        lessonAdapter = new LessonAdapter(mLessonLines);
        rv.setAdapter(lessonAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        //floating button onclick
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DataCsvManager.getInstance().addObserver(this);

        return rootView;
    }

    @Override
    public void onDetach() {

        DataCsvManager.getInstance().deleteObserver(this);
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //when fragment is visible (cause of slider activity), update the lessonlines ArrayList
        if (isVisibleToUser) {
            createLessonLineFromCSV();
            lessonAdapter.notifyDataSetChanged();
        }
    }


    private void createLessonLineFromCSV() {
       mLessonLines.clear();
       mLessonLines.addAll(DataCsvManager.getInstance().getArrayLesson(getContext()));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            AddLessonDialog addLessonDialog = new AddLessonDialog(getActivity());
            addLessonDialog.show();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        createLessonLineFromCSV();
        lessonAdapter.notifyDataSetChanged();
        try {
            getActivity().startService(new Intent(getActivity(), UpdateService.class));
        }
            catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
