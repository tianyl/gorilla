package fi.utu.tech.distributed.gorilla.objects;

import fi.utu.tech.distributed.gorilla.engine.GameObject;

public interface HitHandler<X extends GameObject> {
    void activate(X target);
}
