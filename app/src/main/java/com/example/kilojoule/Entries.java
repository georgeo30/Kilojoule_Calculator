package com.example.kilojoule;


import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Entries extends AppCompatActivity {
    TextView textView;
    TextView header;
    ArrayList<String> nextEntry=new ArrayList<String>();
    int current;
    int arrayLen;
    ImageButton next;
    ImageButton prev;
    @Override
    /**
     * the onCreate method for the Entries activity first checks whether it is being opened from the calculator page or the list view
     * if from calculator: It doesnt let you navigate between entries
     * if from listview:it lets you navigate between entries by using intent to receive the current file, its index and the whole arrayList containing the files.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);
        textView= findViewById(R.id.file_details);
        header= findViewById(R.id.filename_id);
        next=(ImageButton) findViewById(R.id.imageButton);
        prev=(ImageButton) findViewById(R.id.imageButton2);
        if(getIntent().getExtras().getInt("CHECK")==1) {
            String filename ="ID: "+ getIntent().getStringExtra("FILENAME");
            loadFile(filename);
            next=(ImageButton) findViewById(R.id.imageButton);
            next.setVisibility(View.GONE);
            prev=(ImageButton) findViewById(R.id.imageButton2);
            prev.setVisibility(View.GONE);
        }
        else {
            String filename = getIntent().getStringExtra("FILENAME");
            loadFile(filename);
            nextEntry = (ArrayList<String>) getIntent().getSerializableExtra("FILEARRAY");
            current = getIntent().getExtras().getInt("CURRENTINDEX");
            arrayLen = nextEntry.size();
            if(current==0){
                prev.setVisibility(View.INVISIBLE);
            }
            if(current==arrayLen-1){
                next.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * The load file method is used to read in the data within the file
     * The filename is being sent it from the onCreate
     * the data is then written to a textView
     * @param filename
     */
    public void loadFile(String filename){

        try {
            header.setText(filename);
            FileInputStream fis = openFileInput(filename );
            BufferedReader reader =new BufferedReader(new InputStreamReader(new DataInputStream(fis)));

            String line;
            while((line=reader.readLine())!=null){
                textView.append(line);
                textView.append("\n");
            }
            fis.close();
        }
        catch(Exception e){

            e.printStackTrace();
        }
    }

    /**
     * This method is to be able to go to the calculator page from a button click
     * @param view
     */
    public void goToCalculator(View view) {
        Intent intent =new Intent(this,calculator.class);
        startActivity(intent);
    }

    /**
     * This method is to be able to go to the Overview page from a button click
     * @param view
     */
    public void goToOverview(View view) {
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * This method allows you to navigate to the next diary item in the list view.
     * if the entry is the last item in the diary entry.It doesnt allow you to click next anymore
     * @param view
     */
    public void loadNextEntry(View view) {
        current++;
        prev.setVisibility(prev.VISIBLE);
        if(current==arrayLen-1){
            header.setText("");
            textView.setText("");
            loadFile(nextEntry.get(current));
            next.setVisibility(next.INVISIBLE);
        }
        else {
            header.setText("");
            textView.setText("");
            loadFile(nextEntry.get(current));
        }
    }

    /**
     * This method allows you to navigate to the previous entry in the list view
     * if the entry is the first item in the diary entry. It doesnt let you click previous anymore
     * @param view
     */
    public void loadPreviousEntry(View view) {
        current--;
        next.setVisibility(next.VISIBLE);
        if(current==0){
            header.setText("");
            textView.setText("");
            loadFile(nextEntry.get(current));
            prev.setVisibility(prev.INVISIBLE);
        }
        else {
            header.setText("");
            textView.setText("");
            loadFile(nextEntry.get(current));
        }
    }
}
