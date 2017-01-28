package io.github.the28awg.ploy.experiential.entity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by the28awg on 18.01.17.
 */

public class Entity {

    private CopyOnWriteArrayList<Component> components;
    private UUID uuid;
    private String identifier;

    public Entity() {
        this.components = new CopyOnWriteArrayList<>();
        this.identifier = UUID.randomUUID().toString();
        this.uuid = UUID.randomUUID();
    }

    public Entity(String identifier) {
        this.components = new CopyOnWriteArrayList<>();
        this.identifier = identifier;
        this.uuid = UUID.randomUUID();
    }

    public Entity(String identifier, String uuid) {
        this.components = new CopyOnWriteArrayList<>();
        this.identifier = identifier;
        this.uuid = UUID.fromString(uuid);
    }

    public Entity(Component... components) {
        this.components = new CopyOnWriteArrayList<>();
        this.identifier = UUID.randomUUID().toString();
        this.uuid = UUID.randomUUID();
        add(components);
    }

    public Entity(List<Component> components) {
        this.components = new CopyOnWriteArrayList<>();
        this.identifier = UUID.randomUUID().toString();
        this.uuid = UUID.randomUUID();
        this.components.addAll(components);
    }

    public Entity(String identifier, Component... components) {
        this.components = new CopyOnWriteArrayList<>();
        this.identifier = identifier;
        this.uuid = UUID.randomUUID();
        add(components);
    }

    public Entity(String identifier, String uuid, Component... components) {
        this.components = new CopyOnWriteArrayList<>();
        this.identifier = identifier;
        this.uuid = UUID.fromString(uuid);
        add(components);
    }

    public Entity(String identifier, List<Component> components) {
        this.components = new CopyOnWriteArrayList<>();
        this.identifier = identifier;
        this.uuid = UUID.randomUUID();
        this.components.addAll(components);
    }

    public Entity(String identifier, String uuid, List<Component> components) {
        this.components = new CopyOnWriteArrayList<>();
        this.identifier = identifier;
        this.uuid = UUID.fromString(uuid);
        this.components.addAll(components);
    }

    public List<Component> components() {
        return Collections.unmodifiableList(components);
    }

    public UUID uuid() {
        return uuid;
    }

    public String identifier() {
        return identifier;
    }

    public Entity add(Component... components) {
        Collections.addAll(this.components, components);
        return this;
    }

    public void remove(Component component) {
        components.remove(component);
    }

    public <T extends Component> boolean has(Class<T> type) {
        return null != component(type);
    }

    public <T extends Component> T component(Class<T> type) {
        for (Component component : this.components) {
            if (component.getClass().isAssignableFrom(type)) {
                return type.cast(component);
            }
        }
        return null;
    }
}
