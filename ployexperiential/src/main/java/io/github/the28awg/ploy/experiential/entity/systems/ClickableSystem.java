package io.github.the28awg.ploy.experiential.entity.systems;

import io.github.the28awg.ploy.experiential.entity.DimensionComponent;
import io.github.the28awg.ploy.experiential.entity.Entity;
import io.github.the28awg.ploy.experiential.entity.EntityManager;
import io.github.the28awg.ploy.experiential.entity.PositionComponent;
import io.github.the28awg.ploy.experiential.entity.components.ClickableComponent;
import io.github.the28awg.ploy.experiential.geom.XY;
import io.github.the28awg.ploy.experiential.input.Input;

/**
 * Created by the28awg on 21.01.17.
 */

public class ClickableSystem {

    private static Entity old_clickable_entity;

    public static void update(boolean down) {
        boolean found = false;
        if (old_clickable_entity != null) {
            ClickableComponent clickableComponent = old_clickable_entity.component(ClickableComponent.class);
            PositionComponent positionComponent = old_clickable_entity.component(PositionComponent.class);
            DimensionComponent dimensionComponent = old_clickable_entity.component(DimensionComponent.class);
            if (XY.contains(positionComponent, dimensionComponent, Input.xy())) {
                found = true;
                clickableComponent.click(down);
                if (!down) {
                    old_clickable_entity = null;
                }
            } else {
                clickableComponent.click(false);
                old_clickable_entity = null;
            }
        }
        if (!found && !down) {
            for (Entity entity : EntityManager.entities(ClickableComponent.class, DimensionComponent.class, PositionComponent.class)) {
                ClickableComponent clickableComponent = entity.component(ClickableComponent.class);
                PositionComponent positionComponent = entity.component(PositionComponent.class);
                DimensionComponent dimensionComponent = entity.component(DimensionComponent.class);
                if (XY.contains(positionComponent, dimensionComponent, Input.xy())) {
                    found = true;
                    clickableComponent.click(true);
                    old_clickable_entity = entity;
                }
            }
        }
    }

    public static boolean clicked() {
        return old_clickable_entity != null;
    }

    public static Entity entity() {
        return old_clickable_entity;
    }
}
