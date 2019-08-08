package com.example.kilojoule;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;


public class calculator extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText date;
    Button foodButton;
    EditText food_nki;
    EditText exercise_nki;
    int runningFoodTotal=0;
    int exerciseFoodTotal=0;
    int nettTotal=0;

    @Override
    /**
     * The on create method for the calculator loads the the spinners for food and exercise from the string resourse
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        date=(EditText) findViewById(R.id.Date);
        food_nki=findViewById(R.id.food_nki);
        exercise_nki=findViewById(R.id.exercise_nki);
        //spinner for exercise
        Spinner exerciseSpinner=findViewById(R.id.exercise_spinner);
        ArrayAdapter<CharSequence> adapter_exercise=ArrayAdapter.createFromResource(this,R.array.exercise_array,R.layout.support_simple_spinner_dropdown_item);
        adapter_exercise.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter_exercise);
        exerciseSpinner.setOnItemSelectedListener(this);
        //Spinner for food
        Spinner foodSpinner=findViewById(R.id.food_spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.label_array,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodSpinner.setAdapter(adapter);
        foodSpinner.setOnItemSelectedListener(this);
    }
    String currentSpinner;
    @Override
    /**
     * override method for spinner to be able to select an option within the spinner
     * also gets the text of that item to be able to save it to a file
     */
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text=adapterView.getItemAtPosition(i).toString();
        currentSpinner=text;
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    /**
     * This method first ensures that a date is added and an nki value is added when the add button is clicked.
     * It then does the arithmetic calculations such that the food total is updated.
     * it writes the data to a file with the date as the file name
     * @param view
     */
    public void addToFileFood(View view) {
        String dateCheck=date.getText().toString();
        String foodNkiCheck=food_nki.getText().toString();
        if(dateCheck.length()==0){
            date.setError("You must enter a date first");
        }
        else if(foodNkiCheck.length()==0){
            food_nki.setError("You must fill in a NKI");
        }
        else{
            Button foodBttn=findViewById(R.id.food_button);
            //on click of the add button part of food
            foodBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView foodTotal= findViewById(R.id.food_running_total);
                    runningFoodTotal=runningFoodTotal+Integer.parseInt(food_nki.getText().toString());
                    foodTotal.setText(runningFoodTotal+"");
                    String dateCheck=date.getText().toString();
                    String foodNkiCheck=food_nki.getText().toString();
                    saveToFile(dateCheck,foodNkiCheck);
                }
            });
        }
    }

    /**
     * This method writes the food and exercise totals into the file when add button is pressed.
     * It also updates the net total
     * @param filename
     * @param nki
     */
    public void saveToFile(String filename,String nki){
        try {
            FileOutputStream fos =openFileOutput("ID: "+filename, Context.MODE_APPEND);
            fos.write((" "+currentSpinner+" : "+nki).getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            fos.close();
            Toast toast =Toast.makeText(this,"Calculating...", Toast.LENGTH_SHORT);
            toast.show();
            nettTotal=runningFoodTotal-exerciseFoodTotal;
            TextView nett=findViewById(R.id.nett_total);
            nett.setText(nettTotal+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This method first ensures that a date is added and an nki value is added when the add button is clicked.
     * It then does the arithmetic calculations such that the exercise total is updated.
     * it writes the data to a file with the date as the file name
     * @param view
     */
    public void addToFileExercise(View view) {
        String dateCheck=date.getText().toString();
        String exerciseNkiCheck=exercise_nki.getText().toString();
        if(dateCheck.length()==0){
            date.setError("You must enter a date first");
        }
        else if(exerciseNkiCheck.length()==0){
            exercise_nki.setError("You must fill in a NKI");
        }
        else{
            Button exerciseBttn=findViewById(R.id.exercise_button);
            exerciseBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView exerciseTotal= findViewById(R.id.exercise_running_total);
                    exerciseFoodTotal=exerciseFoodTotal+Integer.parseInt(exercise_nki.getText().toString());
                    exerciseTotal.setText(exerciseFoodTotal+"");
                    String dateCheck=date.getText().toString();
                    String exerciseNkiCheck=exercise_nki.getText().toString();
                    saveToFile(dateCheck,exerciseNkiCheck);
                }
            });
        }
    }

    /**
     * When the back button is clicked, this method first calls the delete file method. To delete the created file before going back.
     * Basically doesnt save when back button is pressed
     * then used the systems finish methos to go to the previous activity
     * @param view
     */
    public void backClick(View view) {
        deleteInternalFile();
        finish();
    }
    /**
     * Delete internal file method deletes the file creates to the corresponding date
     */
    public void deleteInternalFile(){
        try{
            String filename=date.getText().toString();
            File dir = getFilesDir();
            File file=new File(dir,"ID: "+filename);
            file.delete();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * saveToDiary method is performed when the save button is pressed.
     * It first calls the method to write the net total to the file and send uses intent to open the entry in the entries page
     * @param view
     */
    public void saveToDiary(View view) {
        String filename=date.getText().toString();
        String foodNkiCheck=food_nki.getText().toString();
        if(filename.length()==0){
            date.setError("You must enter a date first");
        }
        else {
            saveNettTotalFile(filename);
            Intent intent = new Intent(this, Entries.class);
            intent.putExtra("FILENAME", filename);
            intent.putExtra("CHECK",1);
            startActivity(intent);
        }
    }

    /**
     * This method add the net total to the corresponding file.
     * It also writes the nettotal to unique file that is used when calculating the average
     * @param filename
     */
    public void saveNettTotalFile(String filename){
        try {
            FileOutputStream fos =openFileOutput("ID: "+filename, Context.MODE_APPEND);
            FileOutputStream average =openFileOutput("NetTotals", Context.MODE_APPEND);
            fos.write((" Net Total : "+nettTotal).getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            fos.close();
            average.write((nettTotal+"").getBytes());
            average.write(System.getProperty("line.separator").getBytes());
            average.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
