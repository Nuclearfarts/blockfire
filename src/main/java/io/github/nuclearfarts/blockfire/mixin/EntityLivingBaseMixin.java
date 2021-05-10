package io.github.nuclearfarts.blockfire.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.nuclearfarts.blockfire.BlockFireMod;
import io.github.nuclearfarts.blockfire.EntityTick;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin extends EntityMixin {
	@Inject(method = "attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z",
			at = @At(value = "INVOKE", target = "net/minecraft/entity/EntityLivingBase.damageShield(F)V"))
	private void onShieldBlock(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		if(!((Entity) (Object) this).world.isRemote) {
			System.out.println("Blocked " + source);
			if(BlockFireMod.blockFireFromSource(source)) {
				System.out.println("Projectile");
				Entity e = source.getImmediateSource();
				if(e != null) {
					EntityTick et = new EntityTick(e, e.ticksExisted);
					blockfire$blockedEntityTick = et;
					if(et.equals(blockfire$cancelFireOnBlock)) {
						blockfire$directlySetFire(blockfire$prevFire);
					}
				}
			}
		}
	}
}
