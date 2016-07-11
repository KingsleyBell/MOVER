package com.example.mover.mover;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements getRequest.AsyncResponse {

    TextView tv1=null;
    private double acc;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView);
        //tv1.setVisibility(View.GONE);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                acc = Math.sqrt(x * x + y * y + z * z);

                tv1.setText("Acceleration: " + acc);
                //tv1.setVisibility(View.VISIBLE);

                if(acc > 20) {
                    System.out.println(acc);
                    accidentAlert();
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

        }, sensor, SensorManager.SENSOR_DELAY_FASTEST);

//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        List<Sensor> mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);
//
//        for (int i = 1; i < mList.size(); i++) {
//            //tv1.setVisibility(View.VISIBLE);
//            tv1.append("\n" + mList.get(i).getName() + "\n" + mList.get(i).getVendor() + "\n" + mList.get(i).getVersion());
//        }

    }

    /** Called when the user clicks the POST button */
    public void postMessage(View view) {

        postRequest asyncTask = (postRequest) new postRequest(new postRequest.AsyncResponse(){

            @Override
            public void processFinish(String output){
                Context context = getApplicationContext();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, output, duration);
                toast.show();
            }
        }, "name=Luke&message=Hello").execute("http://moutonf.co.za:5000/test-post");

    }

    /** Called when the user clicks the GET button */
    public void getMessage(View view) {

        getRequest asyncTask = (getRequest) new getRequest(new getRequest.AsyncResponse(){

            @Override
            public void processFinish(String output){
                Context context = getApplicationContext();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, output, duration);
                toast.show();
            }
        }).execute("http://moutonf.co.za:5000/test-get");


    }

    public void accidentAlert() {

//        postRequest asyncTask = (postRequest) new postRequest(new postRequest.AsyncResponse(){
//
//            @Override
//            public void processFinish(String output){
//                Context context = getApplicationContext();
//
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, output, duration);
//                toast.show();
//            }
//        }, "name=Luke&message=accident!").execute("http://moutonf.co.za:5000/test-post");

        Intent k = new Intent(this, accidentActivity.class);
        startActivity(k);

    }

    @Override
    public void processFinish(String output) {

    }

}
