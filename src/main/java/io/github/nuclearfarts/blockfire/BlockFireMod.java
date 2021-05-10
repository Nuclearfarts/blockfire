package io.github.nuclearfarts.blockfire;

import java.util.ArrayDeque;
import java.util.Deque;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(modid = BlockFireMod.MODID, name = "BlockFire", version = "@VERSION@")
public class BlockFireMod {
	public static final String MODID = "blockfire";
	private static final Deque<Entity> TICKING_ENTITIES = new ArrayDeque<>();
	
	public static boolean blockFireFromSource(DamageSource source) {
		return source.isProjectile();
	}
	
	public static boolean isEntityProjectile(Entity e) {
		return e instanceof IProjectile;
	}
	
	public static boolean isTickingEntityProjectile() {
		return isEntityProjectile(getTickingEntity());
	}
	
	public static Entity getTickingEntity() {
		return TICKING_ENTITIES.peek();
	}
	
	public static EntityTick getCurrentEntityTick() {
		Entity e = getTickingEntity();
		if(e != null) {
			return new EntityTick(e, e.ticksExisted);
		} else {
			return null;
		}
	}
	
	public static void pushTickingEntity(Entity e) {
		TICKING_ENTITIES.push(e);
	}
	
	public static void popTickingEntity() {
		TICKING_ENTITIES.pop();
	}
}
