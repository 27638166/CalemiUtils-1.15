package calemiutils.integration.curios;

import calemiutils.init.InitItems;
import calemiutils.item.ItemTorchBelt;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CuriosIntegration {

    /**
     * Adds the Curios capability to the Wallet.
     */
    public static ICapabilityProvider walletCapability () {

        ICurio curio = new ICurio() {

            @Override
            public boolean canRightClickEquip () {
                return false;
            }

            @Override
            public boolean canEquip (String identifier, LivingEntity entityLivingBase) {
                return !CuriosAPI.getCurioEquipped(InitItems.WALLET.get(), entityLivingBase).isPresent();
            }

            @Override
            public void playEquipSound (LivingEntity entityLivingBase) {
                entityLivingBase.world.playSound(null, entityLivingBase.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        };

        return new ICapabilityProvider() {

            private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability (@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
            }
        };
    }

    /**
     * Adds the Curios capability to the Torch Belt.
     */
    public static ICapabilityProvider torchBeltCapability () {

        ICurio curio = new ICurio() {

            @Override
            public boolean canRightClickEquip () {
                return false;
            }

            @Override
            public void onCurioTick (String identifier, int index, LivingEntity livingEntity) {

                if (livingEntity instanceof PlayerEntity) {

                    PlayerEntity player = ((PlayerEntity) livingEntity);

                    if (CuriosAPI.getCurioEquipped(InitItems.TORCH_BELT.get(), player).isPresent()) {
                        ItemTorchBelt.tick(CuriosAPI.getCurioEquipped(InitItems.TORCH_BELT.get(), player).get().right, player.world, player);
                    }
                }
            }

            @Override
            public boolean canEquip (String identifier, LivingEntity entityLivingBase) {
                return !CuriosAPI.getCurioEquipped(InitItems.TORCH_BELT.get(), entityLivingBase).isPresent();
            }

            @Override
            public void playEquipSound (LivingEntity entityLivingBase) {
                entityLivingBase.world.playSound(null, entityLivingBase.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        };

        return new ICapabilityProvider() {

            private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability (@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
            }
        };
    }
}
