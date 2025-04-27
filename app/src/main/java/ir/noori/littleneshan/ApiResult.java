package ir.noori.littleneshan;

public abstract class ApiResult<T> {

    private ApiResult() {
    }

    public static final class Success<T> extends ApiResult<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    public static final class Error<T> extends ApiResult<T> {
        private final String throwable;

        public Error(String throwable) {
            this.throwable = throwable;
        }

        public String getThrowable() {
            return throwable;
        }
    }

    public static final class Loading<T> extends ApiResult<T> {
        public Loading() {
        }
    }
}
