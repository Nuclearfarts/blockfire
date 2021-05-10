package io.github.nuclearfarts.blockfire.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.nuclearfarts.blockfire.BlockFireMod;
import io.github.nuclearfarts.blockfire.EntityTick;
import io.github.nuclearfarts.blockfire.WorldExt;

@Mixin(Entity.class)
public class EntityMixin {
	@Shadow private int fire;
	protected int blockfire$prevFire;
	protected EntityTick blockfire$blockedEntityTick;
	protected EntityTick blockfire$cancelFireOnBlock;
	
	@Inject(method = "setFire(I)V", at = @At(value = "FIELD", target = "fire:I", opcode = Opcodes.PUTFIELD), cancellable = true)
	private void captureFireSet(CallbackInfo info) {
		World world = ((Entity) (Object) this).world;
		WorldExt worldExt = (WorldExt) world;
		EntityTick t = worldExt.blockfire$getCurrentEntityTick();
		if(t != null && t.equals(blockfire$blockedEntityTick)) {
			info.cancel();
			return;
		}
		if(BlockFireMod.isEntityProjectile(worldExt.blockfire$getTickingEntity())) {
			blockfire$cancelFireOnBlock = t;
		}
		blockfire$prevFire = fire;
	}
	
	@Inject(method = "updateRidden()V",
			at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.onUpdate()V"))
	private void beforeRiddenUpdate(CallbackInfo info) {
		Entity self = (Entity) (Object) this;
		((WorldExt) self.world).blockfire$pushTickingEntity(self);
	}
	
	@Inject(method = "updateRidden()V",
			at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.onUpdate()V", shift = At.Shift.AFTER))
	private void afterRiddenUpdate(CallbackInfo info) {
		((WorldExt) ((Entity) (Object) this).world).blockfire$popTickingEntity();
	}
	
	protected void blockfire$directlySetFire(int to) {
		fire = to;
	}
}
