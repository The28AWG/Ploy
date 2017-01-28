package io.github.the28awg.ploy.experiential.graphics;

import io.github.the28awg.ploy.experiential.geom.Square;

public class Tile {
    private final Square square;
    private final TileMap tile_map;
    private final int column;
    private final int row;
    private final int index;

    Tile(TileMap tile_map, Square square, int column, int row, int index) {
        this.square = square;
        this.tile_map = tile_map;
        this.column = column;
        this.row = row;
        this.index = index;
    }

    Tile() {
        square = null;
        tile_map = null;
        column = 0;
        row = 0;
        index = 0;
    }

    public TileMap map() {
        return tile_map;
    }

    public Square square() {
        return square;
    }

    public int index() {
        return index;
    }

    public int width() {
        return tile_map != null ? tile_map.width() : 0;
    }

    public int height() {
        return tile_map != null ? tile_map.height() : 0;
    }

    @Override
    public String toString() {
        return "[column = " + column + ", row = " + row + ", index = " + index + ", tile map = " + tile_map + ", square = " + square + "]";
    }
}