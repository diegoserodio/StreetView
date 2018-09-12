package com.example.diego.rastreetview;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button btnConnection, btnLeftCam, btnUpCam, btnRightCam, btnDownCam, btnDownCar, btnUpCar, btnLeftCar, btnRightCar;

    private static final int  bluetoothRequest = 1;
    private static final int  bluetoothConnection = 2;
    boolean connection = false;
    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothDevice mBLuetoothDevice = null;
    BluetoothSocket mBluetoothSocket = null;
    UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static String MAC = null;
    ConnectedThread connectedThread;

    Dialog mDialog;

    private String camLink = "http://google.com";
    private String camLinkUpdate = null;
    boolean linkChanged = false;

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullscreenStyle();

        mDialog = new Dialog(this);

        btnConnection = findViewById(R.id.btnConnection);
        btnLeftCam = findViewById(R.id.btnLeftCam);
        btnRightCam = findViewById(R.id.btnRightCam);
        btnUpCam = findViewById(R.id.btnUpCam);
        btnDownCam = findViewById(R.id.btnDownCam);
        btnLeftCar = findViewById(R.id.btnLeftCar);
        btnRightCar = findViewById(R.id.btnRightCar);
        btnUpCar = findViewById(R.id.btnUpCar);
        btnDownCar = findViewById(R.id.btnDownCar);
        webView = findViewById(R.id.webcamView);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Seu dispositivo não possui bluetooth", Toast.LENGTH_LONG).show();
        }else if(!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, bluetoothRequest);

        }

        runWebView();

        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection){
                    try {
                        mBluetoothSocket.close();
                        connection = false;
                        btnConnection.setText("DESCONECTADO");
                        btnConnection.setBackgroundResource(R.color.orangeRed);
                        Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();
                    }catch (IOException error){
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro: " + error, Toast.LENGTH_LONG).show();
                    }

                }else{
                    Intent openList = new Intent(MainActivity.this, DevicesList.class);
                    startActivityForResult(openList, bluetoothConnection);
                }
            }
        });

        btnLeftCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection){
                    connectedThread.write("lw");
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnRightCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection){
                    connectedThread.write("rw");
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnDownCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection){
                    connectedThread.write("dw");
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnUpCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection){
                    connectedThread.write("uw");
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();

                }
            }
        });

        btnLeftCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection){
                    connectedThread.write("lc");
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnRightCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection){
                    connectedThread.write("rc");
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnDownCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection){
                    connectedThread.write("dc");
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnUpCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connection){
                    connectedThread.write("uc");
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth desconectado", Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    public void camLinkInput(View v){
        Button btnCancelar, btnConfirmar;
        final TextInputLayout linkInput;
        mDialog.setContentView(R.layout.activity_cam_link);
        btnCancelar = mDialog.findViewById(R.id.btnCancelar);
        btnConfirmar = mDialog.findViewById(R.id.btnConfirmar);
        linkInput = mDialog.findViewById(R.id.linkInput);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camLinkUpdate = linkInput.getEditText().getText().toString().trim();
                if(camLinkUpdate.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Campo vazio", Toast.LENGTH_LONG).show();
                }else if(camLinkUpdate.startsWith("http://")){
                    linkChanged = true;
                    runWebView();
                }else{
                    linkChanged = true;
                    camLinkUpdate = "http://" + linkInput.getEditText().getText().toString().trim();
                    runWebView();
                }
                fullscreenStyle();
                mDialog.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenStyle();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void runWebView(){
        if(linkChanged){
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(camLinkUpdate);
        }else{
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(camLink);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case bluetoothRequest:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "O bluetooth foi ativado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "O bluetooth não foi ativado, o app será encerrado", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            case  bluetoothConnection:
                if(resultCode == Activity.RESULT_OK){
                    MAC = data.getExtras().getString(DevicesList.MACAddress);
                    mBLuetoothDevice = mBluetoothAdapter.getRemoteDevice(MAC);
                    try {
                        mBluetoothSocket = mBLuetoothDevice.createRfcommSocketToServiceRecord(mUUID);
                        mBluetoothSocket.connect();
                        Toast.makeText(getApplicationContext(), "Você foi conectado com: "+ MAC, Toast.LENGTH_LONG).show();
                        connection = true;
                        connectedThread = new ConnectedThread(mBluetoothSocket);
                        connectedThread.start();
                        btnConnection.setText("CONECTADO");
                        btnConnection.setBackgroundResource(R.color.green);
                    } catch (IOException error){
                        connection = false;
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro: " + error, Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Falha ao obter endereço MAC", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fullscreenStyle();
    }

    protected void fullscreenStyle(){
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }

    private class ConnectedThread extends Thread {
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            OutputStream tmpOut = null;

            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmOutStream = tmpOut;
        }

        public void write(String data) {
            byte[] dataByte = data.getBytes();
            try {
                mmOutStream.write(dataByte);
            } catch (IOException e) { }
        }
    }
}
