package com.dinojump.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Utilities {

    public static TextureRegion[] textureCutter(Texture texture, int parts, int size, int start) {

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / parts, texture.getHeight());
        TextureRegion[] regions = new TextureRegion[parts];
        TextureRegion[] finalTexture = new TextureRegion[size];
        System.arraycopy(tmp[0], 0, regions, 0, parts);

        System.arraycopy(regions, start, finalTexture, 0, size);

        return finalTexture;
    }

    public static TextureRegion[] textureCutter(Texture texture, int line,int col, int size, int start) {

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / line, texture.getHeight()/col);
        TextureRegion[] regions = new TextureRegion[line*col];
        TextureRegion[] finalTexture = new TextureRegion[size];
        int aux = 0;
        for (int i = 0;i < col; i++){
            for (int j = 0; j < line; j++) {
                regions[aux] = tmp[i][j];
                aux++;
            }
        }

        System.arraycopy(regions, start, finalTexture, 0, size);

        return finalTexture;
    }


}
