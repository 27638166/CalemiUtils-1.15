package calemiutils.event;

import calemiutils.init.InitItems;
import calemiutils.tileentity.TileEntityMobBeacon;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AddTradesEvent {

    /**
     * Handles adding new villager trades.
     */
    @SubscribeEvent
    public void onVillagerTrade (VillagerTradesEvent event) {

        if (event.getType() == VillagerProfession.LIBRARIAN) {
            event.getTrades().get(1).add((entity, random) -> new MerchantOffer(new ItemStack(InitItems.COIN_QUARTER.get(), 1), new ItemStack(Items.EMERALD, 1), 128, 0, 0.05F));
        }
    }
}
