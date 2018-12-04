package mars.atlas.com.zviyero;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()) {
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new OpenGLRenderer(this));
        setContentView(glSurfaceView);


    }
    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

}
