package dansplugins.spawnsystem.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.EnumSource.Mode.MATCH_ALL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlockCheckerTest {

    private final BlockChecker blockChecker = new BlockChecker();

    // Every non-legacy sign material the compiled API knows about, matched by name so that sign types
    // introduced by a later API version are covered automatically rather than needing to be listed here.
    @ParameterizedTest
    @EnumSource(value = Material.class, mode = MATCH_ALL, names = "^(?!LEGACY_).*_SIGN$")
    void isSign_signMaterial_returnsTrue(Material material) {
        Block block = mock(Block.class);
        when(block.getType()).thenReturn(material);

        assertTrue(blockChecker.isSign(block));
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = {"LEGACY_SIGN", "LEGACY_SIGN_POST", "LEGACY_WALL_SIGN"})
    void isSign_legacySignMaterial_returnsFalse(Material material) {
        Block block = mock(Block.class);
        when(block.getType()).thenReturn(material);

        assertFalse(blockChecker.isSign(block));
    }

    @Test
    void isSign_nonSignMaterial_returnsFalse() {
        Block block = mock(Block.class);
        when(block.getType()).thenReturn(Material.STONE);

        assertFalse(blockChecker.isSign(block));
    }

    // These materials have no constant in the 1.15.2 API this plugin compiles against, so their names are
    // supplied directly. Without them the post-1.15 half of the behaviour could not be asserted at all.
    @ParameterizedTest
    @ValueSource(strings = {
            "CRIMSON_SIGN", "CRIMSON_WALL_SIGN",
            "WARPED_SIGN", "WARPED_WALL_SIGN",
            "MANGROVE_SIGN", "MANGROVE_WALL_SIGN",
            "BAMBOO_SIGN", "BAMBOO_WALL_SIGN",
            "CHERRY_SIGN", "CHERRY_WALL_SIGN",
            "OAK_HANGING_SIGN", "OAK_WALL_HANGING_SIGN"
    })
    void isSignMaterialName_signMaterialFromANewerVersion_returnsTrue(String materialName) {
        assertTrue(blockChecker.isSignMaterialName(materialName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"STONE", "CRIMSON_PLANKS", "LEGACY_SIGN", "SIGN_POST"})
    void isSignMaterialName_nonSignMaterialName_returnsFalse(String materialName) {
        assertFalse(blockChecker.isSignMaterialName(materialName));
    }
}
