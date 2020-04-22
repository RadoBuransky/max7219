package com.buransky.max7219.impl;

import com.buransky.max7219.Max7219.PinState;
import com.buransky.max7219.Register;
import com.buransky.max7219.register.ShutdownRegister;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.buransky.max7219.impl.PacketSerializationTest.assertData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        final List<PinState> result = matrix.draw();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(38, result.size());
    }

    @Test
    public void testDrawNothing() {
        // Execute
        final List<PinState> result = fastLedMatrix.draw();

        // Assert
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testDrawSingleDotOnTheFirstDisplay() {
        // Prepare
        fastLedMatrix.setLedStatus(7, 7, true);

        // Execute
        final List<PinState> result = fastLedMatrix.draw();

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(135, result.size());
        final List<Short> data = assertData(result, 4);
        Assert.assertEquals(4, data.size());
        Assert.assertEquals(0x0880, (short)data.get(0));
        Assert.assertEquals(0x0000, (short)data.get(1));
        Assert.assertEquals(0x0000, (short)data.get(2));
        Assert.assertEquals(0x0000, (short)data.get(3));
    }

    @Test
    public void testDrawSingleDotOnTheFirstDisplayTwice() {
        // Prepare
        fastLedMatrix.setLedStatus(0, 0, true);
        fastLedMatrix.draw();
        fastLedMatrix.setLedStatus(0, 0, true);

        // Execute
        final List<PinState> result = fastLedMatrix.draw();

        // Assert
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testDrawSingleDotOnTheLastDisplay() {
        // Prepare
        fastLedMatrix.setLedStatus(DISPLAY_ROWS*DISPLAYS_VERTICALLY - 1,
                DISPLAY_COLUMNS*DISPLAYS_HORIZONTALLY - 1, true);

        // Execute
        final List<PinState> result = fastLedMatrix.draw();

        // Assert
        Assert.assertNotNull(result);
        assertExpectedResultSize(1, 4, result.size());
    }

    @Test
    public void testDrawDiagonalLine() {
        // Prepare
        for (int i = 0; i < 8; i++) {
            fastLedMatrix.setLedStatus(i, i, true);
        }

        // Execute
        final List<PinState> result = fastLedMatrix.draw();

        // Assert
        Assert.assertNotNull(result);
        assertExpectedResultSize(8, 4, result.size());
    }

    @Test
    public void testExecuteAll() {
        // Prepare
        final Register register = ShutdownRegister.NormalOperation;

        // Execute
        final List<PinState> result = fastLedMatrix.executeAll(register);

        // Assert
        Assert.assertNotNull(result);
        assertExpectedResultSize(1, 4, result.size());
    }

    @Test
    public void testSetLedStatus() {
        // Execute
        fastLedMatrix.setLedStatus(0, 15, true);

        // Assert
        assertEquals(0x0L, fastLedMatrix.displays[0]);
        assertEquals(0x80L, fastLedMatrix.displays[1]);
        assertEquals(0x0L, fastLedMatrix.displays[2]);
        assertEquals(0x0L, fastLedMatrix.displays[3]);
    }

    @Test
    public void testGetLedStatus() {
        // Prepare
        fastLedMatrix.setLedStatus(7, 15, true);

        // Execute
        final boolean result = fastLedMatrix.getLedStatus(7, 15);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testTurnOffLed() {
        // Prepare
        fastLedMatrix.setLedStatus(0, 0, true);
        fastLedMatrix.draw();
        assertTrue(fastLedMatrix.getLedStatus(0, 0));

        // Execute
        fastLedMatrix.setLedStatus(0, 0, false);
        final List<PinState> result = fastLedMatrix.draw();

        // Assert
        Assert.assertNotNull(result);
        assertExpectedResultSize(1, 4, result.size());
    }

    @Test
    public void testReset() {
        // Execute
        final List<PinState> result = fastLedMatrix.reset();

        // Assert
        Assert.assertNotNull(result);
        assertEquals(1853, result.size());
    }

    private void assertExpectedResultSize(final int expectedStepsCount, final int displayCount, final int resultSize) {
        Assert.assertTrue(expectedStepsCount*(displayCount*16*3 + 2) >= resultSize);
    }
}
