package io.github.nuclearfarts.blockfire.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.nuclearfarts.blockfire.BlockFireMod;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Mixin(World.class)
public class WorldMixin {
	@Inject(method = "updateEntityWithOptionalForce(Lnet/minecraft/entity/Entity;Z)V",
			at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.onUpdate()V"))
	private void beforeEntityTick(Entity entity, boolean force, CallbackInfo info) {
		if(!((World) (Object) this).isRemote) {
			BlockFireMod.pushTickingEntity(entity);
		}
	}
	
	@Inject(method = "updateEntityWithOptionalForce(Lnet/minecraft/entity/Entity;Z)V",
			at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.onUpdate()V", shift = At.Shift.AFTER))
	private void afterEntityTick(CallbackInfo info) {
		if(!((World) (Object) this).isRemote) {
			BlockFireMod.popTickingEntity();
		}
	}
}
