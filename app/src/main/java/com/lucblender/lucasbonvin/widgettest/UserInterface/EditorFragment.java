package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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

    private FloatingActionMenu floatingActionMenu;

    private LessonAdapter lessonAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get the view from layout
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.editorlayout, container, false);

        //init recyclerview and fill the lessonlines ArrayList with createLessonLineFromCSV
        RecyclerView rv = rootView.findViewById(R.id.recyclerView);

        FloatingActionButton fabHelp = rootView.findViewById(R.id.floating_menu_Help);
        FloatingActionButton fabAdd = rootView.findViewById(R.id.floating_menu_add);
        FloatingActionButton fabDeleteAll = rootView.findViewById(R.id.floating_menu_deleteAll);
        FloatingActionButton fabNewFile = rootView.findViewById(R.id.floating_menu_create);
        floatingActionMenu = rootView.findViewById(R.id.floating_menu);

        createLessonLineFromCSV();
        //create adapter for recycler view and link them
        lessonAdapter = new LessonAdapter(mLessonLines);
        rv.setAdapter(lessonAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        final TypedValue value = new TypedValue ();
        getContext().getTheme().resolveAttribute (android.R.attr.textColorPrimary, value, true);
        floatingActionMenu.getMenuIconView().setColorFilter(value.data);

        Drawable drawableHelp = DrawableCompat.wrap(getContext().getResources().getDrawable(R.mipmap.ic_help));
        DrawableCompat.setTint(drawableHelp, value.data);

        Drawable drawableDelete = DrawableCompat.wrap(getContext().getResources().getDrawable(R.mipmap.ic_delete));
        DrawableCompat.setTint(drawableDelete, value.data);

        Drawable drawableNew = DrawableCompat.wrap(getContext().getResources().getDrawable(R.mipmap.ic_new_file));
        DrawableCompat.setTint(drawableNew, value.data);

        Drawable drawableAdd = DrawableCompat.wrap(getContext().getResources().getDrawable(R.drawable.fab_add));
        DrawableCompat.setTint(drawableAdd, value.data);

        fabHelp.setImageDrawable(drawableHelp);
        fabDeleteAll.setImageDrawable(drawableDelete);
        fabNewFile.setImageDrawable(drawableNew);
        fabAdd.setImageDrawable(drawableAdd);


        fabHelp.setOnClickListener(this);
        fabAdd.setOnClickListener(this);
        fabDeleteAll.setOnClickListener(this);
        fabNewFile.setOnClickListener(this);

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
        if(v.getId() == R.id.floating_menu_add)
        {
            floatingActionMenu.close(true);
            AddLessonDialog addLessonDialog = new AddLessonDialog(getActivity());
            addLessonDialog.show();
        }
        else if(v.getId() == R.id.floating_menu_deleteAll)
        {
            floatingActionMenu.close(true);

            new DeleteCustomDialog(v.getContext())
                    .setCustomDialogTitle(v.getContext().getString(R.string.delete))
                    .setCustomDialogText(v.getContext().getString(R.string.delete_all_warning))
                    .setTextLeftButton(v.getContext().getString(R.string.yes))
                    .setTextRightButton(v.getContext().getString(R.string.cancel))
                    .setCustomOnClickListener(new DeleteCustomDialog.OnClickListener() {
                        @Override
                        public void buttonRightClick() {
                            if(!DataCsvManager.getInstance().deleteAll(getContext()))
                                message(v.getContext().getString(R.string.delete_all_error));
                        }

                        @Override
                        public void buttonLeftClick() {

                        }
                    }).show();

        }
        else if(v.getId() == R.id.floating_menu_Help)
        {
            floatingActionMenu.close(true);
            //create a popup with help message
            CustomLayoutSimpleDialog helpDialog = new CustomLayoutSimpleDialog(getActivity(), R.layout.helpdialog);
            helpDialog.show();

        }
        else if(v.getId() == R.id.floating_menu_create)
        {
            floatingActionMenu.close(true);
            //create a popup with help message
            NewBlankFileDialog newBlankFileDialog = new NewBlankFileDialog(getActivity());
            newBlankFileDialog.show();
        }
    }


    //show a toast message
    void message(String s)
    {
        Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
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
