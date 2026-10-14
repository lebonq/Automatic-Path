[![Cursedstats](http://cf.way2muchnoise.eu/automatic-path.svg)](https://www.curseforge.com/minecraft/mc-mods/automatic-path) [![Cursedstats](http://cf.way2muchnoise.eu/versions/automatic-path.svg)](https://www.curseforge.com/minecraft/mc-mods/automatic-path)

# Automatic-Path

Little Minecraft fabric mod. Which transforms grass into non-permanent path where players, mobs and monsters walk.      
    
[Fabric Loader](https://fabricmc.net/use/ "Download Fabric Loader")   
![Fabric API needed](https://i.imgur.com/5J6bV5W.png)

## Path Creation

**Path creation by mobs is disable by default**.  

There is an option if you want for paths to be permanent. At the maximum state if you walk 12 times on a state 5 block it will become permanent. You can change this amount .  

## Blocks

### Path Block

![gif_path](https://raw.githubusercontent.com/lebonq/Automatic-Path/master/images_description/PathBlock.gif)

Replace grass where you walk the most !
You can plant sugar cane, sapling, grass, flower, on the path but only if it is grassier enough.
And also transform them into farmland by using a hoe.   

### Lawn Block

![Lawn Block](https://raw.githubusercontent.com/lebonq/Automatic-Path/master/images_description/lawn_block.png)

Craft a beautiful lawn with :  
- 1 dirt + 1 bone meals to get 1 lawn block.  
- 1 grass block + 1 bone meals to get 4 lawn block. 

The lawn block is 100% walk-proof and never transform into path. And no matter which biome you are it will keep its beautiful green color !
And sheeps can never run out of grass with it !

## Configuration
You can change the amount of times between 2 states.   
Enable path creation by mobs.
Enable permanent path.  
Enable permanent path to turn into vanilla dirt path block.

- You can do in-game via ModMenu.   
- Or for server and client whitout ModMenu you can configure it directly in the file whit is config/autopath.json    

## Screens

![Screen 1](https://raw.githubusercontent.com/lebonq/Automatic-Path/master/images_description/screen1.png)
Creates path for the player...


![Screen 2](https://raw.githubusercontent.com/lebonq/Automatic-Path/master/images_description/screen2.png)
...but also for mobs/monsters !- - 

## Supported Fabric versions

The `main` branch builds separate Fabric JARs for Minecraft **1.20.1**, **1.21.1**,
and **1.21.11**. Each JAR supports its named Minecraft version only.

Use **JDK 21** to run Gradle. The 1.20.1 artifact is compiled for Java 17;
the 1.21.1 and 1.21.11 artifacts require Java 21.

```sh
# Build and run the game tests for one target:
./gradlew build -Pminecraft_version=1.20.1
./gradlew build -Pminecraft_version=1.21.1
./gradlew build -Pminecraft_version=1.21.11

# Build and test all three targets sequentially:
bash scripts/build-all.sh
```

The default target is 1.21.11. Outputs are isolated under
`build/<minecraft-version>/libs/`, for example
`build/1.20.1/libs/autopath-mc1.20.1-1.5.jar`.
Development worlds are isolated under `run/<minecraft-version>/`.
Use the same `-Pminecraft_version=...` option with `runClient`, `runServer`,
and `publishMods` to select a target.

Gameplay code, configuration, mixins, and canonical resources live in `src/main`.
Minecraft API adapters, block implementations, rendering, and game tests live in
`versions/<minecraft-version>/src/`. Dependency versions are pinned in each
version's `gradle.properties`. The resource task adapts the shared recipes,
loot tables, tags, and item rendering files to the selected Minecraft format.

CI builds and tests every target on Linux and Windows. The **Publish mod**
workflow has a Minecraft version selector and publishes only that target's JAR.
Published version identifiers include Minecraft's version (such as
`1.5+mc1.20.1`) so the three releases can coexist on Modrinth.
