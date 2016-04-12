package com.vann.csdn.bean;

/**
 * author： bwl on 2016-04-11.
 * email: bxl049@163.com
 */
public class CommonException extends Exception {
    private static final long serialVersionUID = 1L;
    public CommonException() {
        super();
    }

    public CommonException(String message, Throwable arg) {
        super(message, arg);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable arg) {
        super(arg);
    }
}
