package io.github.dengchen2020.mybatis.extension.exception;

/**
 * @author dengchen
 */
public class MybatisCustomException extends RuntimeException {

    public MybatisCustomException(String message) {
        super(message);
    }

    public MybatisCustomException(String message, Throwable cause) {
        super(message, cause);
    }

}
