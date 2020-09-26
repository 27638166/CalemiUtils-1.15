package calemiutils.block.base;

import calemiutils.CalemiUtils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

/**
 * The base class for Items that place Blocks.
 */
public class BlockItemBase extends BlockItem {

    public BlockItemBase (Block block) {
        super(block, new Item.Properties().group(CalemiUtils.TAB));
    }
}
