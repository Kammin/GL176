package com.kamin.gl176;

import android.app.ActivityManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final String TAG = "LOG_TAG";
    GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isGL30()){
            Toast.makeText(this,"GLES 3.0 not supported!",Toast.LENGTH_SHORT).show();
            finish();
        }
        Log.d(TAG,"----- Start!");
        glSurfaceView = new GLSurfaceView(this);
        OpenGLRenderer openGLRenderer = new OpenGLRenderer(this);
        glSurfaceView.setEGLContextClientVersion(3);
        glSurfaceView.setRenderer(openGLRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(glSurfaceView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    private boolean isGL30(){
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        return (activityManager.getDeviceConfigurationInfo().reqGlEsVersion>=0x0003);
    }
}
