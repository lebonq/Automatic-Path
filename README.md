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

## Publishing with GitHub Actions

The **Publish mod** workflow (`.github/workflows/release.yml`) builds the mod,
runs the game tests, and publishes the remapped JAR to CurseForge, Modrinth,
or both using [Mod Publish Plugin](https://modmuss50.github.io/mod-publish-plugin/).

1. Create the mod project on each destination you want to use.
2. In GitHub, open **Settings → Secrets and variables → Actions** and configure:

   | Kind | Name | Value |
   | --- | --- | --- |
   | Secret | `CURSEFORGE_TOKEN` | CurseForge upload API token |
   | Secret | `MODRINTH_TOKEN` | Modrinth personal access token |
   | Variable | `CURSEFORGE_PROJECT_ID` | Numeric project ID; defaults to this project's existing `460405` |
   | Variable | `MODRINTH_PROJECT_ID` | Your Modrinth project ID |

   Only configure credentials for the destinations you select. Create tokens in
   [CurseForge account settings](https://legacy.curseforge.com/account/api-tokens)
   and [Modrinth account settings](https://modrinth.com/settings/pats).
   The Modrinth token needs **Create versions**, **Read versions**, and **Write versions**
   permissions, as described in the [plugin documentation](https://modmuss50.github.io/mod-publish-plugin/platforms/modrinth/).
3. Update `mod_version` in `gradle.properties` and write release notes in `changelog.md`.
   The published version includes Minecraft's version, for example `1.5+mc1.21.11`.
   Use a new mod version for each subsequent release on the same Minecraft version.
4. Commit and push the changes, including the workflow. The workflow must be on
   your repository's default branch to appear in the Actions menu.
5. Open **Actions → Publish mod → Run workflow**. Choose the branch, destination,
   and release channel (`stable`, `beta`, or `alpha`). Leave **dry_run** enabled
   for a preview, then download `release-artifacts` to inspect the JAR and upload metadata.
6. Run the workflow again with **dry_run** disabled to publish.

If one destination succeeds and the other fails, rerun with only the failed
destination selected to avoid uploading the successful release again.

You can also preview locally with JDK 21:

```sh
./gradlew build
MODRINTH_PROJECT_ID=YOUR_PROJECT_ID ./gradlew publishMods -Ppublish_dry_run=true
```

Use `-Ppublish_target=curseforge` or `-Ppublish_target=modrinth` to select one
destination, and `-Prelease_type=beta` for a beta. Dry-run mode defaults to true.
For a real local upload, export the selected platform's token and project ID
as environment variables and explicitly pass `-Ppublish_dry_run=false`.
