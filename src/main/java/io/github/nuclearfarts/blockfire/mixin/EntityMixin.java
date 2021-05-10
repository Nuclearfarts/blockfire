package io.github.nuclearfarts.blockfire.mixin;

import net.minecraft.entity.Entity;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.nuclearfarts.blockfire.BlockFireMod;
import io.github.nuclearfarts.blockfire.EntityTick;

@Mixin(Entity.class)
public class EntityMixin {
	@Shadow private int fire;
	protected int blockfire$prevFire;
	protected EntityTick blockfire$blockedEntityTick;
	protected EntityTick blockfire$cancelFireOnBlock;
	
	@Inject(method = "setFire(I)V", at = @At(value = "FIELD", target = "fire:I", opcode = Opcodes.PUTFIELD), cancellable = true)
	private void captureFireSet(CallbackInfo info) {
		if(!((Entity) (Object) this).world.isRemote) {
			EntityTick t = BlockFireMod.getCurrentEntityTick();
			if(t != null && t.equals(blockfire$blockedEntityTick)) {
				info.cancel();
				return;
			}
			if(BlockFireMod.isTickingEntityProjectile()) {
				System.out.println("fire-cancel entity to " + BlockFireMod.getTickingEntity());
				blockfire$cancelFireOnBlock = BlockFireMod.getCurrentEntityTick();
			}
			System.out.println("Prev fire: " + fire);
			System.out.println("Ticking entity: " + BlockFireMod.getTickingEntity());
			blockfire$prevFire = fire;
		}
	}
	
	/*@Inject(method = "onUpdate()V", at = @At("HEAD"))
	private void onStartEntityTick(CallbackInfo info) {
		if(!((Entity) (Object) this).world.isRemote) {
			System.out.println("Tick " + this);
			BlockFireMod.pushTickingEntity((Entity) (Object) this);
		}
	}
	
	@Inject(method = "onUpdate()V", at = @At("RETURN"))
	private void onEndEntityTick(CallbackInfo info) {
		if(!((Entity) (Object) this).world.isRemote) {
			System.out.println("End tick " + this);
			BlockFireMod.popTickingEntity();
		}
	}*/
	
	@Inject(method = "updateRidden()V",
			at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.onUpdate()V"))
	private void beforeRiddenUpdate(CallbackInfo info) {
		if(!((Entity) (Object) this).world.isRemote) {
			BlockFireMod.pushTickingEntity((Entity) (Object) this);
		}
	}
	
	@Inject(method = "updateRidden()V",
			at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.onUpdate()V", shift = At.Shift.AFTER))
	private void afterRiddenUpdate(CallbackInfo info) {
		if(!((Entity) (Object) this).world.isRemote) {
			BlockFireMod.popTickingEntity();
		}
	}
	
	protected void blockfire$directlySetFire(int to) {
		fire = to;
	}
}
