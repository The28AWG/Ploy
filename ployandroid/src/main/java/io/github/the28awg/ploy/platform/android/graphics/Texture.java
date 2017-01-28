package io.github.the28awg.ploy.platform.android.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.github.the28awg.ploy.experiential.io.IO;

/**
 * Created by the28awg on 24.12.16.
 */

public class Texture extends io.github.the28awg.ploy.experiential.graphics.Texture {

    private boolean enabled;

    public Texture() {
        enabled = false;
    }

    public Texture(Bitmap bitmap) {
        int[] tmp = new int[1];
        GLES20.glGenTextures(1, tmp, 0);
        this.texture = tmp[0];
        this.filter = Filter.NEAREST;
        this.wrap = Wrap.REPEAT;
        enable();
        setTexParameter();
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        disable();
    }

    @Override
    protected void init(Builder builder) {
        int[] tmp = new int[1];
        GLES20.glGenTextures(1, tmp, 0);
        this.texture = tmp[0];
        this.filter = builder.filter();
        this.wrap = builder.wrap();
        enable();
        setTexParameter();
        if (builder.file() != null && !builder.file().isEmpty()) {
            try {
                InputStream stream = IO.get().stream(builder.file());
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                this.width = bitmap.getWidth();
                this.height = bitmap.getHeight();
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
                bitmap.recycle();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.width = builder.width();
            this.height = builder.height();
            ByteBuffer buffer = ByteBuffer.allocateDirect(this.width * this.height * 4).order(ByteOrder.nativeOrder());
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
        }
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        disable();
    }

    private void setTexParameter() {
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, filter == Filter.NEAREST ? GLES20.GL_NEAREST : GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, filter == Filter.NEAREST ? GLES20.GL_NEAREST : GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, wrap == Wrap.REPEAT ? GLES20.GL_REPEAT : GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wrap == Wrap.REPEAT ? GLES20.GL_REPEAT : GLES20.GL_CLAMP_TO_EDGE);
    }

    @Override
    public void enable() {
        if (this.texture != NONE && !enabled) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.texture);
            enabled = true;
        }
    }

    @Override
    public void disable() {
        if (this.texture != NONE && enabled) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, NONE);
            enabled = false;
        }
    }

    @Override
    public void dispose() {
        if (this.texture != NONE) {
            disable();
            int[] tmp = new int[]{this.texture};
            GLES20.glDeleteTextures(1, tmp, 0);
            this.texture = NONE;
        }
    }
}
