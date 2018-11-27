package cn.csdb.model;

public class Frequencies {

    private int src;
    private int tar;

    public Frequencies(int src, int tar) {
        this.src = src;
        this.tar = tar;
    }


    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getTar() {
        return tar;
    }

    public void setTar(int tar) {
        this.tar = tar;
    }

    public int addSrc() {
        return ++src;
    }
    public int addTar() {
        return ++tar;
    }
}
