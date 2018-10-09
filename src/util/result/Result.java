package util.result;

public class Result {
    private int error;
    private String info;

    public Result() {
        error = 0;
        info = "OK";
    }

    public Result(int error, String info) {
        this.error = error;
        this.info = info;
    }
}
