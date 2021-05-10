package io.github.nuclearfarts.blockfire.mixin;

import java.util.ArrayDeque;
import java.util.Deque;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.nuclearfarts.blockfire.WorldExt;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Mixin(World.class)
public class WorldMixin implements WorldExt {
	private final @Unique Deque<Entity> tickingEntities = new ArrayDeque<>();
	
	@Inject(method = "updateEntityWithOptionalForce(Lnet/minecraft/entity/Entity;Z)V",
			at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.onUpdate()V"))
	private void beforeEntityTick(Entity entity, boolean force, CallbackInfo info) {
		blockfire$pushTickingEntity(entity);
	}
	
	@Inject(method = "updateEntityWithOptionalForce(Lnet/minecraft/entity/Entity;Z)V",
			at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.onUpdate()V", shift = At.Shift.AFTER))
	private void afterEntityTick(CallbackInfo info) {
		blockfire$popTickingEntity();
	}

	@Override
	public Entity blockfire$getTickingEntity() {
		return tickingEntities.peek();
	}

	@Override
	public void blockfire$pushTickingEntity(Entity e) {
		tickingEntities.push(e);
	}

	@Override
	public void blockfire$popTickingEntity() {
		tickingEntities.pop();
	}
}
