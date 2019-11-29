package com.example.dbtestproject

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: FeedReaderDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        dbHelper = FeedReaderDbHelper(this)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            Thread {
                for (i in 1..5) {
                    insert()
                }
            }.start()
            Thread {
                for (i in 1..5) {
                    insert()
                }
            }.start()
        }
    }

    private fun insert() {
        Log.e(TAG, "insert() " + Thread.currentThread().name)
        // Gets the data repository in write mode
        val db = dbHelper.readableDatabase
        try {
// Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                    "Title: " + Random.nextInt(0, 1000) + " " + Thread.currentThread().name
                )
                put(
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE,
                    "Subtitle: " + Random.nextInt(0, 1000) + " " + Thread.currentThread().name
                )
            }

// Insert the new row, returning the primary key value of the new row
            val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Log.e(TAG, "insert() " + e.message)
        } finally {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    fun viewData(view: View) {
        val db = dbHelper.readableDatabase

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        val projection = arrayOf(
//                BaseColumns._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
            FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
        )

// Filter results WHERE "title" = 'My Title'
        val selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE

// How you want the results sorted in the resulting Cursor
        val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE} DESC"

        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        cursor.use {
            it ?: return
            if (it.count >= 0) {
                it.moveToFirst()
                do {
                    Log.e(TAG, "${it.getString(0)} >>> ${it.getString(1)}")
                } while (cursor.moveToNext())

            }

        }
    }
}
