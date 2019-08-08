package com.example.kilojoule;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class diary extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] allFiles;
    ArrayList<String> savedFiles=new ArrayList<String>();
    /**
     * onCreate Method triggers the showSavedFiles method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        listView= findViewById(R.id.listview);
        showSavedFiles();
    }

    /**
     * Gets all the files in the app directory and writes it to an array.
     * An array list is then created which goes through the array and adds all the files regarding the entries.
     * Specified by "ID"
     * These files are then added to the list view using the adapter class
     * Upon click of an item in the list view, the application navigates you to a the Entries page for the specific Date
     * Uses intent to send the arraylist, the date needed and its current index to the Entries activities
     */
    public void showSavedFiles(){
        allFiles=getApplicationContext().fileList();
        for(String i: allFiles){
            if(i.contains("ID: ")){
                savedFiles.add(i);
            }
        }
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,savedFiles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), Entries.class);
                intent.putExtra("FILENAME",savedFiles.get(i));
                intent.putExtra("FILEARRAY",savedFiles);
                intent.putExtra("CURRENTINDEX",i);
                intent.putExtra("CHECK",0);
                startActivity(intent);
            }
        });
    }
    /**
     * Method for the back button to go to the previous page
     * @param view
     */
    public void launchPrevousPage(View view) {
        finish();
    }
}
