public enum Turn {
    RED,
    BLACK;
    public Turn getOpposite() {
        return (this == RED) ? BLACK : RED;
    }
}
