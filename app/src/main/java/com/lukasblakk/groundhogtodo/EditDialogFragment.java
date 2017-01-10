package com.lukasblakk.groundhogtodo;

/**
 * Created by lukas on 1/9/17.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class EditDialogFragment extends DialogFragment implements OnEditorActionListener {

    private static final String ARG_TEXT = "text";
    private static final String ARG_POS = "pos";
    private static final String ARG_TITLE = "title";

    private String text;
    private int pos;
    private EditText mEditText;



    // Defines the listener interface with a method passing back data result.
    public interface EditDialogListener {
        void onFinishEditDialog(String text, int pos);
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
    public static EditDialogFragment newInstance(String title, String text, int pos) {
        EditDialogFragment frag = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_POS, pos);
        args.putString(ARG_TITLE, title);
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
        // Fetch arguments from bundle and set edit text field
        if (getArguments() != null) {
            text = getArguments().getString(ARG_TEXT);
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
        // callback when the "Done" button is pressed on keyboard
        mEditText.setOnEditorActionListener(this);

    }

    // Fires whenever the EditText has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            EditDialogListener listener = (EditDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString(), pos);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

}