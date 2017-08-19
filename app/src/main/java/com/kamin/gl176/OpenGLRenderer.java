package com.kamin.gl176;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES30.glCreateShader;


public class OpenGLRenderer implements GLSurfaceView.Renderer {
    Context context;
    final String TAG = "LOG_TAG";
    private int programId;
    private FloatBuffer vertexData;
    private int uColor;
    private int aPosition;

    public OpenGLRenderer(Context context) {
        super();
        this.context = context;
        float[] vertices = {-0.5f, -0.2f, 0.0f, 0.2f, 0.5f, -0.2f};
        vertexData = ByteBuffer.allocateDirect(vertices.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES30.glClearColor(0.9f, 0.9f, 0.9f, 1f);
        int vertexShader = createShader(GLES30.GL_VERTEX_SHADER,readTextFromRAW(R.raw.vertex_shader));
        int fragmentShader = createShader(GLES30.GL_FRAGMENT_SHADER,readTextFromRAW(R.raw.fragment_shader));
        int program = createProgram(vertexShader,fragmentShader);
        GLES30.glUseProgram(program);

        uColor = GLES30.glGetUniformLocation(program,"u_Color");
        GLES30.glUniform4f(uColor,0.8f, 0.0f, 0.0f, 1.0f);

        aPosition = GLES30.glGetAttribLocation(program, "a_Position");
        vertexData.position(0);
        GLES30.glVertexAttribPointer(aPosition, 2, GLES30.GL_FLOAT, false, 0, vertexData);
        GLES30.glEnableVertexAttribArray(aPosition);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES30.glViewport(0, 0, i, i1);


    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,3);
    }

    private String readTextFromRAW(int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader;
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\r\n");
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                    bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private int createShader(int type, String ShaderText){
        int shaderID = glCreateShader(type);
        if(shaderID==0){
            return 0;
        }
        GLES30.glShaderSource(shaderID,ShaderText);
        GLES30.glCompileShader(shaderID);
        final int[] compileStatus = new int[1];
        GLES30.glGetShaderiv(shaderID,GLES30.GL_COMPILE_STATUS,compileStatus,0);
        if(compileStatus[0]==0){
            return 0;
        }
        return shaderID;
    }

    private int createProgram(int vertexShaderId, int fragmentShaderId){
        final int programId = GLES30.glCreateProgram();
        if(programId==0)
            return 0;
        GLES30.glAttachShader(programId,vertexShaderId);
        GLES30.glAttachShader(programId,fragmentShaderId);
        GLES30.glLinkProgram(programId);
        final int[] linkStatus = new int[1];
        GLES30.glGetProgramiv(programId,GL_LINK_STATUS,linkStatus,0);
        if(linkStatus[0]==0) {
            GLES30.glDeleteProgram(programId);
            return 0;
        }
        return programId;
    }
}
