package io.github.the28awg.ploy.experiential.entity.systems;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.the28awg.ploy.experiential.entity.Entity;
import io.github.the28awg.ploy.experiential.entity.EntityManager;
import io.github.the28awg.ploy.experiential.entity.RenderComponent;
import io.github.the28awg.ploy.experiential.graphics.Graphics;
import io.github.the28awg.ploy.experiential.graphics.Renderer;

/**
 * Created by the28awg on 18.01.17.
 */

public class RenderSystem {

    private static final Map<String, Renderer> RENDERER_CACHE = new ConcurrentHashMap<>();

    public static void enable(Renderer renderer) {
        RENDERER_CACHE.put(renderer.tag(), renderer);
    }

    public static void disable(Renderer renderer) {
        disable(renderer.tag());
    }

    public static void disable(String tag) {
        Renderer renderer = RENDERER_CACHE.get(tag);
        if (renderer != null) {
            renderer.dispose();
            RENDERER_CACHE.remove(tag);
        }
    }

    public static void render(Graphics graphics) {
        for (Entity entity : EntityManager.entities(RenderComponent.class)) {
            for (String tag : entity.component(RenderComponent.class)) {
                Renderer renderer = RENDERER_CACHE.get(tag);
                if (renderer != null) {
                    renderer.render(entity, graphics);
                }
            }
        }
    }
}
