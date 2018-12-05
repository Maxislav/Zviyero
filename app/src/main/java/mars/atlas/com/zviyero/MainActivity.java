package mars.atlas.com.zviyero;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    OpenGLRenderer openGLRenderer;
    private final static String LOG_TAG = "MainActivityClass";
    float angleZ = 0, angleX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()) {
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        setContentView(R.layout.main_activity);
        glSurfaceView = (GLSurfaceView)findViewById(R.id.surfaceviewclass);
        glSurfaceView.setEGLContextClientVersion(2);
        openGLRenderer = new OpenGLRenderer(this);

        glSurfaceView.setRenderer(openGLRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        glSurfaceView.setOnTouchListener(new GLSurfaceViewEvent());


    }


    private class GLSurfaceViewEvent implements View.OnTouchListener, View.OnDragListener {
        float X, Y, dX, dY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    X = 0;
                    Y = 0;
                    return true;
                case MotionEvent.ACTION_MOVE:{
                    if(X != 0){
                        dX = X - event.getX();
                    }
                    if(Y != 0){
                        dY = Y - event.getY();
                    }
                    angleZ +=(dX/2);
                    angleX -=(dY/2);
                    openGLRenderer.setAngle(angleZ, angleX);
                    X = event.getX();
                    Y = event.getY();
                    break;
                }
            }
            return false;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            return false;
        }
    }



    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }



}
