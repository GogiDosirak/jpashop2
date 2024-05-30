package jpabook.jpashop.exception;

public class NotEnoughStockException extends  RuntimeException { // 상속해서 오버라이드
    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException() {
        super();
    }

}
