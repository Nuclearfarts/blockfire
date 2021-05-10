package io.github.nuclearfarts.blockfire;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(modid = BlockFireMod.MODID, name = "BlockFire", version = "@VERSION@")
public class BlockFireMod {
	public static final String MODID = "blockfire";
	
	public static boolean blockFireFromSource(DamageSource source) {
		return source.isProjectile();
	}
	
	public static boolean isEntityProjectile(Entity e) {
		return e instanceof IProjectile;
	}
}
