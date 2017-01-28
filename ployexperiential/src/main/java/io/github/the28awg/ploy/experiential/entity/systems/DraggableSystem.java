package io.github.the28awg.ploy.experiential.entity.systems;

import io.github.the28awg.ploy.experiential.Platform;
import io.github.the28awg.ploy.experiential.entity.DimensionComponent;
import io.github.the28awg.ploy.experiential.entity.Entity;
import io.github.the28awg.ploy.experiential.entity.EntityManager;
import io.github.the28awg.ploy.experiential.entity.PositionComponent;
import io.github.the28awg.ploy.experiential.entity.components.DraggableComponent;
import io.github.the28awg.ploy.experiential.geom.XY;
import io.github.the28awg.ploy.experiential.input.Input;

public class DraggableSystem {

    private static Entity old_found_drag;

    public static void drag(XY distance) {

        boolean found = false;
        if (old_found_drag != null) {
            PositionComponent position = old_found_drag.component(PositionComponent.class);
            found = true;
            position.xy(position.x() - distance.x(), position.y() + distance.y());
        } else {
            for (Entity other : EntityManager.entities(PositionComponent.class, DimensionComponent.class, DraggableComponent.class)) {
                if (other.component(DraggableComponent.class).accept()) {
                    PositionComponent position = other.component(PositionComponent.class);
                    if (XY.contains(position, other.component(DimensionComponent.class), Input.xy())) {
                        found = true;
                        position.xy(position.x() - distance.x(), position.y() + distance.y());
                        old_found_drag = other;
                        old_found_drag.component(DraggableComponent.class).dragged(true);
                        break;
                    }
                }
            }
        }
        if (!found) {
            if (old_found_drag != null) {
                old_found_drag.component(DraggableComponent.class).dragged(false);
                old_found_drag = null;
            }
            XY offset = Platform.offset();
            offset.xy(offset.x() - distance.x(), offset.y() + distance.y());
            Platform.offset(offset);
        }
    }

    public static void done() {
        if (old_found_drag != null) {
            old_found_drag.component(DraggableComponent.class).dragged(false);
            old_found_drag = null;
        }
    }

    public static boolean dragged() {
        return old_found_drag != null;
    }
}
