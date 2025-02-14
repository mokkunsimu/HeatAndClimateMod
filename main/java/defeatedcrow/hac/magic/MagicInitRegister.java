package defeatedcrow.hac.magic;

import defeatedcrow.hac.core.ClimateCore;
import defeatedcrow.hac.core.DCMaterialReg;
import defeatedcrow.hac.magic.block.BlockBiomeGlass;
import defeatedcrow.hac.magic.block.BlockColdLamp;
import defeatedcrow.hac.magic.block.BlockColorCube;
import defeatedcrow.hac.magic.block.BlockCubeFlame;
import defeatedcrow.hac.magic.block.BlockCubeIce;
import defeatedcrow.hac.magic.block.BlockElestial;
import defeatedcrow.hac.magic.block.BlockLotusCandle;
import defeatedcrow.hac.magic.block.BlockMorayLight;
import defeatedcrow.hac.magic.block.BlockPictureBG;
import defeatedcrow.hac.magic.block.BlockPictureGU;
import defeatedcrow.hac.magic.block.BlockPictureRW;
import defeatedcrow.hac.magic.block.BlockPictureUR;
import defeatedcrow.hac.magic.block.BlockPictureWB;
import defeatedcrow.hac.magic.block.BlockSmallLight;
import defeatedcrow.hac.magic.block.BlockTimeCage;
import defeatedcrow.hac.magic.block.BlockVeinBeacon;
import defeatedcrow.hac.magic.item.ItemArmorGemBoots;
import defeatedcrow.hac.magic.item.ItemColorBadge;
import defeatedcrow.hac.magic.item.ItemColorCard;
import defeatedcrow.hac.magic.item.ItemColorCard2;
import defeatedcrow.hac.magic.item.ItemColorCard3;
import defeatedcrow.hac.magic.item.ItemColorDrop;
import defeatedcrow.hac.magic.item.ItemColorGauntlet;
import defeatedcrow.hac.magic.item.ItemColorGauntlet2;
import defeatedcrow.hac.magic.item.ItemColorPendant;
import defeatedcrow.hac.magic.item.ItemColorPendant2;
import defeatedcrow.hac.magic.item.ItemColorRing;
import defeatedcrow.hac.magic.item.ItemColorRing2;
import defeatedcrow.hac.magic.item.ItemDebugGauntlet;
import defeatedcrow.hac.magic.item.ItemEXPGem;
import defeatedcrow.hac.magic.item.ItemMedallion;
import defeatedcrow.hac.main.ClimateMain;
import defeatedcrow.hac.main.config.ModuleConfig;
import defeatedcrow.hac.main.util.DCArmorMaterial;
import defeatedcrow.hac.main.util.DCMaterialEnum;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.EntityEquipmentSlot;

public class MagicInitRegister {

	private MagicInitRegister() {}

	public static void load() {
		if (ModuleConfig.magic) {
			loadBlocks();
			loadItems();

			loadCreativeTab();
		}
	}

	static void loadItems() {

		MagicInit.expGem = new ItemEXPGem().setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_expgem");
		DCMaterialReg.registerItem(MagicInit.expGem, ClimateCore.PACKAGE_BASE + "_expgem", ClimateMain.MOD_ID);

		MagicInit.colorDrop = new ItemColorDrop().setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_color");
		DCMaterialReg.registerItem(MagicInit.colorDrop, ClimateCore.PACKAGE_BASE + "_color", ClimateMain.MOD_ID);

		MagicInit.medallion = new ItemMedallion().setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_medallion");
		DCMaterialReg.registerItem(MagicInit.medallion, ClimateCore.PACKAGE_BASE + "_medallion", ClimateMain.MOD_ID);

		// tier1

		MagicInit.magicCard = new ItemColorCard().setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_magic_card");
		DCMaterialReg.registerItem(MagicInit.magicCard, ClimateCore.PACKAGE_BASE + "_magic_card", ClimateMain.MOD_ID);

		if (ModuleConfig.magic_advanced) {

			// tier2

			MagicInit.magicCard3 = new ItemColorCard3().setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_magic_card_m2");
			DCMaterialReg
					.registerItem(MagicInit.magicCard3, ClimateCore.PACKAGE_BASE + "_magic_card_m2", ClimateMain.MOD_ID);

			MagicInit.magicCard2 = new ItemColorCard2().setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_magic_card_m");
			DCMaterialReg
					.registerItem(MagicInit.magicCard2, ClimateCore.PACKAGE_BASE + "_magic_card_m", ClimateMain.MOD_ID);
		}

		// tier1

		MagicInit.colorRing2 = new ItemColorRing2().setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_color_ring2");
		DCMaterialReg.registerItem(MagicInit.colorRing2, ClimateCore.PACKAGE_BASE + "_color_ring2", ClimateMain.MOD_ID);

		MagicInit.colorPendant2 = new ItemColorPendant2()
				.setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_color_pendant2");
		DCMaterialReg
				.registerItem(MagicInit.colorPendant2, ClimateCore.PACKAGE_BASE + "_color_pendant2", ClimateMain.MOD_ID);

		if (ModuleConfig.magic_advanced) {

			// tier2

			MagicInit.colorRing = new ItemColorRing().setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_color_ring");
			DCMaterialReg
					.registerItem(MagicInit.colorRing, ClimateCore.PACKAGE_BASE + "_color_ring", ClimateMain.MOD_ID);

			MagicInit.colorPendant = new ItemColorPendant()
					.setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_color_pendant");
			DCMaterialReg
					.registerItem(MagicInit.colorPendant, ClimateCore.PACKAGE_BASE + "_color_pendant", ClimateMain.MOD_ID);

			// tier3

			MagicInit.colorBadge = new ItemColorBadge().setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_color_badge");
			DCMaterialReg
					.registerItem(MagicInit.colorBadge, ClimateCore.PACKAGE_BASE + "_color_badge", ClimateMain.MOD_ID);

			MagicInit.colorGauntlet = new ItemColorGauntlet()
					.setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_color_gauntlet");
			DCMaterialReg
					.registerItem(MagicInit.colorGauntlet, ClimateCore.PACKAGE_BASE + "_color_gauntlet", ClimateMain.MOD_ID);

			MagicInit.colorGauntlet2 = new ItemColorGauntlet2()
					.setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_color_gauntlet2");
			DCMaterialReg
					.registerItem(MagicInit.colorGauntlet2, ClimateCore.PACKAGE_BASE + "_color_gauntlet2", ClimateMain.MOD_ID);

			MagicInit.gemBootsBird = new ItemArmorGemBoots(DCArmorMaterial.DC_CHALCEDONY, DCMaterialEnum.CHALCEDONY,
					EntityEquipmentSlot.FEET, "blue")
							.setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_gemboots_blue");
			DCMaterialReg
					.registerItem(MagicInit.gemBootsBird, ClimateCore.PACKAGE_BASE + "_gemboots_blue", ClimateMain.MOD_ID);

			MagicInit.gemBootsFish = new ItemArmorGemBoots(DCArmorMaterial.DC_CHALCEDONY, DCMaterialEnum.CHALCEDONY,
					EntityEquipmentSlot.FEET, "green")
							.setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_gemboots_green");
			DCMaterialReg
					.registerItem(MagicInit.gemBootsFish, ClimateCore.PACKAGE_BASE + "_gemboots_green", ClimateMain.MOD_ID);

			if (ClimateCore.isDebug) {
				MagicInit.debugGauntlet = new ItemDebugGauntlet()
						.setUnlocalizedName(ClimateCore.PACKAGE_BASE + "_debug_gauntlet");
				DCMaterialReg
						.registerItem(MagicInit.debugGauntlet, ClimateCore.PACKAGE_BASE + "_debug_gauntlet", ClimateMain.MOD_ID);
				ClimateMain.magic.addSubItem(MagicInit.debugGauntlet);
			}
		}
	}

	static void loadBlocks() {
		MagicInit.colorCube = new BlockColorCube(Material.GLASS, ClimateCore.PACKAGE_BASE + "_color_cube");
		DCMaterialReg.registerBlock(MagicInit.colorCube, ClimateCore.PACKAGE_BASE + "_color_cube", ClimateMain.MOD_ID);

		MagicInit.clusterIce = new BlockCubeIce(ClimateCore.PACKAGE_BASE + "_cube_ice");
		DCMaterialReg.registerBlock(MagicInit.clusterIce, ClimateCore.PACKAGE_BASE + "_cube_ice", ClimateMain.MOD_ID);

		MagicInit.infernalFlame = new BlockCubeFlame(ClimateCore.PACKAGE_BASE + "_cube_flame");
		DCMaterialReg
				.registerBlock(MagicInit.infernalFlame, ClimateCore.PACKAGE_BASE + "_cube_flame", ClimateMain.MOD_ID);

		MagicInit.veinBeacon = new BlockVeinBeacon(ClimateCore.PACKAGE_BASE + "_beacon_vein");
		DCMaterialReg
				.registerBlock(MagicInit.veinBeacon, ClimateCore.PACKAGE_BASE + "_beacon_vein", ClimateMain.MOD_ID);

		MagicInit.elestial = new BlockElestial(Material.GLASS, ClimateCore.PACKAGE_BASE + "_ore_elestial");
		DCMaterialReg.registerBlock(MagicInit.elestial, ClimateCore.PACKAGE_BASE + "_ore_elestial", ClimateMain.MOD_ID);

		MagicInit.lotusCandle = new BlockLotusCandle(ClimateCore.PACKAGE_BASE + "_lotus_candle_white", false);
		DCMaterialReg
				.registerBlock(MagicInit.lotusCandle, ClimateCore.PACKAGE_BASE + "_lotus_candle_white", ClimateMain.MOD_ID);

		MagicInit.lotusCandleBlack = new BlockLotusCandle(ClimateCore.PACKAGE_BASE + "_lotus_candle_black", true);
		DCMaterialReg
				.registerBlock(MagicInit.lotusCandleBlack, ClimateCore.PACKAGE_BASE + "_lotus_candle_black", ClimateMain.MOD_ID);

		MagicInit.coldLamp = new BlockColdLamp(ClimateCore.PACKAGE_BASE + "_build_coldlamp");
		DCMaterialReg
				.registerBlock(MagicInit.coldLamp, ClimateCore.PACKAGE_BASE + "_build_coldlamp", ClimateMain.MOD_ID);

		MagicInit.biomeOrb = new BlockBiomeGlass(ClimateCore.PACKAGE_BASE + "_device_biomeglass");
		DCMaterialReg
				.registerBlock(MagicInit.biomeOrb, ClimateCore.PACKAGE_BASE + "_device_biomeglass", ClimateMain.MOD_ID);

		MagicInit.smallLight = new BlockSmallLight(ClimateCore.PACKAGE_BASE + "_magic_small_light");
		DCMaterialReg
				.registerBlock(MagicInit.smallLight, ClimateCore.PACKAGE_BASE + "_magic_small_light", ClimateMain.MOD_ID);

		MagicInit.morayLamp = new BlockMorayLight(ClimateCore.PACKAGE_BASE + "_magic_moray_lamp");
		DCMaterialReg
				.registerBlock(MagicInit.morayLamp, ClimateCore.PACKAGE_BASE + "_magic_moray_lamp", ClimateMain.MOD_ID);

		if (ModuleConfig.magic_advanced) {

			MagicInit.pictureBlue = new BlockPictureUR(ClimateCore.PACKAGE_BASE + "_magic_picture_u");
			DCMaterialReg
					.registerBlock(MagicInit.pictureBlue, ClimateCore.PACKAGE_BASE + "_magic_picture_u", ClimateMain.MOD_ID);

			MagicInit.pictureGreen = new BlockPictureGU(ClimateCore.PACKAGE_BASE + "_magic_picture_g");
			DCMaterialReg
					.registerBlock(MagicInit.pictureGreen, ClimateCore.PACKAGE_BASE + "_magic_picture_g", ClimateMain.MOD_ID);

			MagicInit.pictureRed = new BlockPictureRW(ClimateCore.PACKAGE_BASE + "_magic_picture_r");
			DCMaterialReg
					.registerBlock(MagicInit.pictureRed, ClimateCore.PACKAGE_BASE + "_magic_picture_r", ClimateMain.MOD_ID);

			MagicInit.pictureBlack = new BlockPictureBG(ClimateCore.PACKAGE_BASE + "_magic_picture_b");
			DCMaterialReg
					.registerBlock(MagicInit.pictureBlack, ClimateCore.PACKAGE_BASE + "_magic_picture_b", ClimateMain.MOD_ID);

			MagicInit.pictureWhite = new BlockPictureWB(ClimateCore.PACKAGE_BASE + "_magic_picture_w");
			DCMaterialReg
					.registerBlock(MagicInit.pictureWhite, ClimateCore.PACKAGE_BASE + "_magic_picture_w", ClimateMain.MOD_ID);

			MagicInit.timeCage = new BlockTimeCage(ClimateCore.PACKAGE_BASE + "_time_cage");
			DCMaterialReg
					.registerBlock(MagicInit.timeCage, ClimateCore.PACKAGE_BASE + "_time_cage", ClimateMain.MOD_ID);

		}
	}

	static void loadCreativeTab() {
		ClimateMain.magic.addSubItem(MagicInit.colorDrop);

		ClimateMain.magic.addSubItem(MagicInit.magicCard);
		if (ModuleConfig.magic_advanced) {
			ClimateMain.magic.addSubItem(MagicInit.magicCard3);
			ClimateMain.magic.addSubItem(MagicInit.magicCard2);
		}

		ClimateMain.magic.addSubItem(MagicInit.colorRing2);
		ClimateMain.magic.addSubItem(MagicInit.colorPendant2);

		if (ModuleConfig.magic_advanced) {

			ClimateMain.magic.addSubItem(MagicInit.colorRing);
			ClimateMain.magic.addSubItem(MagicInit.colorPendant);
			ClimateMain.magic.addSubItem(MagicInit.colorBadge);
			ClimateMain.magic.addSubItem(MagicInit.colorGauntlet);
			ClimateMain.magic.addSubItem(MagicInit.colorGauntlet2);
		}

		ClimateMain.magic.addSubItem(MagicInit.gemBootsBird);
		ClimateMain.magic.addSubItem(MagicInit.gemBootsFish);

		ClimateMain.magic.addSubItem(MagicInit.expGem);
		ClimateMain.magic.addSubItem(MagicInit.elestial);
		ClimateMain.magic.addSubItem(MagicInit.colorCube);

		ClimateMain.magic.addSubItem(MagicInit.medallion);
		ClimateMain.magic.addSubItem(MagicInit.lotusCandle);
		ClimateMain.magic.addSubItem(MagicInit.lotusCandleBlack);

		ClimateMain.magic.addSubItem(MagicInit.coldLamp);
		ClimateMain.magic.addSubItem(MagicInit.biomeOrb);

		if (ModuleConfig.magic_advanced) {
			ClimateMain.magic.addSubItem(MagicInit.pictureBlue);
			ClimateMain.magic.addSubItem(MagicInit.pictureGreen);
			ClimateMain.magic.addSubItem(MagicInit.pictureRed);
			ClimateMain.magic.addSubItem(MagicInit.pictureBlack);
			ClimateMain.magic.addSubItem(MagicInit.pictureWhite);

			ClimateMain.magic.addSubItem(MagicInit.timeCage);
		}
	}
}
