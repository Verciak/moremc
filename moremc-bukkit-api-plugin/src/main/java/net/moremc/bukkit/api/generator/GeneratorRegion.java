package net.moremc.bukkit.api.generator;

import com.google.common.collect.Lists;
import org.bukkit.Location;

import java.util.Collections;
import java.util.List;

public class GeneratorRegion extends Region{

    private final List<GeneratorPillar> pillars = Lists.newArrayList();

    public GeneratorRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        super(minX, maxX, minY, maxY, minZ, maxZ);

        this.cachePillars();
    }
    public static GeneratorRegion fromCorners(Location firstCorner, Location secondCorner) {
        int firstX = firstCorner.getBlockX();
        int secondX = secondCorner.getBlockX();

        int firstZ = firstCorner.getBlockZ();
        int secondZ = secondCorner.getBlockZ();

        int firstY = firstCorner.getBlockY();
        int secondY = secondCorner.getBlockY();

        return new GeneratorRegion(
                Math.min(firstX, secondX),
                Math.max(firstX, secondX),
                Math.min(firstY, secondY),
                Math.max(firstY, secondY),
                Math.min(firstZ, secondZ),
                Math.max(firstZ, secondZ));
    }

    public int price() {
        return super.area() * 100;
    }

    public List<GeneratorPillar> pillars() {
        return Collections.unmodifiableList(this.pillars);
    }

    public GeneratorPillar pillar(int index) {
        return this.pillars.get(index);
    }

    private void cachePillars() {
        this.pillars.clear();

        for (int x = getMinX(); x <= getMaxX(); x++) {
            for (int z = getMinZ(); z <= getMaxZ(); z++) {
                this.pillars.add(new GeneratorPillar(x, z));
            }
        }
    }
}
