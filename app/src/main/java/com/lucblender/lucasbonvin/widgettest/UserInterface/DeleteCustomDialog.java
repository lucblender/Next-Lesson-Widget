package com.lucblender.lucasbonvin.widgettest.UserInterface;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lucblender.lucasbonvin.widgettest.R;

public class DeleteCustomDialog  extends Dialog {

    private String title;
    private String text;
    private String textLeftButton;
    private String textRightButton;

    public interface OnClickListener{
        void buttonRightClick();
        void buttonLeftClick();
    }

    private OnClickListener onClickListener;

    public DeleteCustomDialog(Context context){
        super(context);
    }

    public DeleteCustomDialog setCustomDialogTitle(String title)
    {
        this.title = title;
        return this;
    }

    public DeleteCustomDialog setCustomDialogText(String text)
    {
        this.text = text;
        return this;
    }

    public DeleteCustomDialog setTextLeftButton(String textLeftButton)
    {
        this.textLeftButton = textLeftButton;
        return this;
    }

    public DeleteCustomDialog setTextRightButton(String textRightButton)
    {
        this.textRightButton = textRightButton;
        return this;
    }

    public DeleteCustomDialog setCustomOnClickListener(OnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.deletecustomdialog);

        TextView textViewTitle = findViewById(R.id.deleteTextViewTitle);
        TextView textViewMessage = findViewById(R.id.deleteTextViewMessage);
        Button buttonRight = findViewById(R.id.deleteButtonRight);
        Button buttonLeft = findViewById(R.id.deleteButtonLeft);

        textViewTitle.setText(title);
        textViewMessage.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        buttonRight.setText(textLeftButton);
        buttonLeft.setText(textRightButton);

        buttonLeft.setOnClickListener(v -> {
            onClickListener.buttonLeftClick();
            dismiss();
        });

        buttonRight.setOnClickListener(v -> {
            onClickListener.buttonRightClick();
            dismiss();
        });
    }
}