package calemiutils.util.helper;

import calemiutils.util.Location;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemHelper {

    private static final Random rand = new Random();

    public static CompoundNBT getNBT (ItemStack is) {

        if (is.getTag() == null) {
            is.setTag(new CompoundNBT());
        }

        return is.getTag();
    }

    public static String getStringFromStack (ItemStack stack) {

        if (stack.isEmpty()) {
            return "null";
        }

        return Item.getIdFromItem(stack.getItem()) + "&" + stack.getCount();
    }

    public static String getNBTFromStack (ItemStack stack) {

        if (stack.getTag() != null && stack.hasTag()) {
            return stack.getTag().toString();
        }

        return "";
    }

    public static ItemStack getStackFromString (String string) {

        if (!string.equalsIgnoreCase("null")) {

            String[] data = string.split("&");

            if (data.length == 2) {

                int itemId = Integer.parseInt(data[0]);
                int stackSize = Integer.parseInt(data[1]);

                return new ItemStack(Item.getItemById(itemId), stackSize);
            }
        }

        return ItemStack.EMPTY;
    }

    public static void attachNBTFromString (ItemStack stack, String nbtString) {

        try {
            stack.setTag(JsonToNBT.getTagFromJson(nbtString));
        }

        catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }

    public static String countByStacks (int count) {

        int remainder = (count % 64);

        return StringHelper.printCommas(count) + " blocks" + ((count > 64) ? " (" + ((int) Math.floor((float) count / 64)) + " stack(s)" + ((remainder > 0) ? (" + " + remainder + " blocks)") : ")") : "");
    }

    public static ItemEntity spawnItem (World world, Location location, ItemStack is) {

        return spawnItem(world, location.x + 0.5F, location.y + 0.5F, location.z + 0.5F, is);
    }

    private static ItemEntity spawnItem (World world, float x, float y, float z, ItemStack is) {

        ItemEntity item = new ItemEntity(world, x, y, z, is);
        item.setNoPickupDelay();
        item.setMotion(-0.05F + rand.nextFloat() * 0.1F, -0.05F + rand.nextFloat() * 0.1F, -0.05F + rand.nextFloat() * 0.1F);
        world.addEntity(item);
        return item;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static ItemEntity spawnItem (World world, Entity entity, ItemStack is) {

        return spawnItem(world, (float) entity.getPosition().getX(), (float) entity.getPosition().getY(), (float) entity.getPosition().getZ(), is);
    }

    public static void spawnItems (World world, Location location, List<ItemStack> is) {

        spawnItems(world, location.x + 0.5F, location.y + 0.5F, location.z + 0.5F, is);
    }

    private static void spawnItems (World world, float x, float y, float z, List<ItemStack> stacks) {

        for (ItemStack is : stacks) {

            ItemEntity item = new ItemEntity(world, x, y, z, is);
            item.setNoPickupDelay();
            item.setNoPickupDelay();
            item.setMotion(-0.05F + rand.nextFloat() * 0.1F, -0.05F + rand.nextFloat() * 0.1F, -0.05F + rand.nextFloat() * 0.1F);
            world.addEntity(item);
        }
    }
}
