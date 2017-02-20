package com.lukasblakk.groundhogtodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.lukasblakk.groundhogtodo.R;

import static android.text.style.TtsSpan.ARG_TEXT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItemFragment.OnAddItemListener} interface
 * to handle interaction events.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends DialogFragment implements OnEditorActionListener {

    private EditText mAddText;

    // Defines the listener interface with a method passing back data result.
    public interface OnAddItemListener {
        void onFinishAddDialog(String text, String dueDate, String repeats);
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
        // Show soft keyboard automatically and request focus to field
        mAddText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // callback when the "Done" button is pressed on keyboard
        mAddText.setOnEditorActionListener(this);
    }


    // Fires whenever the EditText has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            if(TextUtils.isEmpty(mAddText.getText().toString())) {
                mAddText.setError("Item cannot be empty");
                return false;
            }
            // TODO Get & set the calendar date here
            String dueDate = "today";
            // TODO Get the repeats daily value
            String repeats = "true";
            // Return input text back to activity through the implemented listener
            AddItemFragment.OnAddItemListener listener = (AddItemFragment.OnAddItemListener) getActivity();
            // TODO send the date & repeat values and adjust the method to accept
            listener.onFinishAddDialog(mAddText.getText().toString(), dueDate, repeats);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }
}
