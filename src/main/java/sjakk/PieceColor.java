package sjakk;

public enum PieceColor {
    WHITE(1), BLACK(-1);

    private int dir;

    private PieceColor(int dir) {
        this.dir = dir;
    }

    public int getDir() {
        return dir;
    }

}
