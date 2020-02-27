package calemiutils.block.base;

import calemiutils.block.CUBlockStates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;

public class BlockColoredBase extends Block {

    public static final EnumProperty<DyeColor> COLOR = CUBlockStates.COLOR;

    protected BlockColoredBase(String name, Block.Properties properties) {

        super(properties);
        setDefaultState(this.stateContainer.getBaseState().with(COLOR, DyeColor.BLUE));
        setRegistryName(name);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {

        builder.add(COLOR);
    }
}
