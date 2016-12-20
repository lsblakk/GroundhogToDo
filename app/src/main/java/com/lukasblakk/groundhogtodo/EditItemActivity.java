package com.lukasblakk.groundhogtodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String text = getIntent().getStringExtra("text");
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(text);
        etEditItem.setSelection(text.length());
        etEditItem.isFocused();
    }

    public void onSave(View v) {
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        int pos = getIntent().getIntExtra("position", 0);
        Intent data = new Intent();
        data.putExtra("text", etEditItem.getText().toString());
        data.putExtra("code", 100);
        data.putExtra("position", pos);
        Log.d("pos in onSave", "Value: " + pos);
        setResult(RESULT_OK, data);
        this.finish();
    }
}
