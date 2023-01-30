package com.example.hw05;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.hw05.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Assignment No: HW05
File name: MainActivity.java
Name of the students: Juhi Jayant Jadhav , Saifuddin Mohammed
Group no: 05
 */

public class MainActivity extends AppCompatActivity {

    ExecutorService threadPool;
    Handler handler;
    int totalComplexity= 0;

    ArrayList<Double> numbers = new ArrayList<Double>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Main Activity");
        setContentView(R.layout.activity_main);

        SeekBar seekBar = findViewById(R.id.seekBar);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        ListView listViewNumbers = findViewById(R.id.listViewNumbers);
        TextView progressTextView = findViewById(R.id.progressTextView);
        TextView averageCountTextView = findViewById(R.id.averageCountTextView);
        TextView averageTextView = findViewById(R.id.averageTextView);

        listViewNumbers.setVisibility(View.INVISIBLE);
        progressTextView.setVisibility(View.INVISIBLE);
        averageCountTextView.setVisibility(View.INVISIBLE);
        averageTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        threadPool = Executors.newFixedThreadPool(2);
        handler= new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                switch (message.what){
                    case DoWork.STATUS_Progress:{
                        TextView progressTextView = findViewById(R.id.progressTextView);
                        TextView averageCountTextView = findViewById(R.id.averageCountTextView);
                        progressTextView.setText((message.getData().getInt(DoWork.PROGRESS_KEY)+1) + "/" + totalComplexity);
                        progressBar.setProgress(message.getData().getInt(DoWork.PROGRESS_KEY)+1);
                        numbers.add(message.getData().getDouble(DoWork.NUMBER_KEY)) ;
                        double sum=0;
                        for(int i = 0 ; i< numbers.size();i++ ){
                            sum += numbers.get(i);
                        }
                        double average = sum/numbers.size();
                        averageCountTextView.setText(average + "");

                        ArrayAdapter<Double> arrayAdapter = new ArrayAdapter<Double>(MainActivity.this , android.R.layout.simple_list_item_1,android.R.id.text1, numbers);
                        listViewNumbers.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                        listViewNumbers.setVisibility(View.VISIBLE);

                        averageCountTextView.setVisibility(View.VISIBLE);
                        averageTextView.setVisibility(View.VISIBLE);

                    }

                }


                return false;
            }
        });

        findViewById(R.id.buttonGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                totalComplexity = seekBar.getProgress();
                numbers.clear();
                ArrayAdapter<Double> arrayAdapter = new ArrayAdapter<Double>(MainActivity.this , android.R.layout.simple_list_item_1,android.R.id.text1, numbers);
                listViewNumbers.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
                TextView progressTextView = findViewById(R.id.progressTextView);
                progressTextView.setText(0 + "/" + totalComplexity);
                progressBar.setMax(totalComplexity);
                progressBar.setProgress(0);
                threadPool.execute(new DoWork(totalComplexity));
                progressBar.setVisibility(View.VISIBLE);
                progressTextView.setVisibility(View.VISIBLE);



            }

        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView complexitySelectedTextView = findViewById(R.id.complexitySelectedTextView);
                complexitySelectedTextView.setText(i + " times");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }

    class DoWork implements Runnable{

        int numberOfTimes;
        static final int STATUS_Progress = 0x01;
        static final String PROGRESS_KEY ="PROGRESS_KEY" ;
        static final String NUMBER_KEY ="NUMBER_KEY" ;

        public DoWork(int i) {
            this.numberOfTimes = i;
        }


        @Override
        public void run() {

            for(int i = 0; i<numberOfTimes;i++)
            {
                double number =  HeavyWork.getNumber();
                Bundle bundle =new Bundle();
                Message message = new Message();
                message.what = STATUS_Progress;
                bundle.putInt(PROGRESS_KEY,i);
                bundle.putDouble(NUMBER_KEY,number);
                message.setData(bundle);
                handler.sendMessage(message);
            }


        }
    }
}