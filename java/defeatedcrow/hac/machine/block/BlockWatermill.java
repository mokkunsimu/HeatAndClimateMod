package defeatedcrow.hac.machine.block;

import javax.annotation.Nullable;

import defeatedcrow.hac.api.blockstate.DCState;
import defeatedcrow.hac.api.blockstate.EnumSide;
import defeatedcrow.hac.api.energy.IWrenchDC;
import defeatedcrow.hac.core.energy.BlockTorqueBase;
import defeatedcrow.hac.main.achievement.AchievementClimate;
import defeatedcrow.hac.main.achievement.AcvHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWatermill extends BlockTorqueBase {

	public BlockWatermill(String s) {
		super(Material.ROCK, s, 0);
		this.setSoundType(SoundType.METAL);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileWatermill();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			@Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (player != null && heldItem != null && heldItem.getItem() instanceof IWrenchDC) {
			TileEntity tile = world.getTileEntity(pos);
			// achievement
			if (!player.hasAchievement(AchievementClimate.MACHINE_CHANGE)) {
				AcvHelper.addMachineAcievement(player, AchievementClimate.MACHINE_CHANGE);
			}
		}
		return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
		if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
			if (placer != null) {
				EnumFacing face = placer.getHorizontalFacing();
				facing = face.getOpposite();
			} else {
				facing = EnumFacing.NORTH;
			}
		}
		state = state.withProperty(DCState.SIDE, EnumSide.fromFacing(facing.getOpposite()));
		return state;
	}
}
