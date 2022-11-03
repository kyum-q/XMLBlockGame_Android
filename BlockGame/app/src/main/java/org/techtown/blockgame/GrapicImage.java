package org.techtown.blockgame;

public class GrapicImage {
    protected int w=0, h=0,x=0,y=0, imageID=0;
    protected boolean state = true;

    public GrapicImage() {
    }
    public void removeImage() {
        state = false;
    }

    public boolean getState() {
        return state;
    }

    public void setX(float x) {
        this.x = (int) x;
    }
    public void setY(float y) {
        this.y = (int) y;
    }

    public int getImageID() { return imageID; }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getW(){
        return w;
    }
    public int getH(){
        return h;
    }
}
