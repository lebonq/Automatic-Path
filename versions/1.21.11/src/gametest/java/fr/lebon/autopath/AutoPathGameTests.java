package fr.lebon.autopath;

import fr.lebon.autopath.blocks.PathBlock;
import fr.lebon.autopath.entity.PathEntity;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AutoPathGameTests {
    @GameTest
    public void resourcesLoad(TestContext context) {
        var server = context.getWorld().getServer();
        for (String name : new String[]{"lawn_dirt", "lawn_grass"}) {
            var key = RegistryKey.of(RegistryKeys.RECIPE, Identifier.of(AutoPath.MOD_ID, name));
            context.assertTrue(server.getRecipeManager().get(key).isPresent(), "Missing recipe: " + name);
        }
        for (String name : new String[]{"lawn", "path"}) {
            var key = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(AutoPath.MOD_ID, "blocks/" + name));
            context.assertTrue(server.getReloadableRegistries().getLootTable(key) != LootTable.EMPTY,
                "Missing loot table: " + name);
        }
        context.assertTrue(AutoPath.PATH_BLOCK.getDefaultState().isIn(BlockTags.DIRT), "Path dirt tag missing");
        context.assertTrue(AutoPath.LAWN_BLOCK.getDefaultState().isIn(BlockTags.SHOVEL_MINEABLE), "Lawn shovel tag missing");
        context.complete();
    }

    @GameTest
    public void pathDataSurvivesSave(TestContext context) {
        var pos = new BlockPos(2, 1, 2);
        context.setBlockState(pos, AutoPath.PATH_BLOCK);
        PathEntity entity = context.getBlockEntity(pos, PathEntity.class);
        entity.nbTick = 1234;
        entity.permanent = true;
        entity.timesWalkOn = 7;
        var registries = context.getWorld().getRegistryManager();
        var nbt = entity.createNbtWithIdentifyingData(registries);
        var restored = (PathEntity) BlockEntity.createFromNbt(entity.getPos(), entity.getCachedState(), nbt, registries);
        context.assertTrue(restored != null, "Path block entity did not deserialize");
        context.assertTrue(restored.nbTick == 1234 && restored.permanent && restored.timesWalkOn == 7,
            "Saved path progress was lost");
        context.complete();
    }

    @GameTest
    public void sheepEatOnlyGrassyPathsAndPreserveLawn(TestContext context) {
        var ground = new BlockPos(2, 1, 2);
        var sheep = context.spawnEntity(EntityType.SHEEP, ground.up());
        sheep.setAiDisabled(true);
        var goal = new EatGrassGoal(sheep);

        context.setBlockState(ground, AutoPath.PATH_BLOCK.getDefaultState().with(PathBlock.STATE_RENDER, 5));
        sheep.setSheared(true);
        goal.start();
        for (int i = 0; i < 50; i++) goal.tick();
        context.assertTrue(context.getBlockState(ground).isOf(AutoPath.PATH_BLOCK) && sheep.isSheared(),
            "Bare path should not feed sheep");

        context.setBlockState(ground, AutoPath.LAWN_BLOCK);
        goal.start();
        for (int i = 0; i < 50; i++) goal.tick();
        context.assertTrue(context.getBlockState(ground).isOf(AutoPath.LAWN_BLOCK) && !sheep.isSheared(),
            "Lawn should feed sheep without being consumed");
        context.assertTrue(goal.getTimer() == 0, "Eating timer must finish");

        context.setBlockState(ground, AutoPath.PATH_BLOCK.getDefaultState().with(PathBlock.STATE_RENDER, 3));
        sheep.setSheared(true);
        goal.start();
        for (int i = 0; i < 50; i++) goal.tick();
        context.assertTrue(context.getBlockState(ground).isOf(Blocks.DIRT) && !sheep.isSheared(),
            "Grassy path should feed sheep and become dirt");
        context.complete();
    }

    @GameTest
    public void existingPathsUseUpdatedConfig(TestContext context) {
        var holder = me.shedaniel.autoconfig.AutoConfig.getConfigHolder(fr.lebon.autopath.config.AutoPathConfig.class);
        var original = holder.getConfig();
        try {
            var initial = new fr.lebon.autopath.config.AutoPathConfig();
            initial.upgradeTime = 100;
            initial.downgradeTime = 100;
            holder.setConfig(initial);
            var pos = new BlockPos(2, 1, 2);
            context.setBlockState(pos, AutoPath.PATH_BLOCK);
            var world = context.getWorld();
            var absolutePos = context.getAbsolutePos(pos);
            var entity = (PathEntity) world.getBlockEntity(absolutePos);

            // Replace the whole config object after the block entity already exists.
            var updated = new fr.lebon.autopath.config.AutoPathConfig();
            updated.upgradeTime = 1;
            updated.downgradeTime = 100;
            holder.setConfig(updated);
            entity.nbTick = 20;
            PathEntity.tick(world, absolutePos, context.getBlockState(pos).with(PathBlock.STEPPED, true), entity);
            context.assertTrue(context.getBlockState(pos).get(PathBlock.STATE_RENDER) == 2,
                "Existing path ignored the new upgrade interval");

            // In-place edits must also take effect without recreating the entity.
            updated.downgradeTime = 1;
            entity.nbTick = 20;
            PathEntity.tick(world, absolutePos, context.getBlockState(pos), entity);
            context.assertTrue(context.getBlockState(pos).get(PathBlock.STATE_RENDER) == 1,
                "Existing path ignored the new downgrade interval");

            updated.permanentPath = true;
            updated.steppedBeforePermanent = 2;
            updated.permanentAsDirtPath = true;
            context.setBlockState(pos, AutoPath.PATH_BLOCK.getDefaultState().with(PathBlock.STATE_RENDER, 5));
            entity.timesWalkOn = 7;
            entity.nbTick = 20;
            PathEntity.tick(world, absolutePos, context.getBlockState(pos).with(PathBlock.STEPPED, true), entity);
            context.assertTrue(context.getBlockState(pos).isOf(Blocks.DIRT_PATH),
                "Existing path ignored the new permanence settings");
        } finally {
            holder.setConfig(original);
        }
        context.complete();
    }

    @GameTest
    public void mobPathCreationUsesUpdatedConfig(TestContext context) {
        var holder = me.shedaniel.autoconfig.AutoConfig.getConfigHolder(fr.lebon.autopath.config.AutoPathConfig.class);
        var original = holder.getConfig();
        try {
            var config = new fr.lebon.autopath.config.AutoPathConfig();
            config.enableMobPathCreation = false;
            holder.setConfig(config);
            var ground = new BlockPos(2, 1, 2);
            var sheep = context.spawnEntity(EntityType.SHEEP, ground.up());
            sheep.setAiDisabled(true);
            var world = context.getWorld();
            var absolutePos = context.getAbsolutePos(ground);
            context.setBlockState(ground, Blocks.GRASS_BLOCK);
            Blocks.GRASS_BLOCK.onSteppedOn(world, absolutePos, context.getBlockState(ground), sheep);
            context.assertTrue(context.getBlockState(ground).isOf(Blocks.GRASS_BLOCK),
                "Disabled mob path creation changed grass");

            var updated = new fr.lebon.autopath.config.AutoPathConfig();
            updated.enableMobPathCreation = true;
            holder.setConfig(updated);
            Blocks.GRASS_BLOCK.onSteppedOn(world, absolutePos, context.getBlockState(ground), sheep);
            context.assertTrue(context.getBlockState(ground).isOf(AutoPath.PATH_BLOCK),
                "Mob path creation ignored updated config");

            context.setBlockState(ground, Blocks.STONE);
            Blocks.STONE.onSteppedOn(world, absolutePos, context.getBlockState(ground), sheep);
            context.assertTrue(context.getBlockState(ground).isOf(Blocks.STONE),
                "Stepping on an ineligible block changed it");
        } finally {
            holder.setConfig(original);
        }
        context.complete();
    }
}
