package com.lukasblakk.groundhogtodo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.lukasblakk.groundhogtodo.models.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by lukas on 1/16/17.
 */

public class TodoItemsDatabaseHelper extends SQLiteOpenHelper {
    private static TodoItemsDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "todoItemsDatabase";
    private static final int DATABASE_VERSION = 3;

    // Tables
    private static final String TABLE_ITEMS = "items";

    // Items Table Columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TEXT = "text";
    private static final String KEY_ITEM_DUE_DATE = "dueDate";

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_TEXT + " TEXT," +
                KEY_ITEM_DUE_DATE + " TEXT," +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    // Using the Singleton pattern to avoid memory leaks
    public static synchronized TodoItemsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodoItemsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private TodoItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Insert a todo item into the database
    public long addOrUpdateItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        long itemId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TEXT, item.text);
            values.put(KEY_ITEM_DUE_DATE, item.dueDate);

            // First try to update the item in case the user already exists in the database
            // This assumes item text content is unique
            // TODO - what else could be used to identify items as unique?  Possibly track creation time?
            int rows = db.update(TABLE_ITEMS, values, KEY_ITEM_TEXT + "= ?", new String[]{item.text});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the item we just updated
                String itemSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_ITEM_ID, TABLE_ITEMS, KEY_ITEM_TEXT);
                // TODO need to include all the things that are being updated here
                Cursor cursor = db.rawQuery(itemSelectQuery, new String[]{String.valueOf(item.text)});
                try {
                    if (cursor.moveToFirst()) {
                        itemId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // item with this content did not already exist, so insert new item
                itemId = db.insertOrThrow(TABLE_ITEMS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update item");
        } finally {
            db.endTransaction();
        }
        return itemId;
    }


    public ArrayList<Item> getAllItems() {
       ArrayList<Item> items = new ArrayList<>();

        // SELECT * FROM ITEMS
        String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_ITEMS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item newItem = new Item();
                    newItem.text = cursor.getString(cursor.getColumnIndex(KEY_ITEM_TEXT));
                    newItem.dueDate = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DUE_DATE));
                    items.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    // Update item text
    public int updateItem(Item item, String updatedText, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_TEXT, updatedText);
        values.put(KEY_ITEM_DUE_DATE, dueDate);

        return db.update(TABLE_ITEMS, values, KEY_ITEM_TEXT + " = ?",
                new String[] { String.valueOf(item.text) });
    }

    // Delete a particular item
    public boolean deleteItem(Item deleteItem)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Boolean success = false;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TEXT, deleteItem.text);
            int rows = db.update(TABLE_ITEMS, values, KEY_ITEM_TEXT + "= ?", new String[]{deleteItem.text});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the item we just updated
                String itemSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_ITEM_ID, TABLE_ITEMS, KEY_ITEM_TEXT);
                // TODO need to include all the things that are being updated here
                Cursor cursor = db.rawQuery(itemSelectQuery, new String[]{String.valueOf(deleteItem.text)});
                try {
                    if (cursor.moveToFirst()) {
                        long itemId = cursor.getInt(0);
                        String whereClause = "id=?";
                        String[] whereArgs = new String[] { String.valueOf(itemId) };
                        success = db.delete(TABLE_ITEMS, whereClause, whereArgs) > 0;
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete item");
        } finally {
            db.endTransaction();
        }
        return success;
    }

}