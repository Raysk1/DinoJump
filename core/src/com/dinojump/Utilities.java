package com.dinojump;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Utilities {

    public static TextureRegion[] textureCutter(Texture texture, int parts, int size, int start) {

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / parts, texture.getHeight());
        TextureRegion[] regions = new TextureRegion[parts];
        TextureRegion[] finalTexture = new TextureRegion[size];
        System.arraycopy(tmp[0], 0, regions, 0, parts);

        System.arraycopy(regions, 0 + start, finalTexture, 0, size);

        return finalTexture;
    }


}
