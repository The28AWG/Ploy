package io.github.the28awg.ploy.experiential.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.github.the28awg.ploy.experiential.entity.components.DraggableComponent;
import io.github.the28awg.ploy.experiential.entity.components.LayerComponent;

/**
 * Created by the28awg on 18.01.17.
 */

public class EntityManager {

    private static final EntityComparator COMPARATOR = new EntityComparator();
    private static List<Entity> entities = new CopyOnWriteArrayList<>();

    public static List<Entity> entities(Class... types) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : entities) {
            boolean found = true;
            for (Class type : types) {
                if (!entity.has(type)) {
                    found = false;
                }
            }
            if (found) {
                result.add(entity);
            }
        }
        Collections.sort(result, COMPARATOR);
        return result;
    }

    public static void add(Entity entity) {
        entities.add(entity);
    }

    public static void clear() {
        entities.clear();
    }

    private static class EntityComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity lhs, Entity rhs) {
            int e1_mass = 0;
            int e2_mass = 0;
            LayerComponent e1_layer = lhs.component(LayerComponent.class);
            if (e1_layer != null) {
                switch (e1_layer.layerLevelStrategy()) {
                    case BACKGROUND:
                        e1_mass = 1;
                        break;
                    case FORGROUND:
                        e1_mass = 2;
                        break;
                    case UI:
                        e1_mass = 3;
                        break;
                }
            }
            DraggableComponent e1_draggable = lhs.component(DraggableComponent.class);
            if (e1_draggable != null) {
                if (e1_draggable.dragged()) {
                    e1_mass += 4;
                }
            }
            LayerComponent e2_layer = rhs.component(LayerComponent.class);
            if (e2_layer != null) {
                switch (e2_layer.layerLevelStrategy()) {
                    case BACKGROUND:
                        e2_mass = 1;
                        break;
                    case FORGROUND:
                        e2_mass = 2;
                        break;
                    case UI:
                        e2_mass = 3;
                        break;
                }
            }
            DraggableComponent e2_draggable = rhs.component(DraggableComponent.class);
            if (e2_draggable != null) {
                if (e2_draggable.dragged()) {
                    e2_mass += 4;
                }
            }
            if (e1_mass > e2_mass) {
                return 1;
            } else if (e1_mass < e2_mass) {
                return -1;
            }
            return 0;
        }
    }
}
