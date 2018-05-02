package defeatedcrow.hac.main.worldgen;

import java.util.Random;

import defeatedcrow.hac.api.blockstate.DCState;
import defeatedcrow.hac.core.DCLogger;
import defeatedcrow.hac.main.MainInit;
import defeatedcrow.hac.main.block.build.BlockSlabBase;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenCaravanBase implements IWorldGenerator {

	@Override
	public void generate(Random rand, int cx, int cz, World world, IChunkGenerator gen, IChunkProvider prov) {
		int num = CaravanGenPos.getCaravanPartNum(cx, cz, world);
		if (num > -1) {
			int cx2 = (num % 3) + cx - 1;
			int cz2 = (num / 3) + cz - 1;
			if (CaravanGenPos.canGeneratePos(cx2, cz2, world) && CaravanGenPos.canGenerateBiome(cx2, cz2, world) &&
					!CaravanGenPos.isDupe(cx2, cz2, world)) {
				if (num == 4) {
					generateCore(rand, cx, cz, world);
				} else {
					generatePart(num, rand, cx, cz, world);
				}
				DCLogger.infoLog("Caravanserai Core for Part" + num + " : " + cx2 + ", " + cz2);
			}
		}
	}

	public void generateCore(Random rand, int cx, int cz, World world) {
		int px = cx << 4;
		int pz = cz << 4;
		int py = 68;
		for (int y = -5; y < 20; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					BlockPos pos = new BlockPos(px + x, py + y, pz + z);
					IBlockState set = getCoreState(x, y, z, rand);
					world.setBlockState(pos, set, 2);
				}
			}
		}
	}

	public void generatePart(int num, Random rand, int cx, int cz, World world) {
		int px = cx << 4;
		int pz = cz << 4;
		int py = 68;

		int minX = 0;
		int minZ = 0;
		int maxX = 16;
		int maxZ = 16;
		int cx2 = (num % 3) - 1;
		int cz2 = (num / 3) - 1;
		int[] type = CaravanGenPos.getRoomNum(cx + cx2 - 1, cz + cz2 - 1, world);
		int corner = type[0] * 2 + 1;

		EnumFacing face = EnumFacing.SOUTH;
		if (num == 7) {
			face = EnumFacing.NORTH;
		}
		if (num == 3) {
			face = EnumFacing.EAST;
		}
		if (num == 5) {
			face = EnumFacing.WEST;
		}

		if (cx2 == -1) {
			maxX = 13;
		}
		if (cx2 == 1) {
			minX = 3;
		}
		if (cz2 == -1) {
			maxZ = 13;
		}
		if (cz2 == 1) {
			minZ = 3;
		}

		for (int y = -8; y < 20; y++) {
			for (int x = minX; x < maxX; x++) {
				for (int z = minZ; z < maxZ; z++) {
					BlockPos pos = new BlockPos(px + x, py + y, pz + z);
					IBlockState set = getPartBase(num, x, y, z, rand);
					world.setBlockState(pos, set, 2);
				}
			}
		}

		for (int y = 0; y < 15; y++) {
			for (int x = minX; x < maxX; x++) {
				for (int z = minZ; z < maxZ; z++) {
					BlockPos pos = new BlockPos(px + x, py + y, pz + z);
					int x1 = x;
					int z1 = z;
					if (num == 7) {
						z1 = 15 - z;
					}
					if (num == 3) {
						x1 = 15 - z;
						z1 = x;
					}
					if (num == 5) {
						x1 = z;
						z1 = 15 - x;
					}
					IBlockState set;
					if (num == corner) {
						set = getGate(num, x1, y, z1, rand);
					} else if ((num & 1) == 0) {
						set = getCornerRoom(num, x, y, z, rand);
					} else {
						set = getSmallRoom(num, x1, y, z1, rand);
					}

					if (set != null && set.getBlock() != Blocks.AIR)
						world.setBlockState(pos, set, 2);
				}
			}
		}

		int t = num / 2;
		if ((num & 1) == 1 && num != corner) {
			for (int y = 0; y < 15; y++) {
				for (int x = minX; x < maxX; x++) {
					for (int z = minZ; z < maxZ; z++) {
						int x1 = x;
						int z1 = z;
						if (num == 7) {
							z1 = 15 - z;
						}
						if (num == 3) {
							x1 = 15 - z;
							z1 = x;
						}
						if (num == 5) {
							x1 = z;
							z1 = 15 - x;
						}
						BlockPos pos = new BlockPos(px + x, py + y, pz + z);
						IBlockState set;
						switch (type[t]) {
						case 0:
							set = getInterior0(num, x1, y, z1, rand, face);
							break;
						case 1:
							set = getInterior1(num, x1, y, z1, rand, face);
							break;
						case 2:
							set = getInterior2(num, x1, y, z1, rand, face);
							break;
						case 3:
							set = getInterior3(num, x1, y, z1, rand, face);
							break;
						default:
							set = getInterior0(num, x1, y, z1, rand, face);
						}

						if (set != null && set.getBlock() != Blocks.AIR)
							world.setBlockState(pos, set, 2);
					}
				}
			}
		}

		// if (num == 1) {
		// IBlockState state = MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		// world.setBlockState(new BlockPos(px + 5, 68, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 6, 68, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 7, 68, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 8, 68, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 5, 67, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 6, 67, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 7, 67, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 8, 67, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 5, 66, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 6, 66, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 7, 66, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 8, 66, pz + 12), state, 2);
		// world.setBlockState(new BlockPos(px + 5, 67, pz + 13), state, 2);
		// world.setBlockState(new BlockPos(px + 6, 67, pz + 13), state, 2);
		// world.setBlockState(new BlockPos(px + 7, 67, pz + 13), state, 2);
		// world.setBlockState(new BlockPos(px + 8, 67, pz + 13), state, 2);
		// world.setBlockState(new BlockPos(px + 5, 66, pz + 13), state, 2);
		// world.setBlockState(new BlockPos(px + 6, 66, pz + 13), state, 2);
		// world.setBlockState(new BlockPos(px + 7, 66, pz + 13), state, 2);
		// world.setBlockState(new BlockPos(px + 8, 66, pz + 13), state, 2);
		// world.setBlockState(new BlockPos(px + 5, 66, pz + 14), state, 2);
		// world.setBlockState(new BlockPos(px + 6, 66, pz + 14), state, 2);
		// world.setBlockState(new BlockPos(px + 7, 66, pz + 14), state, 2);
		// world.setBlockState(new BlockPos(px + 8, 66, pz + 14), state, 2);
		// }
	}

	private IBlockState getCoreState(int x, int y, int z, Random rand) {
		if (y < -3) {
			if (x == 7 && z == 7 && y == -4) {
				return Blocks.IRON_BLOCK.getDefaultState();
			}
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		} else if (y == -3) {
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		} else if (y < 0) {
			if (x > 4 && x < 11 && z > 4 && z < 11) {
				if (x == 5 || x == 10 || z == 5 || z == 10) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else {
					return Blocks.WATER.getDefaultState();
				}
			}
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		} else if (y == 0) {
			if (x > 4 && x < 11 && z > 4 && z < 11) {
				if (x == 5 || x == 10 || z == 5 || z == 10) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else {
					return Blocks.WATER.getDefaultState();
				}
			} else if (x > 2 && x < 13 && z > 2 && z < 13) {
				return Blocks.GRASS.getDefaultState();
			} else if (x > 1 && x < 14 && z > 1 && z < 14) {
				return Blocks.WATER.getDefaultState();
			} else if (x > 0 && x < 15 && z > 0 && z < 15) {
				return Blocks.GRASS.getDefaultState();
			}
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 10);
		} else if (y == 1) {
			if (x > 4 && x < 11 && z > 4 && z < 11) {
				if (x == 5 && z == 5) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (x == 5 && z == 10) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (x == 10 && z == 5) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (x == 10 && z == 10) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (x == 5) {
					return MainInit.stairsDirtbrick.getDefaultState().withProperty(DCState.FACING, EnumFacing.WEST);
				} else if (x == 10) {
					return MainInit.stairsDirtbrick.getDefaultState().withProperty(DCState.FACING, EnumFacing.EAST);
				} else if (z == 5) {
					return MainInit.stairsDirtbrick.getDefaultState();
				} else if (z == 10) {
					return MainInit.stairsDirtbrick.getDefaultState().withProperty(DCState.FACING, EnumFacing.SOUTH);
				} else {
					return Blocks.AIR.getDefaultState();
				}
			} else if (x > 3 && x < 12 && z > 3 && z < 12) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
			return Blocks.AIR.getDefaultState();
		} else if (y < 4) {
			if (x == 5 && z == 5) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			} else if (x == 5 && z == 10) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			} else if (x == 10 && z == 5) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			} else if (x == 10 && z == 10) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			} else {
				return Blocks.AIR.getDefaultState();
			}
		} else if (y == 4) {
			if (x > 3 && x < 12 && z > 3 && z < 12) {
				if (x > 4 && x < 11 && z > 4 && z < 11) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else {
					return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
				}
			}
			return Blocks.AIR.getDefaultState();
		} else
			return Blocks.AIR.getDefaultState();
	}

	private IBlockState getPartBase(int num, int x, int y, int z, Random rand) {
		if (y < 0) {
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		}
		return Blocks.AIR.getDefaultState();
	}

	private IBlockState getCornerRoom(int num, int x, int y, int z, Random rand) {
		int cx2 = (num % 3) - 1;
		int cz2 = (num / 3) - 1;
		if (cx2 == 1) {
			x = 15 - x;
		}
		if (cz2 == 1) {
			z = 15 - z;
		}

		if (x == 3 && z == 2 && y == 1) {
			IBlockState door = Blocks.OAK_DOOR.getDefaultState();
			if (cz2 == -1) {
				door = door.withProperty(BlockDoor.FACING, EnumFacing.SOUTH);
			}
			return door;
		}
		if (x == 3 && z == 2 && y == 2) {
			IBlockState door = Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF,
					BlockDoor.EnumDoorHalf.UPPER);
			if (cz2 == -1) {
				door = door.withProperty(BlockDoor.FACING, EnumFacing.SOUTH);
			}
			return door;
		}

		if (x == 8 && z == 8 && y == 5) {
			return Blocks.BIRCH_FENCE.getDefaultState();
		}
		if (x == 8 && z == 8 && y == 4) {
			return MainInit.chandelierGypsum.getDefaultState();
		}
		if (x == 5 && z == 3 && y == 1) {
			return MainInit.chalLamp.getDefaultState().withProperty(DCState.TYPE16, 9);
		}

		if (y == 0) {
			if (x > 3 && x < 12 && z > 3 && z < 12) {
				return MainInit.carpetRed.getDefaultState();
			} else if (x < 11 && z < 11) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 10);
			} else {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			}
		}

		if (x > 1 && x < 13 && z > 1 && z < 13) {
			if (x == 2 || x == 12 || z == 2 || z == 12) {
				if (y < 3) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (y == 3) {
					if (rand.nextBoolean()) {
						return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
					} else {
						return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
								EnumDyeColor.YELLOW);
					}
				} else if (y < 8) {
					return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
							EnumDyeColor.YELLOW);
				} else if (y == 8) {
					return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
				}
			}
			if (x > 2 && x < 12 && z > 2 && z < 12) {
				if (y == 6) {
					return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
							EnumDyeColor.YELLOW);
				} else if (y == 7) {
					return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
				}
			}
		}

		if ((x < 2 && z == 5) || (x < 2 && z == 10) || (x == 5 && z < 2) || (x == 10 && z < 2)) {
			if (y < 3) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			} else if (y == 3) {
				if (rand.nextBoolean()) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else {
					return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
							EnumDyeColor.YELLOW);
				}
			} else if (y < 6) {
				return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
						EnumDyeColor.YELLOW);
			} else if (y == 6) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
		}
		if (x < 2 && z > 5 && z < 10) {
			if (y == 5) {
				return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
						EnumDyeColor.YELLOW);
			} else if (y == 6) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
		}
		if (z < 2 && x > 5 && x < 10) {
			if (y == 5) {
				return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
						EnumDyeColor.YELLOW);
			} else if (y == 6) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
		}
		if (x < 2 && z == 4 && y == 5) {
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		}
		if (z < 2 && x == 4 && y == 5) {
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		}

		return Blocks.AIR.getDefaultState();
	}

	private IBlockState getSmallRoom(int num, int x, int y, int z, Random rand) {
		int cx2 = (num % 3) - 1;
		int cz2 = (num / 3) - 1;
		EnumFacing face = EnumFacing.SOUTH;
		int x1 = x;
		int z1 = z;
		if (num == 7) {
			face = EnumFacing.NORTH;
		}
		if (num == 3) {
			face = EnumFacing.EAST;
		}
		if (num == 5) {
			face = EnumFacing.WEST;
		}

		if (y == 0) {
			if (z1 < 11) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 10);
			}
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		}

		if (z1 == 4) {
			if (y < 4) {
				switch (x1) {
				case 0:
				case 5:
				case 10:
				case 15:
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				default:
					return Blocks.AIR.getDefaultState();
				}
			}
			if (y == 4) {
				switch (x1) {
				case 2:
				case 3:
				case 7:
				case 8:
				case 12:
				case 13:
					return Blocks.AIR.getDefaultState();
				default:
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				}
			}
			if (y == 5) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			}
			return Blocks.AIR.getDefaultState();
		}

		if (z1 == 5 || z1 == 10) {
			if (y < 3) {
				if (z1 == 5) {
					switch (x1) {
					case 2:
					case 3:
					case 7:
					case 8:
					case 12:
					case 13:
						return Blocks.AIR.getDefaultState();
					default:
						return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
					}
				}
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			} else if (y == 3) {
				if (rand.nextBoolean()) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else {
					return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
							EnumDyeColor.YELLOW);
				}
			} else if (y < 6) {
				return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
						EnumDyeColor.YELLOW);
			} else if (y == 6) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
		}

		if (z1 > 5 && z1 < 10) {
			if (y == 5) {
				return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
						EnumDyeColor.YELLOW);
			} else if (y == 6) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
		}

		return Blocks.AIR.getDefaultState();
	}

	private IBlockState getGate(int num, int x, int y, int z, Random rand) {
		int cx2 = (num % 3) - 1;
		int cz2 = (num / 3) - 1;
		int x1 = x;
		int z1 = z;
		EnumFacing face = EnumFacing.SOUTH;
		if (num == 7) {
			face = EnumFacing.NORTH;
		}
		if (num == 3) {
			face = EnumFacing.EAST;
		}
		if (num == 5) {
			face = EnumFacing.WEST;
		}

		if (y == 0) {
			if (z1 < 11) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 10);
			}
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		}

		if (x1 == 3 && z1 == 4 && y < 3) {
			return Blocks.AIR.getDefaultState();
		}
		if (x1 == 12 && z1 == 4 && y < 3) {
			return Blocks.AIR.getDefaultState();
		}
		if (x1 == 6 && z1 == 4 && y < 5) {
			return Blocks.AIR.getDefaultState();
		}
		if (x1 == 9 && z1 == 4 && y < 5) {
			return Blocks.AIR.getDefaultState();
		}
		if (x1 == 6 && z1 == 11 && y < 5) {
			return Blocks.AIR.getDefaultState();
		}
		if (x1 == 9 && z1 == 11 && y < 5) {
			return Blocks.AIR.getDefaultState();
		}
		if (x1 == 2 && z1 == 7 && y < 3) {
			return Blocks.AIR.getDefaultState();
		}
		if (x1 == 13 && z1 == 7 && y < 3) {
			return Blocks.AIR.getDefaultState();
		}

		if (x1 == 5 && z1 == 5 && y < 7) {
			return Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, face);
		}

		if (z1 == 7 && y == 1 && (x1 == 7 || x1 == 8)) {
			return Blocks.BIRCH_FENCE_GATE.getDefaultState().withProperty(BlockFenceGate.FACING, face);
		}

		if (z1 > 6 && z1 < 9 && y == 2 && (x1 == 6 || x1 == 9)) {
			return Blocks.AIR.getDefaultState();
		}

		if (z1 > 6 && z1 < 9 && y == 7 && (x1 == 6 || x1 == 9)) {
			return Blocks.AIR.getDefaultState();
		}
		if (z1 > 6 && z1 < 9 && y == 8 && (x1 == 6 || x1 == 9)) {
			return Blocks.AIR.getDefaultState();
		}

		if (x1 < 2 && y == 5 && z1 == 4) {
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		}
		if (x1 > 13 && y == 5 && z1 == 4) {
			return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
		}

		if (x1 > 3 && x1 < 12 && (z1 == 3 || z1 == 12)) {
			if (y < 9) {
				if (x1 == 4 || x1 == 11) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else {
					return Blocks.AIR.getDefaultState();
				}
			}
			if (y == 9) {
				if (x1 > 5 && x1 < 10) {
					return Blocks.AIR.getDefaultState();
				} else {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				}
			}
			if (y == 10) {
				if (z1 == 3)
					return MainInit.stairsDirtbrick.getDefaultState().withProperty(DCState.FACING, face);
				else
					return MainInit.stairsDirtbrick.getDefaultState().withProperty(DCState.FACING, face.getOpposite());
			}
			if (y == 11) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
		}

		if ((x1 < 2 && z1 == 5) || (x1 < 2 && z1 == 10) || (x1 > 13 && z1 == 5) || (x1 > 13 && z1 == 10)) {
			if (y < 3) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			} else if (y == 3) {
				if (rand.nextBoolean()) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else {
					return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
							EnumDyeColor.YELLOW);
				}
			} else if (y < 6) {
				return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
						EnumDyeColor.YELLOW);
			} else if (y == 6) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
		}
		if (x1 < 2 && z1 > 5 && z1 < 10) {
			if (y == 5) {
				return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
						EnumDyeColor.YELLOW);
			} else if (y == 6) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
		}
		if (x1 > 13 && z1 > 5 && z1 < 10) {
			if (y == 5) {
				return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
						EnumDyeColor.YELLOW);
			} else if (y == 6) {
				return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
			}
		}

		if (x1 > 1 && x1 < 7 && z1 > 3 && z1 < 12) {
			if (x1 == 2 || x1 == 6 || z1 == 4 || z1 == 11) {
				if (y < 12) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (y == 12) {
					return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
				}
			} else {
				if (y == 11 || y == 6) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (y == 12) {
					return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
				}
			}
		}

		if (x1 > 8 && x1 < 14 && z1 > 3 && z1 < 12) {
			if (x1 == 9 || x1 == 13 || z1 == 4 || z1 == 11) {
				if (y < 12) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (y == 12) {
					return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
				}
			} else {
				if (y == 6 || y == 11) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (y == 12) {
					return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
				}
			}
		}

		if (x1 > 6 && x1 < 9 && z1 > 3 && z1 < 12) {
			if (z1 == 4 || z1 == 11) {
				if (y > 5 && y < 12) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (y == 12) {
					return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
				}
			} else {
				if (y == 6) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (y == 12) {
					return MainInit.halfSlab.getDefaultState().withProperty(BlockSlabBase.TYPE, 5);
				}
			}
		}

		return Blocks.AIR.getDefaultState();
	}

	// 普通の寝室
	private IBlockState getInterior0(int num, int x, int y, int z, Random rand, EnumFacing face) {
		if ((x == 3 || x == 8 || x == 13) && z == 4 && y == 2) {
			return MainInit.wallLamp.getDefaultState().withProperty(DCState.TYPE4, 1).withProperty(DCState.FACING,
					face.getOpposite());
		}
		if (z > 4 && z < 11) {
			if ((x == 0 || x == 1 || x == 8 || x == 14 || x == 15) && y == 1) {
				if (z == 8) {
					return Blocks.BED.getDefaultState().withProperty(BlockBed.PART,
							BlockBed.EnumPartType.FOOT).withProperty(BlockBed.FACING, face);
				}
				if (z == 9) {
					return Blocks.BED.getDefaultState().withProperty(BlockBed.PART,
							BlockBed.EnumPartType.HEAD).withProperty(BlockBed.FACING, face);
				}
			}
			if ((x == 3 || x == 8 || x == 13) && z == 5 && y < 3) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			}
			if ((x == 2 || x == 7 || x == 12) && z == 5) {
				if (y == 1) {
					return Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF,
							BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.FACING, face);
				}
				if (y == 2) {
					return Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF,
							BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.FACING, face);
				}
			}
			if (x == 5 || x == 10) {
				if (y < 3) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (y < 6) {
					return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
							EnumDyeColor.YELLOW);
				}
			}
		}
		return Blocks.AIR.getDefaultState();
	}

	// 倉庫
	private IBlockState getInterior1(int num, int x, int y, int z, Random rand, EnumFacing face) {
		if ((x == 3 || x == 8 || x == 13) && z == 4 && y == 2) {
			return MainInit.wallLamp.getDefaultState().withProperty(DCState.TYPE4, 1).withProperty(DCState.FACING,
					face.getOpposite());
		}
		if (z > 4 && z < 11) {
			if ((x == 3 || x == 8 || x == 13) && z == 5 && y < 3) {
				return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
			}
			if ((x == 2 || x == 7 || x == 12) && z == 5) {
				if (y == 1) {
					return Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF,
							BlockDoor.EnumDoorHalf.LOWER).withProperty(BlockDoor.FACING, face);
				}
				if (y == 2) {
					return Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.HALF,
							BlockDoor.EnumDoorHalf.UPPER).withProperty(BlockDoor.FACING, face);
				}
			}
			if (x == 5 || x == 10) {
				if (y < 3) {
					return MainInit.builds.getDefaultState().withProperty(DCState.TYPE16, 7);
				} else if (y < 6) {
					return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR,
							EnumDyeColor.YELLOW);
				}
			}
		}
		return Blocks.AIR.getDefaultState();
	}

	// カフェ
	private IBlockState getInterior2(int num, int x, int y, int z, Random rand, EnumFacing face) {
		if ((x == 5 || x == 10) && z == 6 && y == 3) {
			return MainInit.wallLamp.getDefaultState().withProperty(DCState.TYPE4, 1).withProperty(DCState.FACING,
					face);
		}
		if ((x == 0 || x == 15) && z == 8 && y == 4) {
			return MainInit.chandelierGypsum.getDefaultState();
		}
		if (z == 9 && y == 1) {
			switch (x) {
			case 0:
			case 4:
			case 11:
			case 15:
				return MainInit.tableWood.getDefaultState();
			default:
				return Blocks.AIR.getDefaultState();
			}
		}
		if (z == 8 && y == 1) {
			switch (x) {
			case 2:
			case 3:
			case 12:
			case 13:
				return Blocks.AIR.getDefaultState();
			case 1:
				return MainInit.stoolRed.getDefaultState().withProperty(DCState.FACING, face.rotateY());
			case 14:
				return MainInit.stoolRed.getDefaultState().withProperty(DCState.FACING, face.rotateY().getOpposite());
			default:
				return MainInit.tableWood.getDefaultState();
			}
		}
		if (z == 7 && y == 1) {
			switch (x) {
			case 5:
			case 7:
			case 9:
				return MainInit.stoolRed.getDefaultState().withProperty(DCState.FACING, face.getOpposite());
			case 0:
			case 15:
				return MainInit.tableWood.getDefaultState();
			default:
				return Blocks.AIR.getDefaultState();
			}
		}
		return Blocks.AIR.getDefaultState();
	}

	// 店
	private IBlockState getInterior3(int num, int x, int y, int z, Random rand, EnumFacing face) {
		if ((x == 5 || x == 10) && z == 6 && y == 2) {
			return MainInit.wallLamp.getDefaultState().withProperty(DCState.TYPE4, 2).withProperty(DCState.FACING,
					face);
		}
		if (z == 9 && y == 1) {
			switch (x) {
			case 0:
			case 15:
				return Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, face.getOpposite());
			case 2:
			case 13:
				return MainInit.tableWood.getDefaultState();
			default:
				return Blocks.AIR.getDefaultState();
			}
		}
		if (z == 8 && y == 1) {
			switch (x) {
			case 2:
			case 13:
				return MainInit.tableWood.getDefaultState();
			default:
				return Blocks.AIR.getDefaultState();
			}
		}
		if (z == 7 && y == 1) {
			switch (x) {
			case 0:
			case 1:
			case 2:
			case 13:
			case 14:
			case 15:
				return MainInit.tableWood.getDefaultState();
			default:
				return Blocks.AIR.getDefaultState();
			}
		}
		return Blocks.AIR.getDefaultState();
	}

}
