package com.lukasblakk.groundhogtodo.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import com.lukasblakk.groundhogtodo.adapters.ItemsAdapter;
import com.lukasblakk.groundhogtodo.data.TodoItemsDatabaseHelper;
import com.lukasblakk.groundhogtodo.fragments.EditDialogFragment;
import com.lukasblakk.groundhogtodo.fragments.EditDialogFragment.EditDialogListener;
import com.lukasblakk.groundhogtodo.R;
import com.lukasblakk.groundhogtodo.models.Item;


public class MainActivity extends AppCompatActivity implements EditDialogListener {
    ArrayList<Item> items = new ArrayList<>();
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    TodoItemsDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ItemsAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        helper = TodoItemsDatabaseHelper.getInstance(this);
        setupListViewListener();
        setupEditViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                Item deleteItem = (Item) lvItems.getItemAtPosition(pos);
                Boolean deleted = helper.deleteItem(deleteItem);
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return deleted;
            }
        });
    }

    private void setupEditViewListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        FragmentManager fm = getSupportFragmentManager();
                        Item editItem = (Item) lvItems.getItemAtPosition(pos);
                        EditDialogFragment editDialogFragment = EditDialogFragment.newInstance("Edit Item", editItem.text, pos);
                        editDialogFragment.show(fm, "fragment_edit");
                    }
                }
        );
    }

    private void readItems() {
        helper = TodoItemsDatabaseHelper.getInstance(this);
        items = helper.getAllItems();
    }

    private void writeItems() {
        helper = TodoItemsDatabaseHelper.getInstance(this);
        for (Item item : items) {
            helper.addOrUpdateItem(item);
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        if(TextUtils.isEmpty(etNewItem.getText().toString())) {
            etNewItem.setError("Item cannot be empty");
            return;
        }
        Item newItem = new Item();
        newItem.text = etNewItem.getText().toString();
        itemsAdapter.add(newItem);
        writeItems();
        etNewItem.setText("");
    }

    // This method is invoked in the activity when the listener is triggered
    @Override
    public void onFinishEditDialog(String origText, String editedText, int pos) {
        Log.d("pos in onActivityResult", "Value: " + pos);
        Item originalItem = new Item(origText);
        helper.updateItemText(originalItem, editedText);
        Item editedItem = new Item(editedText);
        items.set(pos, editedItem);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }


}
