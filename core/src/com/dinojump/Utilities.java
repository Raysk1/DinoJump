package com.dinojump;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Utilities {

    public static TextureRegion[] textureCutter(Texture texture, int parts, int size, int start){

        TextureRegion tmp[][] = TextureRegion.split(texture,texture.getWidth()/parts,texture.getHeight());
        TextureRegion[] regions = new TextureRegion[parts];
        TextureRegion[] finalTexture = new TextureRegion[size];
        for (int i = 0; i < parts; i++){
            regions[i] = tmp[0][i];
        }

        for (int i = 0; i<size;i++){
            finalTexture[i] = regions[i+start];
        }

        return finalTexture;
    }
}
