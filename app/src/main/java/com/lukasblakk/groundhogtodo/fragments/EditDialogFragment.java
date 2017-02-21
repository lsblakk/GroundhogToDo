package com.lukasblakk.groundhogtodo.fragments;

/**
 * Created by lukas on 1/9/17.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.view.View.OnClickListener;

import com.lukasblakk.groundhogtodo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EditDialogFragment extends DialogFragment implements OnClickListener {

    private static final String ARG_TEXT = "text";
    private static final String ARG_POS = "pos";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DUE_DATE = "dueDate";

    private String text;
    private int pos;
    private String dueDate;
    private EditText mEditText;
    private DatePicker mDatePicker;
    private Calendar mCalendar;
    private Button updateBtn = null;


    // Handling Integer for calendar dates

    public static final String DATE_FORMAT = "YYYY-MM-DD";
    private static final SimpleDateFormat dateFormat = new
            SimpleDateFormat(DATE_FORMAT);

    // Defines the listener interface with a method passing back data result.
    public interface EditDialogListener {
        void onFinishEditDialog(String origText, String editedText, String dueDate, int pos);
    }


    public EditDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param text Text of the item to be edited.
     * @param pos Position of the item in the ListView from MainActivity.
     * @title title Text to set as title for the fragment
     * @return A new instance of fragment EditFragment.
     */
    public static EditDialogFragment newInstance(String title, String text, String dueDate, int pos) {
        EditDialogFragment frag = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_POS, pos);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DUE_DATE, dueDate);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.txt_edit_item);
        mDatePicker = (DatePicker) view.findViewById(R.id.dpEditItem);
        // Fetch arguments from bundle and set edit text field
        if (getArguments() != null) {
            text = getArguments().getString(ARG_TEXT);
            dueDate = getArguments().getString(ARG_DUE_DATE);
            pos = getArguments().getInt(ARG_POS);
        }
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        mEditText.setText(text);
        mEditText.setSelection(text.length());
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Set the calendar to the saved value
        // Use the current date as the default values for the picker
        mCalendar = null;
        if (dueDate.equals("today")) {
            mCalendar = Calendar.getInstance();
        } else {
            try {
                mCalendar.setTime(dateFormat.parse(dueDate));
            } catch (ParseException e){
                mCalendar = Calendar.getInstance();
            }
        }
        mDatePicker.updateDate(mCalendar.YEAR, mCalendar.MONTH, mCalendar.DAY_OF_MONTH);

        // Activate the button listener
        updateBtn = (Button) view.findViewById(R.id.btnUpdate);
        updateBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(TextUtils.isEmpty(mEditText.getText().toString())) {
            mEditText.setError("Item cannot be empty");
        }
        // Get the dueDate from date picker
        mDatePicker = (DatePicker) view.findViewById(R.id.dpNewItem);
        Calendar c = Calendar.getInstance();
        int day;
        int month;
        int year;
        if (mDatePicker != null) {
            day = mDatePicker.getDayOfMonth();
            month = mDatePicker.getMonth();
            year =  mDatePicker.getYear();
        } else {
            year = c.YEAR;
            month = c.MONTH;
            day = c.DAY_OF_MONTH;
        }
        c.set(year, month, day);
        dueDate = c.toString();
        // Return input text back to activity through the implemented listener
        EditDialogListener listener = (EditDialogListener) getActivity();
        listener.onFinishEditDialog(text, mEditText.getText().toString(), dueDate, pos);
        // Close the dialog and return back to the parent activity
        dismiss();
    }

}