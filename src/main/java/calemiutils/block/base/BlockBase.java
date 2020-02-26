package calemiutils.block.base;

import net.minecraft.block.Block;

public class BlockBase extends Block {

    public BlockBase(String name, Properties properties) {

        super(properties);
        setRegistryName(name);
    }
}
