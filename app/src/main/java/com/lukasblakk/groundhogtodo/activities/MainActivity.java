package com.lukasblakk.groundhogtodo.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import com.lukasblakk.groundhogtodo.adapters.ItemsAdapter;
import com.lukasblakk.groundhogtodo.data.TodoItemsDatabaseHelper;
import com.lukasblakk.groundhogtodo.fragments.AddItemFragment;
import com.lukasblakk.groundhogtodo.fragments.EditDialogFragment;
import com.lukasblakk.groundhogtodo.fragments.EditDialogFragment.EditDialogListener;
import com.lukasblakk.groundhogtodo.fragments.AddItemFragment.OnAddItemListener;
import com.lukasblakk.groundhogtodo.R;
import com.lukasblakk.groundhogtodo.models.Item;


public class MainActivity extends AppCompatActivity implements EditDialogListener, OnAddItemListener {
    ArrayList<Item> items = new ArrayList<>();
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    TodoItemsDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ItemsAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        helper = TodoItemsDatabaseHelper.getInstance(this);
        setSupportActionBar(toolbar);
        setupListViewListener();
        setupEditViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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
                        EditDialogFragment editDialogFragment = EditDialogFragment.newInstance("Edit Item", editItem.text, editItem.dueDate, pos);
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

    public void onAddAction(MenuItem mi) {
        FragmentManager fm = getSupportFragmentManager();
        AddItemFragment addItemFragment = AddItemFragment.newInstance();
        addItemFragment.show(fm, "fragment_add_item");

    }

    // This method is invoked in the activity when the listener is triggered
    @Override
    public void onFinishEditDialog(String origText, String editedText, String dueDate, int pos) {
        Log.d("pos in onActivityResult", "Value: " + pos);
        Item originalItem = new Item(origText);
        helper.updateItem(originalItem, editedText, dueDate);
        Item editedItem = new Item(editedText, dueDate);
        items.set(pos, editedItem);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }

    // This method is invoked in the activity when the listener is triggered
    @Override
    public void onFinishAddDialog(String text, String dueDate) {
        Item newItem = new Item();
        newItem.text = text;
        newItem.dueDate = dueDate;
        itemsAdapter.add(newItem);
        writeItems();
    }

}
