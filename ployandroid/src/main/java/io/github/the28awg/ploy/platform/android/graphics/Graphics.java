package io.github.the28awg.ploy.platform.android.graphics;

import android.opengl.GLES20;
import android.opengl.Matrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import io.github.the28awg.ploy.experiential.Platform;
import io.github.the28awg.ploy.experiential.geom.Dimension;
import io.github.the28awg.ploy.experiential.geom.XY;
import io.github.the28awg.ploy.experiential.graphics.Texture;
import io.github.the28awg.ploy.experiential.graphics.Tile;
import io.github.the28awg.ploy.experiential.io.IO;

/**
 * Created by the28awg on 26.12.16.
 */

public class Graphics extends io.github.the28awg.ploy.experiential.graphics.Graphics {

    private static final Logger logger = LoggerFactory.getLogger(Graphics.class);

    private int program;
    private int vertex_shader;
    private int fragment_shader;

    private int a_Position_Location;
    private int a_TextureCoordinate_Location;
    private int u_Projection_Location;
    private int u_View_Location;
    private int u_TextureSampler_Location;

    private int a_TextureCoordinate_Buffer;
    private int a_Position_Buffer;
    private int a_PositionVertexElement_Buffer;

    private float[] projection_matrix = new float[16];
    private float[] view_matrix = new float[16];
    private boolean enable;
    private boolean compile;

    private float scale;
    private XY offset;

    @Override
    protected void init(Builder builder) {
        vertex_shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        try {
            GLES20.glShaderSource(vertex_shader, IO.get().string("glsl/" + builder.tag() + "_vs.glsl"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GLES20.glCompileShader(vertex_shader);

        int[] tmp_vcs = new int[1];
        GLES20.glGetShaderiv(vertex_shader, GLES20.GL_COMPILE_STATUS, tmp_vcs, 0);
        if (tmp_vcs[0] != GLES20.GL_TRUE) {
            throw new RuntimeException("compile vertex shader: " + GLES20.glGetShaderInfoLog(vertex_shader));
        }
        fragment_shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        try {
            GLES20.glShaderSource(fragment_shader, IO.get().string("glsl/" + builder.tag() + "_fs.glsl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        GLES20.glCompileShader(fragment_shader);

        int[] tmp_fcs = new int[1];
        GLES20.glGetShaderiv(fragment_shader, GLES20.GL_COMPILE_STATUS, tmp_fcs, 0);
        if (tmp_fcs[0] != GLES20.GL_TRUE) {
            throw new RuntimeException("compile fragment shader: " + GLES20.glGetShaderInfoLog(fragment_shader));
        }

        program = GLES20.glCreateProgram();

        GLES20.glAttachShader(program, vertex_shader);
        GLES20.glAttachShader(program, fragment_shader);

        GLES20.glLinkProgram(program);

        int[] tmp_pls = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, tmp_pls, 0);
        if (tmp_pls[0] != GLES20.GL_TRUE) {
            throw new RuntimeException("link program: " + GLES20.glGetProgramInfoLog(program));
        }
        GLES20.glDeleteShader(vertex_shader);
        int[] tmp_vds = new int[1];
        GLES20.glGetShaderiv(vertex_shader, GLES20.GL_DELETE_STATUS, tmp_vds, 0);
        if (tmp_vds[0] != GLES20.GL_TRUE) {
            throw new RuntimeException("delete vertex shader: " + GLES20.glGetShaderInfoLog(vertex_shader));
        }

        GLES20.glDeleteShader(fragment_shader);
        int[] tmp_fds = new int[1];
        GLES20.glGetShaderiv(vertex_shader, GLES20.GL_DELETE_STATUS, tmp_fds, 0);
        if (tmp_fds[0] != GLES20.GL_TRUE) {
            throw new RuntimeException("delete fragment shader: " + GLES20.glGetShaderInfoLog(fragment_shader));
        }


        a_Position_Location = GLES20.glGetAttribLocation(program, "a_Position");
        if (a_Position_Location == -1) {
            if (Platform.DEBUG) {
                logger.info(GLES20.glGetProgramInfoLog(program));
                logger.info(GLES20.glGetShaderInfoLog(vertex_shader));
                logger.info(GLES20.glGetShaderInfoLog(fragment_shader));
            }
            throw new RuntimeException("could not bind attribute: a_Position");
        }
        a_TextureCoordinate_Location = GLES20.glGetAttribLocation(program, "a_TextureCoordinate");
        if (a_TextureCoordinate_Location == -1) {
            if (Platform.DEBUG) {
                logger.info(GLES20.glGetProgramInfoLog(program));
                logger.info(GLES20.glGetShaderInfoLog(vertex_shader));
                logger.info(GLES20.glGetShaderInfoLog(fragment_shader));
            }
            throw new RuntimeException("could not bind attribute: a_TextureCoordinate");
        }
        u_Projection_Location = GLES20.glGetUniformLocation(program, "u_Projection");
        if (u_Projection_Location == -1) {
            if (Platform.DEBUG) {
                logger.info(GLES20.glGetProgramInfoLog(program));
                logger.info(GLES20.glGetShaderInfoLog(vertex_shader));
                logger.info(GLES20.glGetShaderInfoLog(fragment_shader));
            }
            throw new RuntimeException("could not bind uniform: u_Projection");
        }
        u_View_Location = GLES20.glGetUniformLocation(program, "u_View");
        if (u_View_Location == -1) {
            if (Platform.DEBUG) {
                logger.info(GLES20.glGetProgramInfoLog(program));
                logger.info(GLES20.glGetShaderInfoLog(vertex_shader));
                logger.info(GLES20.glGetShaderInfoLog(fragment_shader));
            }
            throw new RuntimeException("could not bind uniform: u_View");
        }
        u_TextureSampler_Location = GLES20.glGetUniformLocation(program, "u_TextureSampler");
        if (u_TextureSampler_Location == -1) {
            if (Platform.DEBUG) {
                logger.info(GLES20.glGetProgramInfoLog(program));
                logger.info(GLES20.glGetShaderInfoLog(vertex_shader));
                logger.info(GLES20.glGetShaderInfoLog(fragment_shader));
            }
            throw new RuntimeException("could not bind uniform: u_TextureSampler");
        }

        int[] tmp_tcb = new int[1];
        GLES20.glGenBuffers(1, tmp_tcb, 0);
        a_TextureCoordinate_Buffer = tmp_tcb[0];

        int[] tmp_pb = new int[1];
        GLES20.glGenBuffers(1, tmp_pb, 0);
        a_Position_Buffer = tmp_pb[0];

        int[] tmp_pvb = new int[1];
        GLES20.glGenBuffers(1, tmp_pvb, 0);
        a_PositionVertexElement_Buffer = tmp_pvb[0];

        Dimension dimension = Platform.dimension();
//        Matrix.orthoM(projection_matrix, 0, -rectangle.width() / 2, rectangle.width() / 2, -rectangle.height() / 2, rectangle.height() / 2, -1, 1);
        Matrix.orthoM(projection_matrix, 0, 0, dimension.width(), 0, dimension.height(), -1, 1);

        this.scale = Platform.scale();
        this.offset = Platform.offset();
        compute_view_matrix();

        enable = false;
        compile = true;
    }

    @Override
    public void resize(Dimension dimension) {
//        Matrix.orthoM(projection_matrix, 0, -rectangle.width() / 2, rectangle.width() / 2, -rectangle.height() / 2, rectangle.height() / 2, -1, 1);
        Matrix.orthoM(projection_matrix, 0, 0, dimension.width(), 0, dimension.height(), -1, 1);
        System.out.println("rectangle = [" + dimension + "]");
    }

    private void compute_view_matrix() {
        Matrix.setIdentityM(view_matrix, 0);
        Matrix.scaleM(view_matrix, 0, this.scale, this.scale, 0);
        Matrix.translateM(view_matrix, 0, this.offset.x(), this.offset.y(), 0);
    }

    @Override
    public void scale(float scale) {
        if (scale < 0) {
            throw new RuntimeException("scale < 0");
        }
        this.scale = scale;
        compute_view_matrix();
    }

    @Override
    public void offset(XY xy) {
        this.offset = xy;
        compute_view_matrix();
    }

    @Override
    public void enable() {
        if (compile && !enable) {
            enable = true;
            GLES20.glUseProgram(program);
        }
    }

    @Override
    public void disable() {
        if (compile && enable) {
            enable = false;
            GLES20.glUseProgram(0);
        }
    }

    @Override
    public void dispose() {
        if (compile) {
            disable();
            GLES20.glDeleteProgram(program);
            compile = false;
            super.dispose();
        }
    }

    @Override
    public void push(XY xy, Tile tile) {
        push(xy.x(), xy.y(), tile);
    }

    @Override
    public void push(float x, float y, Tile tile) {
        push(x, y, tile.width(), tile.height(), tile);
    }

    @Override
    public void push(XY xy, Texture texture) {
        push(xy.x(), xy.y(), texture.width(), texture.height(), texture);
    }

    @Override
    public void push(float x, float y, Texture texture) {

        push(x, y, texture.width(), texture.height(), texture);
    }

    @Override
    public void push(XY xy, Dimension dimension, Tile tile) {
        push(xy.x(), xy.y(), dimension.width(), dimension.height(), tile);
    }

    @Override
    public void push(float x, float y, float width, float height, Tile tile) {
        push(tile.map().texture().texture(),
                tile.square().a().x(), tile.square().a().y(),
                tile.square().b().x(), tile.square().b().y(),
                tile.square().c().x(), tile.square().c().y(),
                tile.square().d().x(), tile.square().d().y(),
                x, y, x + width, y, x, y + height, x + width, y + height);
    }

    @Override
    public void push(XY xy, Dimension dimension, Texture texture) {

        push(xy.x(), xy.y(), dimension.width(), dimension.height(), texture);
    }

    @Override
    public void push(float x, float y, float width, float height, Texture texture) {

        push(texture.texture(),
                0, 0,
                1, 0,
                0, 1,
                1, 1,
                x, y, x + width, y, x, y + height, x + width, y + height);
    }

    private void push(int texture_id, float uvxa, float uvya, float uvxb, float uvyb, float uvxc, float uvyc, float uvxd, float uvyd,
                      float xa, float ya, float xb, float yb, float xc, float yc, float xd, float yd) {
        if (enable) {
//            GLES20.glFrontFace(GLES20.GL_CW);
            int v_count = 4;
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture_id);
            GLES20.glUniform1i(u_TextureSampler_Location, 0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, a_TextureCoordinate_Buffer);
            FloatBuffer texture = ByteBuffer.allocateDirect((v_count * 2) << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
            texture.put(uvxa).put(uvya).put(uvxb).put(uvyb).put(uvxc).put(uvyc).put(uvxd).put(uvyd).position(0);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texture.capacity() * 4, texture, GLES20.GL_STATIC_DRAW);
            GLES20.glEnableVertexAttribArray(a_TextureCoordinate_Location);
            GLES20.glVertexAttribPointer(a_TextureCoordinate_Location, 2, GLES20.GL_FLOAT, false, 0, 0);
            GLES20.glUniformMatrix4fv(u_Projection_Location, 1, false, projection_matrix, 0);
            GLES20.glUniformMatrix4fv(u_View_Location, 1, false, view_matrix, 0);
            FloatBuffer vertices = ByteBuffer.allocateDirect((v_count * 2) << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
            IntBuffer elements = ByteBuffer.allocateDirect((6) << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
            vertices.position(0);
            vertices.put(xc).put(yc).put(xd).put(yd).put(xa).put(ya).put(xb).put(yb);
            vertices.position(0);
            elements.position(0);
            elements.put(0).put(1).put(2).put(1).put(3).put(2).flip();
            elements.position(0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, a_Position_Buffer);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.capacity() * 4, vertices, GLES20.GL_STATIC_DRAW);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, a_PositionVertexElement_Buffer);
            GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, elements.capacity() * 4, elements, GLES20.GL_STATIC_DRAW);
            GLES20.glEnableVertexAttribArray(a_Position_Location);
            GLES20.glVertexAttribPointer(a_Position_Location, 2, GLES20.GL_FLOAT, false, 0, 0);
            GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, elements.capacity(), GLES20.GL_UNSIGNED_INT, 0);
        }
    }
}
