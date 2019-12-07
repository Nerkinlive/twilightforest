package twilightforest.block;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFConfig;
import twilightforest.enums.Leaves3Variant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import java.util.List;
import java.util.Random;

public class BlockTFLeaves3 extends BlockLeaves implements ModelRegisterCallback {

	public static final IProperty<Leaves3Variant> VARIANT = PropertyEnum.create("variant", Leaves3Variant.class);

	protected BlockTFLeaves3() {
		this.setCreativeTab(TFItems.creativeTab);
		this.setLightOpacity(1);
		this.setDefaultState(
				blockState.getBaseState()
						.withProperty(CHECK_DECAY, true)
						.withProperty(DECAYABLE, true)
						.withProperty(VARIANT, Leaves3Variant.THORN)
		);
	}

	@Override
	public int getLightOpacity(BlockState state) {
		return TFConfig.performance.leavesLightOpacity;
	}

	// [VanillaCopy] BlockLeavesNew.getMetaFromState - could subclass, but different VARIANT property
	@Override
	public int getMetaFromState(BlockState state) {
		int i = 0;
		i |= state.getValue(VARIANT).ordinal();

		if (!state.getValue(DECAYABLE)) {
			i |= 4;
		}

		if (state.getValue(CHECK_DECAY)) {
			i |= 8;
		}

		return i;
	}

	// [VanillaCopy] BlockLeavesNew.getStateFromMeta - could subclass, but different VARIANT property
	@Override
	@Deprecated
	public BlockState getStateFromMeta(int meta) {
		int variant = meta & 3;
		final Leaves3Variant[] values = Leaves3Variant.values();

		return getDefaultState()
				.withProperty(VARIANT, values[variant % values.length])
				.withProperty(DECAYABLE, (meta & 4) == 0)
				.withProperty(CHECK_DECAY, (meta & 8) > 0);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE, VARIANT);
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta) {
		return BlockPlanks.EnumType.OAK;
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, BlockState state) {
		return new ItemStack(this, 1, state.getValue(VARIANT).ordinal());
	}

	@Override
	public Item getItemDropped(BlockState state, Random random, int fortune) {
		return TFItems.magic_beans;
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		int n = Leaves3Variant.values().length;
		for (int i = 0; i < n; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public boolean canBeReplacedByLeaves(BlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return NonNullList.withSize(1, new ItemStack(this, 1, world.getBlockState(pos).getValue(VARIANT).ordinal()));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModel() {
		IStateMapper stateMapper = new StateMap.Builder().ignore(CHECK_DECAY, DECAYABLE).build();
		ModelLoader.setCustomStateMapper(this, stateMapper);
		ModelUtils.registerToStateSingleVariant(this, VARIANT, stateMapper);
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, Direction face) {
		return 60;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, Direction face) {
		return 30;
	}

	@Override
	public ItemStack getSilkTouchDrop(BlockState state) {
		return new ItemStack(this, 1, state.getValue(VARIANT).ordinal());
	}
}
