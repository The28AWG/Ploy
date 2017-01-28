package io.github.the28awg.ploy.experiential.graphics;

import java.util.HashMap;
import java.util.Map;

import io.github.the28awg.ploy.experiential.geom.Dimension;
import io.github.the28awg.ploy.experiential.geom.Square;
import io.github.the28awg.ploy.experiential.geom.XY;

public class TileMap {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private Texture texture;
    private int width;
    private int height;
    private int max_columns;
    private int max_rows;
    private int max_index;
    private Map<String, Tile> tile_map;

    public TileMap(Texture texture) {
        this(texture, TILE_WIDTH, TILE_HEIGHT);
    }

    public TileMap(Texture texture, Dimension dimension) {
        this(texture, (int) dimension.width(), (int) dimension.height());
    }

    public TileMap(Texture texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.max_columns = texture.width() / width;
        this.max_rows = texture.height() / height;
        this.max_index = this.max_columns * this.max_rows;
        this.tile_map = new HashMap<>();
    }

    private Tile tile(int column, int row) {
        if (column < 0 || row < 0 || column >= max_columns || row >= max_rows) {
            throw new RuntimeException("column >= max_columns || row >= max_rows");
        }

        String key = column + "_" + row;
        if (tile_map.containsKey(key)) {
            return tile_map.get(key);
        }
        float y = 1.0f / ((float) texture.height() / (float) (row * width));
        float x = 1.0f / ((float) texture.width() / (float) (column * height));
        float w = 1.0f / ((float) texture.height() / (float) ((row * width) + width));
        float h = 1.0f / ((float) texture.width() / (float) ((column * height) + height));
//        Square square = new Square(new XY(y, x),new XY(w, x), new XY(y, h), new XY(w,h));
        Square square = new Square(new XY(x, y), new XY(h, y), new XY(x, w), new XY(h, w));
        Tile tile = new Tile(this, square, column, row, (row * max_columns) + column);
        tile_map.put(key, tile);
        return tile;
    }

    public Tile tile(int index) {
        if (index < 0 || index >= max_index) {
            throw new RuntimeException("index >= max_index");
        }
        int row = index / max_columns;
        int column = index % max_columns;
        return tile(column, row);
    }

    public Texture texture() {
        return texture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int columns() {
        return max_columns;
    }

    public int rows() {
        return max_rows;
    }

    public int indexes() {
        return max_index;
    }

    @Override
    public String toString() {
        return "[texture = " + texture + ", width = " + width + ", height = " + height + ", max(columns = " + max_columns + ", rows = " + max_rows + ", index = " + max_index + ")]";
    }
}
