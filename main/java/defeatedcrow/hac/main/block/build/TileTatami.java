package defeatedcrow.hac.main.block.build;

import defeatedcrow.hac.api.blockstate.DCState;
import defeatedcrow.hac.main.api.IColorChangeTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public class TileTatami extends TileEntity implements IColorChangeTile {

	// color

	@Override
	public int getColor() {
		int color = DCState.getInt(world.getBlockState(getPos()), DCState.TYPE16);
		return color;
	}

	@Override
	public void setColor(int num) {
		if (num < 0 || num > 5) {
			num = 0;
		}
		IBlockState current = world.getBlockState(pos);
		IBlockState next = current.withProperty(DCState.TYPE16, num);
		world.setBlockState(pos, next, 3);
	}

	@Override
	public boolean rotateColor() {
		int c = getColor();
		c++;
		setColor(c);
		return true;
	}

}
