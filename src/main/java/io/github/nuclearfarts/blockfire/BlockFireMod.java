package io.github.nuclearfarts.blockfire;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(modid = BlockFireMod.MODID, name = "BlockFire", version = "@VERSION@")
public class BlockFireMod {
	public static final String MODID = "blockfire";
	private String[] configEntities;
	private static final Set<Class<?>> ENTITY_CLASSES = new HashSet<>();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		configEntities = config.get("misc", "EntitiesToBlockFireFrom", new String[] {"minecraft:arrow"}, "Entities which, when blocked, should have any fire effects they applied (or will apply) this tick cancelled.").getStringList();
		config.save();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for(String s : configEntities) {
			ENTITY_CLASSES.add(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(s)).getEntityClass());
		}
	}
	
	public static boolean blockFireFromSource(DamageSource source) {
		return blockFireFromEntity(source.getImmediateSource());
	}
	
	public static boolean blockFireFromEntity(Entity e) {
		return e != null && ENTITY_CLASSES.contains(e.getClass());
	}
}
