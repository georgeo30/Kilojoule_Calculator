package com.example.kilojoule;

        import android.support.v7.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;

        import java.io.BufferedReader;
        import java.io.DataInputStream;
        import java.io.FileInputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> totals=new ArrayList<Integer>();
    float average=0;
    int count=0;
    @Override
    /**
     * onCreate for the main page
     * triggers the average such that the average is alsways updating upon coming to the main page.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        launchAverage();
    }

    /**
     * opens the calculator page
     * @param view
     */
    public void launchCalculator(View view) {
        Intent intent = new Intent(this, calculator.class);
        startActivity(intent);
    }

    /**
     * opens the diary page that contains the list views
     * @param view
     */
    public void launchDiary(View view) {
        Intent intent=new Intent(this,diary.class);
        startActivity(intent);
    }

    /**
     * Triggers the average to update
     * reads through a file which contains all the net totals cand calculates the average
     * writes the average to the text view. Displaying the current average.
     */
    public void launchAverage() {
        TextView t=(TextView)findViewById(R.id.averageText);
        try {

            FileInputStream fis = openFileInput("NetTotals");
            BufferedReader reader =new BufferedReader(new InputStreamReader(new DataInputStream(fis)));

            String line;
            while((line=reader.readLine())!=null){
                totals.add(Integer.parseInt(line));
            }
            fis.close();
            count=totals.size();
            for(int i: totals){
                average=average+i;
            }
            t.setText(("Average : "+String.format("%.2f",average/count)));
            average=0;
        }
        catch(Exception e){

            e.printStackTrace();
        }

    }
}
