package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TileEntityTFCinderFurnace;

import java.util.Random;

public class BlockTFCinderFurnace extends Block implements ModelRegisterCallback {

	private static boolean keepInventory;
	private final boolean isBurning;

	BlockTFCinderFurnace(boolean isLit) {
		super(Material.WOOD);

		this.isBurning = isLit;

		this.setHardness(7.0F);

		this.setLightLevel(isLit ? 1F : 0);

		if (!isLit) {
			this.setCreativeTab(TFItems.creativeTab);
		}

		this.setDefaultState(blockState.getBaseState().withProperty(BlockFurnace.FACING, Direction.NORTH));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockFurnace.FACING);
	}

	@Override
	public int getMetaFromState(BlockState state) {
		return state.getValue(BlockFurnace.FACING).getHorizontalIndex();
	}

	@Override
	@Deprecated
	public BlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BlockFurnace.FACING, Direction.byHorizontalIndex(meta));
	}

	@Override
	public TileEntity createTileEntity(World world, BlockState state) {
		return new TileEntityTFCinderFurnace();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && world.getTileEntity(pos) instanceof TileEntityTFCinderFurnace) {
			player.openGui(TwilightForestMod.instance, TwilightForestMod.GUI_ID_FURNACE, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, EntityLivingBase placer, ItemStack itemStack) {
		if (itemStack.hasDisplayName()) {
			((TileEntityFurnace) world.getTileEntity(pos)).setCustomInventoryName(itemStack.getDisplayName());
		}
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {
		BlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (active) {
			worldIn.setBlockState(pos, TFBlocks.cinder_furnace_lit.getDefaultState().withProperty(BlockFurnace.FACING, iblockstate.getValue(BlockFurnace.FACING)), 3);
		} else {
			worldIn.setBlockState(pos, TFBlocks.cinder_furnace.getDefaultState().withProperty(BlockFurnace.FACING, iblockstate.getValue(BlockFurnace.FACING)), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (this.isBurning) {
			Blocks.LIT_FURNACE.randomDisplayTick(state, world, pos, random);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, BlockState state) {
		if (!keepInventory) {
			TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntityFurnace) {
				InventoryHelper.dropInventoryItems(world, pos, (TileEntityFurnace) tileentity);
				world.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public Item getItemDropped(BlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(TFBlocks.cinder_furnace);
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {
		return new ItemStack(Item.getItemFromBlock(TFBlocks.cinder_furnace));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModel() {
		if (!isBurning) {
			ModelRegisterCallback.super.registerModel();
		}
	}
}
