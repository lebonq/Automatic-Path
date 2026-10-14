package fr.lebon.autopath.compat;

import fr.lebon.autopath.AutoPath;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import java.util.Optional;

/** API bindings for Minecraft 1.20.1; no gameplay rules belong here. */
public final class VersionSupport {
    private VersionSupport() {}

    public static Identifier identifier(String name) {
        return new Identifier(AutoPath.MOD_ID, name);
    }

    public static AbstractBlock.Settings blockSettings(String name) {
        return AbstractBlock.Settings.create().hardness(0.5f).sounds(BlockSoundGroup.GRASS);
    }

    public static Item.Settings itemSettings(String name) {
        return new Item.Settings();
    }

    public static Block shortGrass() {
        return Blocks.GRASS;
    }

    public static Optional<RegistryEntry.Reference<PlacedFeature>> bonemealFeature(ServerWorld world) {
        return world.getRegistryManager().get(RegistryKeys.PLACED_FEATURE).getEntry(VegetationPlacedFeatures.GRASS_BONEMEAL);
    }

    public static void damageHoe(ItemUsageContext context, PlayerEntity player) {
        context.getStack().damage(1, player, entity -> entity.sendToolBreakStatus(context.getHand()));
    }

    public static ActionResult success(World world) {
        return ActionResult.success(world.isClient());
    }
}
