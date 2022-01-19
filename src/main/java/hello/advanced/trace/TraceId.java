package hello.advanced.trace;

import java.util.UUID;

// 로그추적기 -> 트랜잭션 ID와 깊이 표현
public class TraceId {

    private String id; // HTTP 요청 ID(트랜잭션 ID)
    private int level; // Level 정보

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {
        // UUID가 너무 길기 때문에 0부터 8까지로 잘라줄 것이다.
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // id는 같고 레벨이 하나씩 증가하기 위한 메서드
    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    // id는 같고 레벨이 하나씩 감소하는 메서드 (한 단계 내려갈때)
    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    // 첫번째 레벨 여부를 알기 위한 메서드
    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
