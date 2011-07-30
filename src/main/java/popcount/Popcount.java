package popcount;

public class Popcount {
    /** 8 ビットの数値に対する popcount 値テーブル */
    private static final int[] POPCOUNT8_TABLE;

    /** 16 ビットの数値に対する popcount 値テーブル */
    private static final int[] POPCOUNT16_TABLE;

    /**
     * popcountT8() および popcountT16() で利用するテーブルを用意します。
     */
    static {
        POPCOUNT8_TABLE = new int[1 << 8];
        POPCOUNT16_TABLE = new int[1 << 16];

        for (int i = 0; i < POPCOUNT8_TABLE.length; i++) {
            POPCOUNT8_TABLE[i] = popcountB(i);
        }

        for (int i = 0; i < POPCOUNT16_TABLE.length; i++) {
            POPCOUNT16_TABLE[i] = popcountB(i);
        }
    }

    /**
     * ビット演算による popcount 演算を行います。
     * 
     * @param value
     * @return
     */
    public static int popcountB(int value) {
        value = (value & 0x55555555) + ((value & 0xaaaaaaaa) >>> 1);
        value = (value & 0x33333333) + ((value & 0xcccccccc) >>> 2);
        value = (value & 0x0f0f0f0f) + ((value & 0xf0f0f0f0) >>> 4);
        value = (value & 0x00ff00ff) + ((value & 0xff00ff00) >>> 8);
        return (value & 0x0000ffff) + ((value & 0xffff0000) >>> 16);
    }

    /**
     * 8 ビットごとのテーブル参照による popcount 演算を行います。
     * 
     * @param value
     * @return
     */
    public static int popcountT8(int value) {
        return POPCOUNT8_TABLE[value >>> 24]
                + POPCOUNT8_TABLE[(value >> 16) & 0xff]
                + POPCOUNT8_TABLE[(value >> 8) & 0xff]
                + POPCOUNT8_TABLE[value & 0xff];
    }

    /**
     * 16 ビットごとのテーブル参照による popcount 演算を行います。
     * 
     * @param value
     * @return
     */
    public static int popcountT16(int value) {
        return POPCOUNT16_TABLE[value >>> 16]
                + POPCOUNT16_TABLE[value & 0xffff];
    }

    /**
     * 各 popcount 演算の実装ごとにその性能（処理時間）を測定し、結果を表示します。
     * 
     * @param args
     */
    public static void main(String[] args) {
        int i = 0;
        long totalPopcount = 0;

        int initValue = 0;
        int endValue = 1000000000;

        PopcountProfiler profiler = new PopcountProfiler(initValue, endValue);

        // popcountB
        totalPopcount = 0;
        profiler.prepare("popcountB");
        for (i = initValue; i < endValue; i++) {
            totalPopcount += popcountB(i);
        }
        profiler.showResult();

        // popcountT8
        totalPopcount = 0;
        profiler.prepare("popcountT8");
        for (i = initValue; i < endValue; i++) {
            totalPopcount += popcountT8(i);
        }
        profiler.showResult();

        // popcountT16
        totalPopcount = 0;
        profiler.prepare("popcountT16");
        for (i = initValue; i < endValue; i++) {
            totalPopcount += popcountT16(i);
        }
        profiler.showResult();
    }

    private static class PopcountProfiler {
        private static final int DELAY_MSECS = 5000;

        private String methodName;

        private long startMsecs;

        private long loopCount;

        public PopcountProfiler(int initValue, int endValue) {
            loopCount = ((long) endValue) - initValue;
        }

        void prepare(String methodName) {
            this.methodName = methodName;

            System.out.println();
            System.out.println("-----");
            System.out.println(methodName + " を実行します。");

            startMsecs = System.currentTimeMillis() + DELAY_MSECS;
            while (System.currentTimeMillis() < startMsecs) {
                // 遅延をかける
            }

            startMsecs = System.currentTimeMillis();
        }

        void showResult() {
            long result = System.currentTimeMillis() - startMsecs;
            System.out.println(methodName + " の結果は " + result + " ミリ秒でした。");
            if (result > 0) {
                System.out.println("(" + (loopCount / result) + " 回/秒)");
            }
        }
    }
}
