package sjakk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import sjakk.utils.FENParser;

public class FileSaveTest {
    private static final String sep = System.getProperty("file.separator");
    private static final String directory = System.getProperty("user.dir");
    private static final String saveLocationString = String.join(sep, directory, "src", "test", "resources", "sjakk",
            "testSave.fen");
    private static final String defaultFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private String loadedFEN;
    private File saveLocation;

    @Test
    public void testFileSaveAndLoad() {
        assertDoesNotThrow(() -> saveLocation = new File(saveLocationString));
        FENParser.saveToFile(defaultFEN, saveLocation);
        assertDoesNotThrow(() -> loadedFEN = FENParser.readFENFromFile(saveLocation));
        assertEquals(defaultFEN, loadedFEN);
    }

    @Test
    public void testThrowsExceptionOnInvalidFile() {
        assertThrows(FileNotFoundException.class, () -> FENParser.readFENFromFile(new File("")));
    }
}
