package hello.advanced.trace;

// 로그를 시작할 때의 상태 정보를 가지고 있는것!
// 이 상태 정보는 로그를 종료할 때 사용한다.
public class TraceStatus {

    private TraceId traceId; // 내부의 트랜잭션 id와 level을 가지고 있다.
    private Long startTimeMs; // 로그 시작 시간 ( 로그 시작 시간을 기준으로 종료까지 전체 수행 시간을 구할 수 있다.)
    private String message; // 시작 시 사용한 메시지

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
