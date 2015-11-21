package minesim.tiles;

import java.lang.Math;
import java.util.Random;
import java.util.HashMap;
import java.util.RandomAccess;

/**
 * Generates 2D noise using the Perlin noise algorithm
 *
 * @author Team Hopper (Cranny, Fraser, Shen, Spearritt)
 */
public class NoiseCreator {

    private Random random;
    private HashMap<Integer, HashMap<Integer, Pair>> gradients;

    public NoiseCreator(int genWidth, int genHeight, Random randomGenerator) {
        random = randomGenerator;
        gradients = new HashMap<>();

        // Start by generating randoms for the rectangle created by genWidth and genHeight

        int xLow, xHigh, yLow, yHigh;
        xLow = -(genWidth/2);
        if (genWidth % 2 == 1) {
            xLow--;
        }
        yLow = -(genHeight/2);
        if (genHeight % 2 == 1) {
            yLow--;
        }
        xHigh = genWidth/2;
        yHigh = genHeight/2;

        for (int x = xLow; x <= xHigh; ++x) {
            gradients.put(x, new HashMap<>());
            for (int y = yLow; y <= yHigh; ++y) {
                gradients.get(x).put(y, makeRandomVector());
            }
        }
    }

    public NoiseCreator(int genWidth, int genHeight) {
        this(genWidth, genHeight, new Random());
    }

    /**
     * Construct a new NoiseCreator using the default generation width/height of 16
     */
    public NoiseCreator() {
        this(16, 16);
    }

    /**
     * Creates a random gradient vector within the unit circle
     *
     * @return a Pair of random X and Y coordinates on the unit circle
     */
    private Pair makeRandomVector() {
        double theta = 2 * Math.PI * random.nextDouble();

        return new Pair(Math.cos(theta), Math.sin(theta));
    }

    /**
     * Gets the gradient at a given coordinate, generating it if required
     *
     * @param x the grid x coordinate
     * @param y the grid y coordinate
     */
    private Pair getGradientAtCoordinate(int x, int y) {
        if (!gradients.containsKey(x)) {
            gradients.put(x, new HashMap<>());
        }
        if (!gradients.get(x).containsKey(y)) {
            gradients.get(x).put(y, makeRandomVector());
        }
        return gradients.get(x).get(y);
    }

    /**
     * Calculates the dot product of the distance vector for a point
     * with its gradient vector
     *
     * @param nodeX the x corner coordinate to base distance off
     * @param nodeY the y corner coordinate to base distance off
     * @param point the point to use
     * @return the dot product (scalar value)
     */
    private double dotProductGradient(int nodeX, int nodeY, Pair point) {
        // Get the distance
        Pair dist = new Pair(point.x - (double)nodeX, point.y - (double)nodeY);
        // Get the gradients
        Pair grad = getGradientAtCoordinate(nodeX, nodeY);

        // Return the dot product
        return dist.x * grad.x + dist.y * grad.y;
    }

    /**
     * Compute the Perlin noise value at the given point
     *
     * @param x the x coordinate to get noise at
     * @param y the y coordinate to get noise at
     * @return the Perlin noise value at this coordinate
     */
    public double getPerlin(double x, double y) {
        // Figure out what cell the point is in
        int xLow, yLow, xHigh, yHigh;
        xLow = (int)x;
        if (x < 0) {
            xLow--;
        }
        yLow = (int)y;
        if (y < 0) {
            yLow--;
        }
        xHigh = xLow + 1;
        yHigh = yLow + 1;

        Pair point = new Pair(x, y);

        // Get the weightings for interpolation
        double xWeight = x - (double)xLow;
        double yWeight = y - (double)yLow;

        // Get the corner point gradient dot products
        double topLeft = dotProductGradient(xLow, yLow, point);
        double topRight = dotProductGradient(xHigh, yLow, point);
        double bottomLeft = dotProductGradient(xHigh, yLow, point);
        double bottomRight = dotProductGradient(xHigh, yHigh, point);

        // Get the x interpolation
        double topVal = interpolate(topLeft, topRight, xWeight);
        double bottomVal = interpolate(bottomLeft, bottomRight, xWeight);

        // Get the final interpolated value
        return interpolate(topVal, bottomVal, yWeight);

    }

    /**
     * Adds perlin noise to itself to generate fractal noise
     * via Fractional Brownian Motion
     *
     * @param x the x coordinate of noise to use
     * @param y the y coordinate of noise to use
     * @param octaves the number of times to add noise to itself
     * @param freqGrowth the growth rate of the frequency
     * @param gain the shrink rate of the amplitude
     * @return the resultant fractal noise
     */
    public double getFractalNoise(double x, double y, int octaves, double freqGrowth, double gain) {
        double noise = 0.0;
        double freq = 1.0/50.0;
        double amp = gain;

        for (int i = 0; i < octaves; i++) {

            // Add the next octave of noise
            noise += getPerlin(freq * x, freq * y) * amp;

            // Increase frequency and decrease amplitude
            freq *= freqGrowth;
            amp *= gain;
        }

        return noise;
    }

    /**
     * Add perlin noise to itself to generate fractal noise
     * using standard values for freqGrowth = 2 and gain = 0.5
     *
     * @param x the x coordinate of noise to use
     * @param y the y coordinate of noise to use
     * @param octaves the number of times to add noise to itself
     * @return the resultant fractal noise
     */
    public double getFractalNoise(double x, double y, int octaves) {
        return getFractalNoise(x, y, octaves, 2, 0.5);
    }

    /**
     * Interpolates between two values linearly, with weighting x
     *
     * @param a0 the lower endpoint
     * @param a1 the upper endpoint
     * @param x the weighting between 0 and 1
     * @return the interpolated value
     */
    private double interpolate(double a0, double a1, double x) {
        return a0 * (1 - x) + a1 * x;
    }

    /**
     * Immutable 2D vector object
     */
    private class Pair {
        public final double x;
        public final double y;

        public Pair(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
