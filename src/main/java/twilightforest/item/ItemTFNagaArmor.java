package twilightforest.item;

import net.minecraft.entity.Entity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import twilightforest.TwilightForestMod;

public class ItemTFNagaArmor extends ItemTFArmor {
	protected ItemTFNagaArmor(ArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Rarity rarity, Properties props) {
		super(materialIn, equipmentSlotIn, rarity, props);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlotType slot, String layer) {
		if (slot == EquipmentSlotType.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "naga_scale_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "naga_scale_1.png";
		}
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> list) {
		if (isInGroup(tab)) {
			ItemStack istack = new ItemStack(this);
			switch (this.slot) {
				case CHEST:
					istack.addEnchantment(Enchantments.FIRE_PROTECTION, 3);
					break;
				case LEGS:
					istack.addEnchantment(Enchantments.PROTECTION, 3);
					break;
				default:
					break;
			}
			list.add(istack);
		}
	}
}
