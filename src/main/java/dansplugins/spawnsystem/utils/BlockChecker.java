package dansplugins.spawnsystem.utils;

import org.bukkit.block.Block;

public class BlockChecker {

    // Signs are recognised by material name rather than by a list of known sign materials, so wood types
    // added after the API version this plugin builds against - crimson, warped, mangrove, bamboo, cherry,
    // and the hanging sign family - are treated as signs without needing a Spigot version bump. Legacy
    // (pre-1.13) materials are excluded, as a block in a modern world never reports one as its type.
    public boolean isSign(Block block) {
        return isSignMaterialName(block.getType().name());
    }

    // Kept separate from the Material enum so that names from Minecraft versions newer than the compiled
    // API - which have no constant to reference - can still be exercised by a test.
    boolean isSignMaterialName(String materialName) {
        return !materialName.startsWith("LEGACY_") && materialName.endsWith("_SIGN");
    }

}
