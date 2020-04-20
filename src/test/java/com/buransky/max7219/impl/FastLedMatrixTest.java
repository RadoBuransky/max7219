package com.buransky.max7219.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FastLedMatrixTest {
    private static final int DISPLAY_ROWS = 8;
    private static final int DISPLAY_COLUMNS = 8;
    private static final int DISPLAYS_VERTICALLY = 1;
    private static final int DISPLAYS_HORIZONTALLY = 4;
    private FastLedMatrix fastLedMatrix;

    public FastLedMatrixTest() {
    }

    @Before
    public void before() {
        fastLedMatrix = new FastLedMatrix(DISPLAY_ROWS, DISPLAY_COLUMNS, DISPLAYS_VERTICALLY, DISPLAYS_HORIZONTALLY);
    }

    @Test
    public void testDrawSingleDotOnOneSmallDisplay() {
        // Prepare
        final FastLedMatrix matrix = new FastLedMatrix(1, 1, 1, 1);
        matrix.setLedStatus(0, 0, true);

        // Execute
        final List<Byte> result = matrix.draw();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(50, result.size());
    }

    @Test
    public void testDrawNothing() {
        // Execute
        final List<Byte> result = fastLedMatrix.draw();

        // Assert
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testDrawSingleDotOnTheFirstDisplay() {
        // Prepare
        fastLedMatrix.setLedStatus(0, 0, true);

        // Execute
        final List<Byte> result = fastLedMatrix.draw();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(194, result.size());
    }

    @Test
    public void testDrawSingleDotOnTheFirstDisplayTwice() {
        // Prepare
        fastLedMatrix.setLedStatus(0, 0, true);
        fastLedMatrix.draw();
        fastLedMatrix.setLedStatus(0, 0, true);

        // Execute
        final List<Byte> result = fastLedMatrix.draw();

        // Assert
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testDrawSingleDotOnTheLastDisplay() {
        // Prepare
        fastLedMatrix.setLedStatus(DISPLAY_ROWS*DISPLAYS_VERTICALLY - 1,
                DISPLAY_COLUMNS*DISPLAYS_HORIZONTALLY - 1, true);

        // Execute
        final List<Byte> result = fastLedMatrix.draw();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(194, result.size());
    }

    @Test
    public void testDrawDiagonalLine() {
        // Prepare
        for (int i = 0; i < 8; i++) {
            fastLedMatrix.setLedStatus(i, i, true);
        }

        // Execute
        final List<Byte> result = fastLedMatrix.draw();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(1552, result.size());
    }
}
