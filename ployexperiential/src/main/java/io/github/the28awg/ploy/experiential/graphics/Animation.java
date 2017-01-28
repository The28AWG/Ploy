package io.github.the28awg.ploy.experiential.graphics;

import java.util.ArrayList;
import java.util.List;

import io.github.the28awg.ploy.experiential.geom.Square;

public class Animation extends Tile {

    public static final long ANIMATION_DURATION = 1000;
    private int current_index;
    private boolean repeat;
    private List<Frame> frames;
    private TileMap tile_map;
    private long last_frame_time;
    private State state;

    private Animation(Builder builder) {
        this.repeat = builder.repeat();
        this.tile_map = builder.tile_map();
        this.frames = builder.frames();
        this.current_index = 0;
        state = State.STARTED;
    }

    @Override
    public TileMap map() {
        return frame().map();
    }

    @Override
    public Square square() {
        return frame().square();
    }

    @Override
    public int index() {
        return frame().index();
    }

    @Override
    public String toString() {
        return frame().toString();
    }

    private Frame current_frame() {
        return frames.get(current_index);
    }

    public synchronized void start() {
        state = State.STARTED;
    }

    public synchronized void stop() {
        state = State.STOPPED;
    }

    public State state() {
        return state;
    }

    private Tile frame() {
        if (!frames.isEmpty() && state == State.STARTED) {
            state = State.RUNNING;
            last_frame_time = System.currentTimeMillis();
        }
        if (state == State.RUNNING) {
            if (System.currentTimeMillis() > last_frame_time + current_frame().duration()) {
                last_frame_time = System.currentTimeMillis();
                if (current_index < frames.size() - 1) {
                    current_index++;
                } else {
                    if (repeat) {
                        current_index = 0;
                    } else {
                        state = State.PAUSE;
                    }
                }
            }
            return tile_map.tile(current_frame().index());
        }
        return tile_map.tile(0);
    }

    public int width() {
        return tile_map.width();
    }

    public int height() {
        return tile_map.height();
    }

    public enum State {
        STOPPED, STARTED, RUNNING, PAUSE
    }

    private static class Frame {
        private final int index;
        private final long duration;

        private Frame(int index, long duration) {
            this.index = index;
            this.duration = duration;
        }

        public int index() {
            return index;
        }

        public long duration() {
            return duration;
        }
    }

    public static class Builder {
        private List<Frame> frames;
        private TileMap tile_map;
        private long duration;
        private boolean repeat;

        public Builder() {
            frames = new ArrayList<>();
            duration = ANIMATION_DURATION;
            repeat = true;
        }

        public Builder(TileMap tile_map) {
            this();
            tile_map(tile_map);
        }

        public List<Frame> frames() {
            return frames;
        }

        public Builder frames(List<Frame> frames) {
            this.frames = frames;
            return this;
        }

        public void frame(int index, int tile_index, long duration) {
            if (tile_index < 0 || tile_map == null || tile_index >= tile_map.indexes() || duration <= 0) {
                throw new RuntimeException("tile_index < 0 || tile_map == null || tile_index >= tile_map.indexes() || duration <= 0");
            }
            frames.add(index, new Frame(tile_index, duration));
        }

        public void frame(int tile_index, long duration) {
            if (tile_index < 0 || tile_map == null || tile_index >= tile_map.indexes() || duration <= 0) {
                throw new RuntimeException("tile_index < 0 || tile_map == null || tile_index >= tile_map.indexes() || duration <= 0");
            }
            frames.add(new Frame(tile_index, duration));
        }

        public void frame(int tile_index) {
            if (tile_index < 0 || tile_map == null || tile_index >= tile_map.indexes()) {
                throw new RuntimeException("tile_index < 0 || tile_map == null || tile_index >= tile_map.indexes()");
            }
            frames.add(new Frame(tile_index, duration));
        }

        public void frame(int index, Tile tile, long duration) {
            if (tile == null || duration <= 0) {
                throw new RuntimeException("tile == null || duration <= 0");
            }
            frames.add(index, new Frame(tile.index(), duration));
        }

        public void frame(Tile tile, long duration) {
            if (tile == null || duration <= 0) {
                throw new RuntimeException("tile == null || duration <= 0");
            }
            frames.add(new Frame(tile.index(), duration));
        }

        public void frame(Tile tile) {
            if (tile == null) {
                throw new RuntimeException("tile == null");
            }
            frames.add(new Frame(tile.index(), duration));
        }

        public void frame(Tile... tiles) {
            for (Tile tile : tiles) {
                if (tile == null) {
                    throw new RuntimeException("tile == null");
                }
                frames.add(new Frame(tile.index(), duration));
            }
        }

        public Builder all() {
            if (tile_map == null) {
                throw new RuntimeException("tile_map == null");
            }
            for (int i = 0; i < tile_map.indexes(); i++) {
                frame(i);
            }
            return this;
        }

        public TileMap tile_map() {
            return tile_map;
        }

        public Builder tile_map(TileMap tile_map) {
            if (tile_map == null || tile_map.indexes() == 0) {
                throw new RuntimeException("tile_map == null || tile_map.indexes() == 0");
            }
            this.tile_map = tile_map;
            return this;
        }

        public long duration() {
            return duration;
        }

        public Builder duration(long duration) {
            if (duration <= 0) {
                throw new RuntimeException("duration <= 0");
            }
            this.duration = duration;
            return this;
        }

        public Animation build() {
            if (tile_map == null) {
                throw new RuntimeException("tile_map == null");
            }
            if (frames.isEmpty()) {
                all();
            }
            return new Animation(this);
        }

        public boolean repeat() {
            return repeat;
        }

        public Builder repeat(boolean repeat) {
            this.repeat = repeat;
            return this;
        }
    }
}
