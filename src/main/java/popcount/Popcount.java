package popcount;

public class Popcount {
    /** 8 �r�b�g�̐��l�ɑ΂��� popcount �l�e�[�u�� */
    private static final int[] POPCOUNT8_TABLE;

    /** 16 �r�b�g�̐��l�ɑ΂��� popcount �l�e�[�u�� */
    private static final int[] POPCOUNT16_TABLE;

    /**
     * popcountT8() ����� popcountT16() �ŗ��p����e�[�u����p�ӂ��܂��B
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
     * �r�b�g���Z�ɂ�� popcount ���Z���s���܂��B
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
     * 8 �r�b�g���Ƃ̃e�[�u���Q�Ƃɂ�� popcount ���Z���s���܂��B
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
     * 16 �r�b�g���Ƃ̃e�[�u���Q�Ƃɂ�� popcount ���Z���s���܂��B
     * 
     * @param value
     * @return
     */
    public static int popcountT16(int value) {
        return POPCOUNT16_TABLE[value >>> 16]
                + POPCOUNT16_TABLE[value & 0xffff];
    }

    /**
     * �e popcount ���Z�̎������Ƃɂ��̐��\�i�������ԁj�𑪒肵�A���ʂ�\�����܂��B
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
            System.out.println(methodName + " �����s���܂��B");

            startMsecs = System.currentTimeMillis() + DELAY_MSECS;
            while (System.currentTimeMillis() < startMsecs) {
                // �x����������
            }

            startMsecs = System.currentTimeMillis();
        }

        void showResult() {
            long result = System.currentTimeMillis() - startMsecs;
            System.out.println(methodName + " �̌��ʂ� " + result + " �~���b�ł����B");
            if (result > 0) {
                System.out.println("(" + (loopCount / result) + " ��/�b)");
            }
        }
    }
}
