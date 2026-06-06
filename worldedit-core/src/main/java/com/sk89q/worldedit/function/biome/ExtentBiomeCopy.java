package com.sk89q.worldedit.function.biome;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.FlatRegionFunction;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.world.biome.BaseBiome;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Copies the biome from one extent to another.
 */
public class ExtentBiomeCopy implements FlatRegionFunction {

    private final Extent source, destination;
    private final Vector2D from, to;
    private final Transform transform;

    /**
     * Make a new biome copy.
     *
     * @param source the source extent
     * @param from the source offset
     * @param destination the destination extent
     * @param to the destination offset
     * @param transform a transform to apply to positions (after source offset, before destination offset)
     */
    public ExtentBiomeCopy(Extent source, Vector2D from, Extent destination, Vector2D to, Transform transform) {
        checkNotNull(source);
        checkNotNull(from);
        checkNotNull(destination);
        checkNotNull(to);
        checkNotNull(transform);
        this.source = source;
        this.from = from;
        this.destination = destination;
        this.to = to;
        this.transform = transform;
    }

    @Override
    public boolean apply(Vector2D position) {
        BaseBiome biome = source.getBiome(position);
        Vector2D orig = position.subtract(from);
        Vector transformed = transform.apply(orig.toVector())
                .toBlockPoint()
                .add(to.toVector());

        return destination.setBiome(transformed.toVector2D(), biome);
    }

}
