package minesim.tile;

import minesim.tiles.NoiseCreator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Random;

import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


/**
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public class NoiseCreatorTests {

    /**
     * Test the basic constructor with a real random noise generator
     */
    @Test
    public void testNoiseCreatorConstructor() {
        NoiseCreator noiseCreator = new NoiseCreator();
    }

    /**
     * Test the perlin noise generation using mocked random functions
     */
    @Test
    public void testMockedPerlinNoise() {
        Random r = mock(Random.class);

        when(r.nextDouble()).then(new Answer() {
            public Object answer(InvocationOnMock invoc) {
                return 1;
            }
        });

        NoiseCreator n = new NoiseCreator(16, 16, r);
        assertEquals(n.getPerlin(5.5, 5.5), -0.25, 0.01);
        assertEquals(n.getPerlin(2.5, 2.5), -0.25, 0.01);
        assertEquals(n.getPerlin(5, 5), 0, 0.01);
        assertEquals(n.getPerlin(2, 2), 0, 0.01);
    }

    @Test
    public void testMockedFractalNoise() {
        Random r = mock(Random.class);

        when(r.nextDouble()).then(new Answer() {
            public Object answer(InvocationOnMock invoc) {
                return 1;
            }
        });

        NoiseCreator n = new NoiseCreator(16, 16, r);
        assertEquals(-0.0376, n.getFractalNoise(2.0, 2.0, 2), 0.01);
        assertEquals(-0.0463, n.getFractalNoise(2.5, 2.5, 2), 0.01);
    }


}
