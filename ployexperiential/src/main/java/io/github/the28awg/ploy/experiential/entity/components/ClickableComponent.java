package io.github.the28awg.ploy.experiential.entity.components;

import io.github.the28awg.ploy.experiential.entity.Component;

/**
 * Created by the28awg on 21.01.17.
 */

public class ClickableComponent implements Component {

    private boolean click;

    public boolean click() {
        return click;
    }

    public ClickableComponent click(boolean click) {
        this.click = click;
        return this;
    }
}
