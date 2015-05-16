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
package com.codelanx.plotted.config;

import com.codelanx.codelanxlib.annotation.PluginClass;
import com.codelanx.codelanxlib.annotation.RelativePath;
import com.codelanx.codelanxlib.config.Config;
import com.codelanx.codelanxlib.config.DataHolder;
import com.codelanx.codelanxlib.data.FileDataType;
import com.codelanx.plotted.Plotted;

/**
 * Class description for {@link PlotConfig}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
@RelativePath("global.yml")
@PluginClass(Plotted.class)
public enum PlotConfig implements Config {

    PLOT_SIZE("plot-size", 100),
    BOUNDARY_WIDTH("boundary-width", 2),
    ;

    private static final DataHolder<? extends FileDataType> DATA;
    private final String key;
    private final Object def;
    
    static {
        DATA = new DataHolder<>(FileDataType.fromString(PluginConfig.PLOT_FILE_TYPE.as(String.class)));
    }

    private PlotConfig(String key, Object def) {
        this.key = key;
        this.def = def;
    }

    @Override
    public String getPath() {
        return this.key;
    }

    @Override
    public Object getDefault() {
        return this.def;
    }

    @Override
    public DataHolder<? extends FileDataType> getData() {
        return DATA;
    }

}
