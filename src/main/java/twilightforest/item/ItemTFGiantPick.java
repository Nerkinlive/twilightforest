package twilightforest.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.block.BlockTFGiantBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFGiantPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFGiantPick(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
		this.attackDamage = 8 + material.getAttackDamage();
		this.attackSpeed = -3.5F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getTranslationKey() + ".tooltip"));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		// extra 64X strength vs giant obsidian
		destroySpeed *= (state.getBlock() == TFBlocks.giant_obsidian) ? 64 : 1;
		// 64x strength vs giant blocks
		return state.getBlock() instanceof BlockTFGiantBlock ? destroySpeed * 64 : destroySpeed;
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EquipmentSlotType equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(TFItems.GIANT_REACH_MODIFIER, "Tool modifier", 2.5, 0));
		}

		return multimap;
	}
}
