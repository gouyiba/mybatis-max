package cloub.gouyiba.common.exception;

import lombok.Getter;

/**
 * mybatis-max统一异常处理
 */
@Getter
public class MyBatisMaxException extends RuntimeException {

    private static final String TAG = "mybatis-max -> ";

    public MyBatisMaxException(String message) {
        super(TAG + message);
    }

}