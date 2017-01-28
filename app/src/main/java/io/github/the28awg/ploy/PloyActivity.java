package io.github.the28awg.ploy;

public class PloyActivity extends io.github.the28awg.ploy.platform.android.PloyActivity {

    @Override
    public void init_layers() {
        layers().add(new Bootstrap(), new Test());
    }
}
