package io.github.the28awg.ploy.experiential.graphics.renderer;

import io.github.the28awg.ploy.experiential.entity.Entity;
import io.github.the28awg.ploy.experiential.entity.PositionComponent;
import io.github.the28awg.ploy.experiential.graphics.Graphics;
import io.github.the28awg.ploy.experiential.graphics.Renderer;
import io.github.the28awg.ploy.experiential.graphics.Texture;
import io.github.the28awg.ploy.experiential.graphics.Tile;
import io.github.the28awg.ploy.experiential.graphics.TileMap;

/**
 * Created by the28awg on 18.01.17.
 */

public class TileRenderer implements Renderer {

    private String tile_name;
    private Texture tile_texture;
    private Tile tile;

    public TileRenderer(String tile_name) {
        this.tile_name = tile_name;
        this.tile_texture = new Texture.Builder("textures/" + tile_name + ".png").build();
        this.tile = new TileMap(tile_texture, tile_texture.width(), tile_texture.height()).tile(0);
    }

    @Override
    public String tag() {
        return tile_name;
    }

    @Override
    public void render(Entity entity, Graphics g) {
        PositionComponent position = entity.component(PositionComponent.class);
        if (position != null) {
            g.push(position, tile);
        }
    }

    @Override
    public void dispose() {
        tile_texture.dispose();
    }
}
