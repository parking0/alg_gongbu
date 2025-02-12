import java.util.*;
import java.io.*;

class swea_5653 {

    static int N, M, K, input, row, col;
    static int max_index;
    static int DEAD = 0;
    static int ACTIVE = 1;
    static int INACTIVE = 2;

    static Map<intPair, Integer> active;       // key - 좌표 , value - 생명력
    static Map<intPair, Integer> inactive;     // key - 좌표 , value - 생명력
    static int[][] move_dir = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    static int[][] status;

    public static void main(String args[]) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        int T = Integer.parseInt(br.readLine());

        for (int test_case = 1; test_case <= T; test_case++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());       // 세로
            M = Integer.parseInt(st.nextToken());       // 가로
            K = Integer.parseInt(st.nextToken());       // 배양 시간

            max_index = N + 2 * (M + 2);
            active = new HashMap<>();
            inactive = new HashMap<>();
            status = new int[max_index][max_index];
            row = M+2;
            col = N+2;

            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < M; j++) {
                    int y = i + col;
                    int x = j + row;
                    input = Integer.parseInt(st.nextToken());
                    if (input != 0) {
                        status[y][x] = INACTIVE;
                        inactive.put(new intPair(x, y), input);
                    }
                }
            }

            // 초기상태- X시간 동안 비활성 -> X시간이 지나면 활성 -> X시간동안 살고 -> 죽음
            // 활성화 - 상하좌우 동시 번식 -> 번식된 애는 비활성 상태
            // 이미 줄기세포가 있으면 추가 번식X
            // 동시 번식 시도 -> 생명력 수치 높은 줄기 세포가 차지
            for (int hour = 1; hour <= K; hour++) {

                List<intPair> toRemoveFromActive = new ArrayList<>();
                List<intPair> toAddToInactive = new ArrayList<>();
                List<intPair> toRemoveFromInActive = new ArrayList<>();
                List<intPair> toAddToActive = new ArrayList<>();


                // 활성 조회
                for (Map.Entry<intPair, Integer> entry : active.entrySet()) {
                    intPair key = entry.getKey();    // 좌표
                    int val = entry.getValue();      // 생명력 값

                    if (val == hour) {
                        status[key.y][key.x] = DEAD;
                        toRemoveFromActive.add(key);  // 삭제할 항목 저장
                    } else {
                        if (hour == val) {  // 번식할 시기가 됨
                            for (int i = 0; i < 4; i++) {
                                int newX = key.x + move_dir[i][0];
                                int newY = key.y + move_dir[i][1];
                                if (status[newY][newX] == 0) {
                                    toAddToInactive.add(new intPair(newX, newY)); // 번식할 좌표 저장
                                    status[newY][newX] = INACTIVE;
                                }
                            }
                        }
                    }
                }

                // 비활성 조회
                for (Map.Entry<intPair, Integer> entry : inactive.entrySet()) {
                    intPair key = entry.getKey();    // 좌표
                    int val = entry.getValue();      // 생명력 값
                    
                    // 비활성 -> 활성
                    if (hour == val + 1) {
                        active.put(new intPair(key.x, key.y), val + hour);   // 새로 추가되는 생명력에는 시간을 더해줌
                        status[key.y][key.x] = ACTIVE;
                        toRemoveFromInActive.add(key);
                        toAddToActive.add(key);
                    }
                }

                for (intPair key : toRemoveFromActive) {        // active에서 한꺼번에 제거
                    active.remove(key);
                }

                for (intPair key : toAddToInactive) {           // inactive에 새로운 항목을 추가
                    inactive.put(key, hour);
                }
                for (intPair key : toRemoveFromInActive) {        // active에서 한꺼번에 제거
                    inactive.remove(key);
                }
                for (intPair key : toAddToActive) {           // inactive에 새로운 항목을 추가
                    active.put(key, hour);
                }

                
                System.out.println("hour : " + hour);
                System.out.println("-- inactive --");
                for (Map.Entry<intPair, Integer> entry : inactive.entrySet()){
                    System.out.println("x = " + (entry.getKey().x - row) + " y = " + (entry.getKey().y - col));
                }

                System.out.println("-- active --");
                for (Map.Entry<intPair, Integer> entry : active.entrySet()){
                    System.out.println("x = " + (entry.getKey().x - row) + " y = " + (entry.getKey().y - col));
                }
                System.out.println();
            }

            sb.append("#").append(test_case).append(" ").append(active.size() + inactive.size()).append("\n");
        }

        System.out.println(sb.toString());
        br.close();
    }

    static class intPair {
        int x;
        int y;

        intPair(int a, int b) {
            x = a;
            y = b;
        }
    }
}
