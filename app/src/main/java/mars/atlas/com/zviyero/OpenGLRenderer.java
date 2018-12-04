package mars.atlas.com.zviyero;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.glGenTextures;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glGetUniformLocation;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glUniformMatrix4fv;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private final Context context;
    private FloatBuffer vertexData;
    private int programId;
    private int uColorLocation;
    private int aPositionLocation;
    private int aColorLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int uMatrixLocation;
    private int texture;


    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMatrix = new float[16];

    public OpenGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_DEPTH_TEST);
        createAndUseProgram();
        getLocations();
        prepareData();
        bindData();
        createViewMatrix();



    }

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = 0;
        float eyeY = 0;
        float eyeZ = 7;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void getLocations() {
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        aTextureLocation = glGetAttribLocation(programId, "a_Texture");
        uTextureUnitLocation = glGetUniformLocation(programId, "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
    }

    private void createAndUseProgram() {
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
    }

    private void bindData() {

        // координаты вершин
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // координаты текстур
        vertexData.position(POSITION_COUNT);
        glVertexAttribPointer(aTextureLocation, TEXTURE_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(aTextureLocation);

        // помещаем текстуру в target 2D юнита 0
        glActiveTexture(GL_TEXTURE0);

        /*final int[] textureIds = new int[1];
        glGenTextures(1, textureIds, 0);


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource( context.getResources(), R.drawable.box, options);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureIds[0]);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();



        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindTexture(GL_TEXTURE_2D, textureIds[0]);*/


        //glBindTexture(GL_TEXTURE_2D, 0);

        glBindTexture(GL_TEXTURE_2D, texture);

        // юнит текстуры
        glUniform1i(uTextureUnitLocation, 0);
    }




    @Override
    public void onDrawFrame(GL10 arg0) {
        //glClear(GL_COLOR_BUFFER_BIT);
        /*glLineWidth(5);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);*/

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        bindMatrix();
    }

    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 2;
        float far = 12;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }


    private void prepareData() {
       /* float[] vertices = {
                -0.5f, 0.3f, 1.0f, 0.0f, 0.0f,
                -0.5f, -0.3f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.3f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.3f, 0.0f, 0.0f, 1.0f,
        };  */
        float[] vertices = {
                -1,  1, 1,   0, 0,
                -1, -1, 1,   0, 1,
                1,  1, 1,   1, 0,
                1, -1, 1,   1, 1,
        };

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        texture = TextureUtils.loadTexture(context, R.drawable.box);


    }
}
