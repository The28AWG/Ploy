package io.github.the28awg.ploy.platform.android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.github.the28awg.ploy.experiential.Platform;
import io.github.the28awg.ploy.experiential.entity.systems.ClickableSystem;
import io.github.the28awg.ploy.experiential.entity.systems.DraggableSystem;
import io.github.the28awg.ploy.experiential.events.WindowDimensionChange;
import io.github.the28awg.ploy.experiential.geom.Dimension;
import io.github.the28awg.ploy.experiential.geom.XY;
import io.github.the28awg.ploy.experiential.graphics.GraphicLayerGroup;
import io.github.the28awg.ploy.experiential.input.Input;

public abstract class PloyActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    private static final Logger logger = LoggerFactory.getLogger(PloyActivity.class);
    private static WeakReference<Context> context;

    private GLSurfaceView view;
    private GraphicLayerGroup group;
    private boolean surface_created;
    private int width;
    private int height;
    private long last_time;
    private int fps;
    private ScaleGestureDetector scaleDetector;
    private float scaleFactor = 1.0f;
    private GestureDetector dragDetector;

    public static Context context() {
        return context.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideUI();
        if (hasGL()) {
            context = new WeakReference<>(this.getApplicationContext());
            scaleDetector = new ScaleGestureDetector(this, new ScaleListener());
            dragDetector = new GestureDetector(this, new DragListener());
            initSurface();
        }
        if (Platform.DEBUG) {
            logger.info("PloyActivity.onCreate");
        }
    }

    private void hideUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private boolean hasGL() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }

    private void initSurface() {
        surface_created = false;
        width = -1;
        height = -1;
        last_time = System.nanoTime();
        fps = 0;
        view = new GLSurfaceView(this);
        view.setEGLContextClientVersion(2);
        view.setPreserveEGLContextOnPause(true);
        view.setRenderer(this);
        setContentView(view);
        if (Platform.DEBUG) {
            logger.info("PloyActivity.initSurface");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Platform.DEBUG) {
            logger.info("PloyActivity.onResume");
        }
        if (view != null) {
            view.onResume();
            view.requestRender();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Platform.DEBUG) {
            logger.info("PloyActivity.onPause");
        }
        if (view != null) {
            view.onPause();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (Platform.DEBUG) {
            logger.info("PloyActivity.onSurfaceCreated");
        }
        surface_created = true;
        width = -1;
        height = -1;
        group = new GraphicLayerGroup() {
            @Override
            public void init_layers() {
                PloyActivity.this.init_layers();
            }

            @Override
            protected void postRenderLayer() {

            }

            @Override
            protected void preRenderLayer() {

            }
        };
    }

    public abstract void init_layers();

    public GraphicLayerGroup layers() {
        return group;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (Platform.DEBUG) {
            logger.info("PloyActivity.onSurfaceChanged");
        }
        if (!surface_created && width == this.width && height == this.height) {
            if (Platform.DEBUG) {
                logger.info("Surface changed but already handled.");
            }
            return;
        }
        if (Platform.DEBUG) {
            String msg = "Surface changed width:" + width + " height:" + height;
            if (surface_created) {
                msg += " context lost.";
            } else {
                msg += ".";
            }
            logger.info(msg);
        }
        this.width = width;
        this.height = height;

        GLES20.glViewport(0, 0, this.width, this.height);
        Platform.dimension(new Dimension(this.width, this.height));
        Platform.bus().post(new WindowDimensionChange(Platform.dimension()));
        if (surface_created) {
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            GLES20.glClearColor(1f, 1f, 1f, 1f);
            group.init(null);
            surface_created = false;
        }
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        System.out.println("onKeyMultiple: keyCode = [" + keyCode + "], repeatCount = [" + repeatCount + "], event = [" + event + "]");
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        System.out.println("onKeyLongPress: keyCode = [" + keyCode + "], event = [" + event + "]");
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float px = event.getX() / Platform.scale() - Platform.offset().x();
        float py = (height - event.getY()) / Platform.scale() - Platform.offset().y();
        scaleDetector.onTouchEvent(event);
        if (!dragDetector.onTouchEvent(event)) {
            DraggableSystem.done();
        }
        Input.xy().xy(px, py);
        if (!DraggableSystem.dragged()) {
            ClickableSystem.update(event.getAction() == MotionEvent.ACTION_DOWN);
        }
        if (ClickableSystem.clicked()) {
            System.out.println(ClickableSystem.entity().identifier());
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (Platform.DEBUG) {
            fps++;
            long current_time = System.nanoTime();
            if (current_time - last_time >= 1000000000) {
                Platform.fps(fps);
                fps = 0;
                last_time = current_time;
            }
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        group.tick();
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            Platform.scale(scaleFactor);
            return true;
        }
    }

    private class DragListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            DraggableSystem.drag(new XY(distanceX / Platform.scale(), distanceY / Platform.scale()));
            return true;
        }
    }
}
