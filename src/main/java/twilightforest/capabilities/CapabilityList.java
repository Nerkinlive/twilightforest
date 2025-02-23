package twilightforest.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.capabilities.fan.FeatherFanCapabilityHandler;
import twilightforest.capabilities.fan.FeatherFanFallCapability;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.capabilities.shield.ShieldCapabilityHandler;
import twilightforest.capabilities.thrown.YetiThrowCapability;
import twilightforest.capabilities.thrown.YetiThrowCapabilityHandler;

import javax.annotation.Nonnull;

public class CapabilityList {

	public static final Capability<IShieldCapability> SHIELDS = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<FeatherFanFallCapability> FEATHER_FAN_FALLING = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<YetiThrowCapability> YETI_THROWN = CapabilityManager.get(new CapabilityToken<>() {});

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IShieldCapability.class);
		event.register(FeatherFanFallCapability.class);
		event.register(YetiThrowCapability.class);
	}

	public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> e) {
		if (e.getObject() instanceof LivingEntity living) {
			e.addCapability(IShieldCapability.ID, new ICapabilitySerializable<CompoundTag>() {

				final LazyOptional<IShieldCapability> inst = LazyOptional.of(() -> {
					ShieldCapabilityHandler i = new ShieldCapabilityHandler();
					i.setEntity(living);
					return i;
				});

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
					return SHIELDS.orEmpty(capability, inst.cast());
				}

				@Override
				public CompoundTag serializeNBT() {
					return inst.orElseThrow(NullPointerException::new).serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt) {
					inst.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
				}
			});

			e.addCapability(FeatherFanFallCapability.ID, new ICapabilitySerializable<CompoundTag>() {

				private final LazyOptional<FeatherFanFallCapability> inst = LazyOptional.of(() -> {
					FeatherFanCapabilityHandler cap = new FeatherFanCapabilityHandler();
					cap.setEntity(living);
					return cap;
				});

				@Override
				public CompoundTag serializeNBT() {
					return inst.orElseThrow(NullPointerException::new).serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt) {
					inst.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
				}

				@Override
				public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
					return FEATHER_FAN_FALLING.orEmpty(cap, inst.cast());
				}
			});

			e.addCapability(YetiThrowCapability.ID, new ICapabilitySerializable<CompoundTag>() {

				private final LazyOptional<YetiThrowCapability> inst = LazyOptional.of(() -> {
					YetiThrowCapabilityHandler cap = new YetiThrowCapabilityHandler();
					cap.setEntity(living);
					return cap;
				});

				@Override
				public CompoundTag serializeNBT() {
					return inst.orElseThrow(NullPointerException::new).serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt) {
					inst.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
				}

				@Override
				public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
					return YETI_THROWN.orEmpty(cap, inst.cast());
				}
			});
		}
	}
}
