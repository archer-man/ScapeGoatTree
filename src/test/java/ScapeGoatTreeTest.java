import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScapeGoatTreeTest {

    @Test
    public static void main(String[] args) {
        ScapeGoatTree tree = new ScapeGoatTree();
        assertTrue(tree.add(1));
        assertTrue(tree.add(2));
        assertTrue(tree.add(3));
        assertTrue(tree.add(4));
        assertTrue(tree.add(5));
        assertTrue(tree.add(6));
        assertTrue(tree.remove(3));
    }
}