package dansplugins.spawnsystem.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BlockCheckerTest {

    private final BlockChecker blockChecker = new BlockChecker();

    @ParameterizedTest
    @EnumSource(value = Material.class, names = {
            "ACACIA_SIGN", "ACACIA_WALL_SIGN",
            "BIRCH_SIGN", "BIRCH_WALL_SIGN",
            "DARK_OAK_SIGN", "DARK_OAK_WALL_SIGN",
            "JUNGLE_SIGN", "JUNGLE_WALL_SIGN",
            "OAK_SIGN", "OAK_WALL_SIGN",
            "SPRUCE_SIGN", "SPRUCE_WALL_SIGN"
    })
    void isSign_signMaterial_returnsTrue(Material material) {
        Block block = mock(Block.class);
        when(block.getType()).thenReturn(material);

        assertTrue(blockChecker.isSign(block));
    }

    @Test
    void isSign_nonSignMaterial_returnsFalse() {
        Block block = mock(Block.class);
        when(block.getType()).thenReturn(Material.STONE);

        assertFalse(blockChecker.isSign(block));
    }
}
