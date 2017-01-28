package io.github.the28awg.ploy.experiential.entity.systems;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.the28awg.ploy.experiential.entity.Entity;
import io.github.the28awg.ploy.experiential.entity.EntityManager;
import io.github.the28awg.ploy.experiential.entity.PositionComponent;
import io.github.the28awg.ploy.experiential.entity.components.DraggableComponent;
import io.github.the28awg.ploy.experiential.entity.components.LayerComponent;
import io.github.the28awg.ploy.experiential.entity.components.LocationComponent;
import io.github.the28awg.ploy.experiential.geom.XY;

public class LocationSystem {

    private static final Map<LayerComponent.LayerLevelStrategy, MagnetLayer> MAGNET_MAP = new ConcurrentHashMap<>();

    public static void magnets(LayerComponent.LayerLevelStrategy strategy, MagnetLayer layer) {
        MAGNET_MAP.put(strategy, layer);
    }

    public static void update() {
        for (Entity entity : EntityManager.entities(PositionComponent.class, LayerComponent.class, LocationComponent.class)) {
            DraggableComponent draggableComponent = entity.component(DraggableComponent.class);
            if (draggableComponent != null && draggableComponent.dragged()) {
                continue;
            }
            LocationComponent locationComponent = entity.component(LocationComponent.class);
            LayerComponent.LayerLevelStrategy layerLevelStrategy = entity.component(LayerComponent.class).layerLevelStrategy();
            PositionComponent positionComponent = entity.component(PositionComponent.class);
            if (!new XY(locationComponent).equals(new XY(positionComponent))) {
                MagnetLayer magnets = MAGNET_MAP.get(layerLevelStrategy);
                Magnet tmp = new Magnet(locationComponent);
                double d = XY.distance(positionComponent, tmp);
                if (magnets != null) {
                    for (Magnet magnet : magnets) {
                        double tmp_d = XY.distance(positionComponent, magnet);
                        if (tmp_d < d) {
                            tmp = magnet;
                            d = tmp_d;
                        }
                    }
                    locationComponent.xy(tmp);
                }
                if (d > 100) {
                    positionComponent.xy(XY.interpolationByDistance(locationComponent, positionComponent, d / 2));
                } else {
                    positionComponent.xy(locationComponent);
                }
            }
        }
    }

    public static LocationComponent location(PositionComponent positionComponent) {
        return new LocationComponent(math32(positionComponent.x()), math32(positionComponent.y()));
    }

    private static float math32(float math) {
        return (float) ((int) (math % 32)) * 32.0f;
    }

    public static class Magnet extends XY {

        public Magnet() {
        }

        public Magnet(float x, float y) {
            super(x, y);
        }

        public Magnet(XY xy) {
            super(xy);
        }
    }

    public static class MagnetLayer extends ArrayList<Magnet> {
        private String tag;

        public MagnetLayer(String tag) {
            this.tag = tag;
        }

        public MagnetLayer(String tag, Magnet... magnets) {
            this.tag = tag;
            add(magnets);
        }

        public String tag() {
            return tag;
        }

        public void add(Magnet... magnets) {
            for (Magnet magnet : magnets) {
                add(magnet);
            }
        }
    }
}
