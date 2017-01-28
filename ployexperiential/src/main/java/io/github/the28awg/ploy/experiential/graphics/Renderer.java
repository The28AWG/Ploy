package io.github.the28awg.ploy.experiential.graphics;

import io.github.the28awg.ploy.experiential.entity.Entity;
import io.github.the28awg.ploy.experiential.io.Disposable;

/**
 * Created by the28awg on 02.01.17.
 */

public interface Renderer extends Disposable {

    String tag();

    void render(Entity entity, Graphics g);

}
