package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component // 싱글톤을 사용하기 위해 스프링 빈으로 등록했고, 컴포넌트 스캔 대상이 된다.
public class HelloTraceV2 {
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    // 로그를 시작한다.
    // 로그 메시지를 파라미터로 받아서 시작 로그를 출력한다.
    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        long startTimeMs = System.currentTimeMillis();
        // 로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    // V2 New !
    public TraceStatus beginSync( TraceId beforeTraceId, String message) {
//        TraceId traceId = new TraceId();
        TraceId nextId = beforeTraceId.createNextId();
        long startTimeMs = System.currentTimeMillis();
        // 로그 출력
        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);
        return new TraceStatus(nextId, startTimeMs, message);
    }

    // 로그를 정상 종료한다. (정상 흐름)
    // 파라미터로 시작 로그의 상태를 전달받고, 이 값을 활용해서 실행 시간을 계산하고,
    // 종료시에도 시작할때와 동일한 로그 메시지 출력 가능하다.
    public void end(TraceStatus status) {
        complete(status, null);
    }

    // 로그를 예외 상황으로 종료한다.
    // Exception 정보를 추가로 전달 받아서 예외 정보를 포함한 결과 로그를 남긴다.
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    // 여기서부터는 비공개 메서드

    // end(), exception()의 요청 흐름을 한곳에서 편리하게 처리하는 메서드
    // 실행 시간을 측정하고 로그를 남긴다.
    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(),
                    resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs,
                    e.toString());
        }
    }

    // 이 로직이 제일 어렵긴하다!
    // prefix '-->'
    // level 0 : ''
    // level 1 : '|-->'
    // level 2 : '|   | -->'

    // prefix '<--'
    // level 0 : ''
    // level 1 : '|<--'
    // level 2 : '|   |<--'

    // prefix '<X--'
    // level 0 : ''
    // level 1 : '|<X--'
    // level 2 : '|    |<X--'
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "| ");
        }
        return sb.toString();
    }

}
