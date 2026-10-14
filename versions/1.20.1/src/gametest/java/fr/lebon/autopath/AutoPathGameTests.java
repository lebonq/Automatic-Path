package fr.lebon.autopath;

import fr.lebon.autopath.blocks.PathBlock;
import fr.lebon.autopath.entity.PathEntity;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.GameTest;
import fr.lebon.autopath.compat.VersionSupport;
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

public class AutoPathGameTests implements FabricGameTest {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void resourcesLoad(TestContext context) {
        var server = context.getWorld().getServer();
        for (String name : new String[]{"lawn_dirt", "lawn_grass"}) {
            var key = VersionSupport.identifier(name);
            context.assertTrue(server.getRecipeManager().get(key).isPresent(), "Missing recipe: " + name);
        }
        for (String name : new String[]{"lawn", "path"}) {
            var key = VersionSupport.identifier("blocks/" + name);
            context.assertTrue(server.getLootManager().getLootTable(key) != LootTable.EMPTY,
                "Missing loot table: " + name);
        }
        context.assertTrue(AutoPath.PATH_BLOCK.getDefaultState().isIn(BlockTags.DIRT), "Path dirt tag missing");
        context.assertTrue(AutoPath.LAWN_BLOCK.getDefaultState().isIn(BlockTags.SHOVEL_MINEABLE), "Lawn shovel tag missing");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void pathDataSurvivesSave(TestContext context) {
        var pos = new BlockPos(2, 1, 2);
        context.setBlockState(pos, AutoPath.PATH_BLOCK);
        PathEntity entity = (PathEntity) context.getBlockEntity(pos);
        entity.nbTick = 1234;
        entity.permanent = true;
        entity.timesWalkOn = 7;
        var registries = context.getWorld().getRegistryManager();
        var nbt = entity.createNbtWithIdentifyingData();
        var restored = (PathEntity) BlockEntity.createFromNbt(entity.getPos(), entity.getCachedState(), nbt);
        context.assertTrue(restored != null, "Path block entity did not deserialize");
        context.assertTrue(restored.nbTick == 1234 && restored.permanent && restored.timesWalkOn == 7,
            "Saved path progress was lost");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
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
}
