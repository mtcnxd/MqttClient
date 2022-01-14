package com.example.mqttclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    TextView txtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOn = (Button)findViewById(R.id.btnOn);
        Button btnOff = (Button)findViewById(R.id.btnOff);
        txtInput = (TextView)findViewById(R.id.txtInput);

        startMqtt();

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishToTopic("mtcnxd/feeds/led", "1");
                Log.d("Mensaje","ON");
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishToTopic("mtcnxd/feeds/led", "0");
                Log.d("Mensaje","OFF");
            }
        });

    } // onCreate

    private void publishToTopic(String topic, String mensaje){
        try {
            MqttMessage message = new MqttMessage(mensaje.getBytes());
            mqttHelper.mqttAndroidClient.publish(topic, message);
            message.setQos(0);
            message.setRetained(false);

        } catch (MqttException ex) {
            Log.w("Mensaje", ex);
        }
    }


    private void startMqtt(){
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) { }

            @Override
            public void connectionLost(Throwable throwable) { }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Mensaje", mqttMessage.toString());
                txtInput.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) { }
        });
    }


} // MainActivity