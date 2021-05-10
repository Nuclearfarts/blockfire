package io.github.nuclearfarts.blockfire;

import net.minecraft.entity.Entity;

public interface WorldExt {
	Entity blockfire$getTickingEntity();
	void blockfire$pushTickingEntity(Entity e);
	void blockfire$popTickingEntity();
	default EntityTick blockfire$getCurrentEntityTick() {
		Entity e = blockfire$getTickingEntity();
		if(e != null) {
			return new EntityTick(e, e.ticksExisted);
		} else {
			return null;
		}
	}
}
