package util.result;

public class Result {
    public int error;
    public String info;

    public Result() {
        error = 0;
        info = "OK";
    }

    public Result(int error, String info) {
        this.error = error;
        this.info = info;
    }

    @Override
    public String toString() {
        return error == 0
                ? "OK: " + info
                : "Error code " + error + ": " + info;
    }
}
