/*
 * Copyright (C) 2015 Codelanx, All Rights Reserved
 *
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * This program is protected software: You are free to distrubute your
 * own use of this software under the terms of the Creative Commons BY-NC-ND
 * license as published by Creative Commons in the year 2015 or as published
 * by a later date. You may not provide the source files or provide a means
 * of running the software outside of those licensed to use it.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the Creative Commons BY-NC-ND license
 * long with this program. If not, see <https://creativecommons.org/licenses/>.
 */
package com.codelanx.plotted;

import com.codelanx.codelanxlib.logging.Debugger;
import com.codelanx.plotted.config.PlotConfig;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;

/**
 * Class description for {@link Range}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class Range implements Comparable<Range>, Cloneable {

    public static final int MAGNITUDE = PlotConfig.PLOT_SIZE.as(int.class); //Set as an even number for a new map to change island plot size
    private static final int HALFMAG = MAGNITUDE / 2;
    private final int x;
    private final int z;

    public Range(int x, int z) {
        Validate.isTrue(x % Range.MAGNITUDE == 0);
        Validate.isTrue(z % Range.MAGNITUDE == 0);
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public boolean contains(int x, int z) {
        return Math.abs(this.x - x) < (Range.HALFMAG) && Math.abs(this.z - z) < (Range.HALFMAG);
    }

    public boolean contains(Location loc) {
        return this.contains(loc.getBlockX(), loc.getBlockZ());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.x;
        hash = 59 * hash + this.z;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Range other = (Range) obj;
        return this.x == other.x && this.z == other.z;
    }

    @Override
    public int compareTo(Range o) {
        return this.getLocationInSpiral() - o.getLocationInSpiral();
    }

    private int getLocationInSpiral() {
        int mag = Math.max(Math.abs(x), Math.abs(z));
        if (mag == 0) {
            return 0;
        }
        int root = (((mag / Range.MAGNITUDE) - 1) << 1) + 1;
        //next ring start would be the same, without removing 1 from mag/Range.MAG
        int ringInit = root * root;
        Range init = new Range(Range.MAGNITUDE - mag, mag);
        while(!this.equals(init) && Math.abs(init.getZ()) <= mag) {
            init = init.nextIslandLocation();
            ringInit++;
        }
        return ringInit;
    }

    public static Range fromLocation(Location loc) {
        Debugger.print("Rounding %.2f (x) to %d", loc.getX(), Range.round(loc.getX()));
        Debugger.print("Rounding %.2f (z) to %d", loc.getZ(), Range.round(loc.getZ()));
        return new Range(Range.round(loc.getX()), Range.round(loc.getZ()));
    }

    private static int round(double init) { //more of a raise and truncation
        int sign = init < 0 ? -1 : 1;
        init = Math.abs(init);
        init += init % Range.MAGNITUDE >= Range.HALFMAG ? Range.MAGNITUDE : 0;
        return sign * (int) ((long) (init * 1e-2) / 1e-2);
    }

    public Range nextIslandLocation() {
        return Range.nextIslandLocation(this);
    }

    public static Range nextIslandLocation(Range current) {
        return Range.nextIslandLocation(current.x, current.z);
    }

    /**
     * Taken from the old SkySMP plugin for consistency. No logic has been
     * attempted to be improved upon or changed, merely adopted to work with
     * the current plugin (and cleaned up a little). This method should return
     * the next sequential island in the "spiral" for islands
     * 
     * @param x The island x
     * @param z The island z
     * @return A new {@link Range} for the next island
     */
    private static Range nextIslandLocation(int x, int z) {
        //old
        if (x < z) {
            if (-1 * x < z) {
                return new Range(x + Range.MAGNITUDE, z);
            }
            return new Range(x, z + Range.MAGNITUDE);
        }
        if (x > z) {
            if (-1 * x >= z) {
                return new Range(x - Range.MAGNITUDE, z);
            }
            return new Range(x, z - Range.MAGNITUDE);
        }
        return new Range(x, z + ((x <= 0) ? Range.MAGNITUDE : -Range.MAGNITUDE));
    }

    @Override
    public String toString() {
        return "Range[" + this.x + ", " + this.z + "]";
    }

    @Override
    public Range clone() {
        try {
            return (Range) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error("Cannot create clone of Range", ex);
        }
    }

}
