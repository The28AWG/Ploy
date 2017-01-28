package io.github.the28awg.ploy;

import android.graphics.Color;

import io.github.the28awg.ploy.experiential.Platform;
import io.github.the28awg.ploy.experiential.entity.DimensionComponent;
import io.github.the28awg.ploy.experiential.entity.Entity;
import io.github.the28awg.ploy.experiential.entity.EntityManager;
import io.github.the28awg.ploy.experiential.entity.PositionComponent;
import io.github.the28awg.ploy.experiential.entity.RenderComponent;
import io.github.the28awg.ploy.experiential.entity.components.ClickableComponent;
import io.github.the28awg.ploy.experiential.entity.components.DraggableComponent;
import io.github.the28awg.ploy.experiential.entity.components.LayerComponent;
import io.github.the28awg.ploy.experiential.entity.components.LocationComponent;
import io.github.the28awg.ploy.experiential.entity.systems.LocationSystem;
import io.github.the28awg.ploy.experiential.entity.systems.RenderSystem;
import io.github.the28awg.ploy.experiential.graphics.GraphicLayer;
import io.github.the28awg.ploy.experiential.graphics.Graphics;
import io.github.the28awg.ploy.experiential.graphics.renderer.TileRenderer;
import io.github.the28awg.ploy.experiential.text.Style;
import io.github.the28awg.ploy.experiential.text.fonts.Glyph;
import io.github.the28awg.ploy.experiential.text.fonts.GlyphFactory;
import io.github.the28awg.ploy.experiential.text.fonts.GlyphManager;

/**
 * Created by the28awg on 26.12.16.
 */

public class Test implements GraphicLayer {

    private Graphics graphics;
    private GlyphFactory factory;

    @Override
    public String tag() {
        return "test";
    }

    @Override
    public void init(GraphicLayer parent) {
        Platform.scale(3);
        graphics = new Graphics.Builder("test").build();
        factory = GlyphFactory.factory("DroidSansMono.ttf");
        RenderSystem.enable(new TileRenderer("1"));
        RenderSystem.enable(new TileRenderer("2"));
        RenderSystem.enable(new TileRenderer("3"));
        LocationSystem.magnets(LayerComponent.LayerLevelStrategy.UI, new LocationSystem.MagnetLayer("UI", new LocationSystem.Magnet(0, 0),
                new LocationSystem.Magnet(0, 100), new LocationSystem.Magnet(0, 200)));

        EntityManager.add(new Entity("block_1",
                new RenderComponent("1"),
                new PositionComponent(100, 100),
                new DraggableComponent(),
                new DimensionComponent(32, 32),
                new LocationComponent(100, 100),
                new LayerComponent(LayerComponent.LayerLevelStrategy.UI),
                new ClickableComponent()));
        EntityManager.add(new Entity("block_2", new RenderComponent("2"), new PositionComponent(200, 200), new DraggableComponent(), new DimensionComponent(32, 32)));
        EntityManager.add(new Entity("block_3", new RenderComponent("3"), new PositionComponent(300, 300), new DraggableComponent(), new DimensionComponent(32, 32)));
    }

    @Override
    public void tick() {
        graphics.enable();
        RenderSystem.render(graphics);
        LocationSystem.update();

        Glyph glyph = GlyphManager.glyph('T', new Style().foreground(Color.RED), 30, factory);
        if (glyph != null) {
            System.out.println(glyph);
//            TileMap map = new TileMap(glyph.texture(), glyph.rectangle());
//            graphics.push(400, 100, map.tile(0));
        }
        graphics.disable();
    }

    @Override
    public void enter(GraphicLayer parent) {

    }

    @Override
    public void leave(GraphicLayer parent) {
        graphics.dispose();
        RenderSystem.disable("1");
    }
}
