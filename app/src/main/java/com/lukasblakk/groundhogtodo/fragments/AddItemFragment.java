package com.lukasblakk.groundhogtodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItemFragment.OnAddItemListener} interface
 * to handle interaction events.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends DialogFragment implements OnClickListener {

    private EditText mAddText;
    private DatePicker mDatePicker;

    private Button saveBtn = null;

    // Handling Integer for calendar dates

    public static final String DATE_FORMAT = "yyyyMMdd";
    private static final SimpleDateFormat dateFormat = new
            SimpleDateFormat(DATE_FORMAT);

    public static long formatDateAsLong(Calendar cal){
        return Long.parseLong(dateFormat.format(cal.getTime()));
    }

    public static Calendar getCalendarFromFormattedLong(long l){
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormat.parse(String.valueOf(l)));
            return c;

        } catch (ParseException e) {
            return null;
        }
    }

    // Defines the listener interface with a method passing back data result.
    public interface OnAddItemListener {
        void onFinishAddDialog(String text, Integer dueDate);
    }

    public AddItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment AddItemFragment.
     */
    public static AddItemFragment newInstance() {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mAddText = (EditText) view.findViewById(R.id.txt_new_item);
        mDatePicker = (DatePicker) view.findViewById(R.id.dpNewItem);
        // Show soft keyboard automatically and request focus to field
        mAddText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // Use the current date as the default values for the picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        mDatePicker.updateDate(year, month, day);
        // Activate the button listener
        saveBtn = (Button) view.findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(TextUtils.isEmpty(mAddText.getText().toString())) {
            mAddText.setError("Item cannot be empty");
        }
        // TODO Get & set the calendar date here
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
        Integer dueDate = (int) formatDateAsLong(c);
        // Return input text back to activity through the implemented listener
        AddItemFragment.OnAddItemListener listener = (AddItemFragment.OnAddItemListener) getActivity();
        // TODO send the date & repeat values and adjust the method to accept
        listener.onFinishAddDialog(mAddText.getText().toString(), dueDate);
        // Close the dialog and return back to the parent activity
        dismiss();
    }
}
