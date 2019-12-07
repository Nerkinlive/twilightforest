package twilightforest.structures.minotaurmaze;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;

import java.util.Random;

public class ComponentTFMazeDeadEndTorches extends ComponentTFMazeDeadEnd {

	public ComponentTFMazeDeadEndTorches() {
		super();
	}

	public ComponentTFMazeDeadEndTorches(TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(feature, i, x, y, z, rotation);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// normal doorway
		super.addComponentParts(world, rand, sbb);

		// torches!
		this.fillWithBlocks(world, sbb, 2, 1, 4, 3, 4, 4, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, Direction.SOUTH), AIR, false);
		this.fillWithBlocks(world, sbb, 1, 1, 1, 1, 4, 4, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, Direction.WEST), AIR, false);
		this.fillWithBlocks(world, sbb, 4, 1, 1, 4, 4, 4, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, Direction.EAST), AIR, false);


		return true;
	}
}
