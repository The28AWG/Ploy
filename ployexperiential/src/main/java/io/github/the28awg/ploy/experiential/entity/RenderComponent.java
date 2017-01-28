package io.github.the28awg.ploy.experiential.entity;

import java.util.ArrayList;

/**
 * Created by the28awg on 18.01.17.
 */

public class RenderComponent extends ArrayList<String> implements Component {
    public RenderComponent(String... tags) {
        for (String tag : tags) {
            add(tag);
        }
    }

    public RenderComponent render(String... tags) {
        for (String tag : tags) {
            add(tag);
        }
        return this;
    }
}
