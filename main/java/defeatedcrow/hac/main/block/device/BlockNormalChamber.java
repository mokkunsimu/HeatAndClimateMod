package defeatedcrow.hac.main.block.device;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import defeatedcrow.hac.api.blockstate.DCState;
import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.climate.IClimate;
import defeatedcrow.hac.api.climate.IHeatTile;
import defeatedcrow.hac.core.ClimateCore;
import defeatedcrow.hac.core.base.DCTileBlockFaced;
import defeatedcrow.hac.main.ClimateMain;
import defeatedcrow.hac.main.util.DCName;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNormalChamber extends DCTileBlockFaced implements IHeatTile {
	public BlockNormalChamber(Material m, String s, int max) {
		super(m, s, max);
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		EnumFacing face = DCState.getFace(state, DCState.FACING);
		return face != null && side != face.getOpposite();
	}

	@Override
	public boolean onRightClick(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!player.world.isRemote && player != null && hand == EnumHand.MAIN_HAND) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileChamberBase) {
				player.openGui(ClimateMain.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileNormalChamber();
	}

	@Override
	public List<ItemStack> getSubItemList() {
		List<ItemStack> list = Lists.newArrayList();
		list.add(new ItemStack(this, 1, 0));
		return list;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		// block側の気候処理は無し
	}

	@Override
	public boolean onClimateChange(World world, BlockPos pos, IBlockState state, IClimate clm) {
		return false;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public DCHeatTier getHeatTier(World world, BlockPos from, BlockPos to) {
		IBlockState state = world.getBlockState(to);
		int meta = DCState.getInt(state, DCState.TYPE4);
		if (meta == 1) {
			TileEntity t = world.getTileEntity(to);
			if (t instanceof TileChamberBase) {
				DCHeatTier heat = ((TileChamberBase) t).getCurrentHeatTier();
				return heat;
			}
			return DCHeatTier.BOIL;
		}
		return DCHeatTier.NORMAL;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		int meta = this.getMetaFromState(state) & 3;
		return meta == 1 ? 15 : 0;
	}

	public static void changeLitState(World world, BlockPos pos, boolean f) {
		IBlockState state = world.getBlockState(pos);
		int m = DCState.getInt(state, DCState.TYPE4);
		if (m >= 0) {
			int power = m & 2;
			if (f) {
				world.setBlockState(pos, state.withProperty(DCState.TYPE4, 1 + power), 3);
			} else {
				world.setBlockState(pos, state.withProperty(DCState.TYPE4, power), 3);
			}
		}
	}

	public static void changePowerState(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		int m = DCState.getInt(state, DCState.TYPE4);
		if (m >= 0) {
			int lit = m & 1;
			boolean power = (m & 2) == 0;
			if (power) {
				world.setBlockState(pos, state.withProperty(DCState.TYPE4, lit + 2), 3);
			} else {
				world.setBlockState(pos, state.withProperty(DCState.TYPE4, lit), 3);
			}
		}
	}

	public static boolean isLit(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		int meta = DCState.getInt(state, DCState.TYPE4);
		return meta == 1 || meta == 3;
	}

	public static boolean isPower(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		int meta = DCState.getInt(state, DCState.TYPE4);
		return meta == 0 || meta == 1;
	}
	// redstone

	@Override
	public void onNeighborChange(IBlockState state, World world, BlockPos pos, Block block, BlockPos from) {
		if (!world.isRemote) {
			boolean flag = world.isBlockPowered(pos);
			if (flag || block.getDefaultState().canProvidePower()) {
				int m = DCState.getInt(state, DCState.TYPE4);
				int lit = m & 1;
				boolean flag2 = m == 1;
				boolean power = (m & 2) != 0;
				if (flag && !power) {
					world.setBlockState(pos, state.withProperty(DCState.TYPE4, lit + 2), 3);
					world.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
				} else if (!flag && power) {
					world.setBlockState(pos, state.withProperty(DCState.TYPE4, lit), 3);
					world.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.5F);
				}
			}
		}
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (state != null && BlockNormalChamber.isLit(world, pos)) {
			int c = ClimateMain.proxy.getParticleCount();
			if (ClimateMain.proxy.getParticleCount() > 0 && rand.nextInt(c) == 0) {
				EnumFacing face = DCState.getFace(state, DCState.FACING).getOpposite();
				double x = pos.getX() + 0.5D + face.getFrontOffsetX() * 0.5D + rand.nextDouble() * 0.15D;
				double y = pos.getY() + 0.25D + rand.nextDouble() * 0.15D;
				double z = pos.getZ() + 0.5D + face.getFrontOffsetZ() * 0.5D + rand.nextDouble() * 0.15D;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (ClimateCore.proxy.isShiftKeyDown()) {
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Requirement ===");
			tooltip.add(DCName.FUEL_SOLID.getLocalizedName());
			tooltip.add(DCName.AIR.getLocalizedName() + ": " + TextFormatting.AQUA.toString() + "WIND");
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Output ===");
			tooltip.add(DCName.HEAT.getLocalizedName() + ": " + TextFormatting.RED.toString() + "SMELTING");
			tooltip.add(I18n.format("dcs.tip.chamber"));
			tooltip.add(TextFormatting.YELLOW.toString() + TextFormatting.BOLD.toString() + "=== Tips ===");
			tooltip.add(I18n.format("dcs.tip.chamber2"));
			tooltip.add(DCName.RS.getLocalizedName() + ": " + DCName.TURN_OFF.getLocalizedName());
			tooltip.add(TextFormatting.AQUA.toString() + DCName.COLOR_CHANGE_TARGET.getLocalizedName());
		} else {
			tooltip.add(TextFormatting.ITALIC.toString() + "=== Lshift key: expand tooltip ===");
		}
		tooltip.add(TextFormatting.BOLD.toString() + "Tier 1");
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
}
