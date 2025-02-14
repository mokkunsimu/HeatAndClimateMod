package defeatedcrow.hac.main.client.gui;

import defeatedcrow.hac.core.util.DCUtil;
import defeatedcrow.hac.main.block.build.TileDisplayShopCase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDisplayShopCase extends Container {

	public final TileDisplayShopCase tile;
	public final InventoryPlayer playerInv;

	private int[] current = new int[5];

	public ContainerDisplayShopCase(TileDisplayShopCase chest, EntityPlayer player, boolean flag) {
		this.tile = chest;
		this.playerInv = player.inventory;
		chest.openInventory(player);

		if (flag) {
			this.addSlotToContainer(new SlotDisplay(chest, 0, 26, 22));
			this.addSlotToContainer(new SlotDisplay(chest, 1, 107, 22));
		} else {
			this.addSlotToContainer(new Slot(chest, 0, 14, 21));
			this.addSlotToContainer(new Slot(chest, 1, 95, 21));
			this.addSlotToContainer(new SlotInvalid(chest, 2, 146, 55));
		}

		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlotToContainer(new Slot(playerInv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(playerInv, l, 8 + l * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tile.isUsableByPlayer(playerIn);
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, tile);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icrafting = this.listeners.get(i);

			for (int j = 0; j < this.tile.getFieldCount(); j++) {
				if (this.current[j] != this.tile.getField(j)) {
					icrafting.sendWindowProperty(this, j, this.tile.getField(j));
				}
			}
		}

		for (int k = 0; k < this.tile.getFieldCount(); k++) {
			this.current[k] = this.tile.getField(k);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.tile.setField(id, data);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		int lim = this.inventorySlots.size() - 36;

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < lim) {
				if (!this.mergeItemStack(itemstack1, lim, this.inventorySlots.size(), true))
					return ItemStack.EMPTY;
				slot.onSlotChange(itemstack1, itemstack);
			} else if (!this.mergeItemStack(itemstack1, 0, lim, false))
				return ItemStack.EMPTY;

			if (!DCUtil.isEmpty(itemstack1)) {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
				return null;

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		this.tile.closeInventory(player);
	}

}
