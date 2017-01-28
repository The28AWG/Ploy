package io.github.the28awg.ploy.experiential.graphics.renderer;

import io.github.the28awg.ploy.experiential.entity.Entity;
import io.github.the28awg.ploy.experiential.entity.PositionComponent;
import io.github.the28awg.ploy.experiential.graphics.Animation;
import io.github.the28awg.ploy.experiential.graphics.Graphics;
import io.github.the28awg.ploy.experiential.graphics.Renderer;
import io.github.the28awg.ploy.experiential.graphics.Texture;
import io.github.the28awg.ploy.experiential.graphics.TileMap;

/**
 * Created by the28awg on 25.01.17.
 */

public class AnimationRenderer implements Renderer {

    private String animation_name;
    private Texture tile_texture;
    private Animation animation;

    public AnimationRenderer(String animation_name) {
        this.animation_name = animation_name;
        this.tile_texture = new Texture.Builder("textures/" + animation_name + ".png").build();
        this.animation = new Animation.Builder(new TileMap(tile_texture, tile_texture.width(), tile_texture.height())).build();
    }

    @Override
    public String tag() {
        return animation_name;
    }

    @Override
    public void render(Entity entity, Graphics g) {
        PositionComponent position = entity.component(PositionComponent.class);
        if (position != null) {
            g.push(position, animation);
        }
    }

    @Override
    public void dispose() {
        tile_texture.dispose();
    }
}
