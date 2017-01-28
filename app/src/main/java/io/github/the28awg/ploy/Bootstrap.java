package io.github.the28awg.ploy;

import io.github.the28awg.ploy.experiential.Platform;
import io.github.the28awg.ploy.experiential.graphics.GraphicLayer;
import io.github.the28awg.ploy.experiential.graphics.Graphics;
import io.github.the28awg.ploy.experiential.text.Text;
import io.github.the28awg.ploy.experiential.text.fonts.GlyphFactory;

/**
 * Created by the28awg on 25.01.17.
 */

public class Bootstrap implements GraphicLayer {

    private Graphics graphics;
    private GlyphFactory factory;

    @Override
    public String tag() {
        return "bootstrap";
    }

    @Override
    public void init(GraphicLayer parent) {
        graphics = new Graphics.Builder("test").build();
        factory = GlyphFactory.factory("DroidSansMono.ttf");
//        Text.text2(0, 0, null, null, "ABCDEFGHIJKLMNOPQRSTUVWXYZ space\nabcdefghijklmnopqrstuvwxyz");
    }

    @Override
    public void tick() {
        graphics.enable();
        Text.text(100, 100, graphics, factory, "ABCDEFGHIJKLMNOPQRSTUVWXYZ space\nabcdefghijklmnopqrstuvwxyz");
        graphics.disable();
        System.out.println("fps: " + Platform.fps());
    }

    @Override
    public void enter(GraphicLayer parent) {

    }

    @Override
    public void leave(GraphicLayer parent) {

    }
}
