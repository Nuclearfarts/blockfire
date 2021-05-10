package io.github.nuclearfarts.blockfire;

import net.minecraft.entity.Entity;

public final class EntityTick {
	public final Entity entity;
	public final int entityAge;
	public EntityTick(Entity entity, int entityAge) {
		this.entity = entity;
		this.entityAge = entityAge;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof EntityTick)) return false;
		EntityTick other = (EntityTick) o;
		return other.entity == entity && other.entityAge == this.entityAge;
	}
}
