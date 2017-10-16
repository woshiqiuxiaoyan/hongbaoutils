package user.controller;

import java.text.DecimalFormat;
import java.util.Random;

public class HongBaoAlgorithm {


    static DecimalFormat df   = new DecimalFormat("######0.00");

    static Random random = new Random();
    static {
        random.setSeed(System.currentTimeMillis());
    }
    public static void main(String[] args) {
        double max = 2;
        double min = 1;

        double[] result = HongBaoAlgorithm.generate(100, 80, max, min);
        double total = 0;
        for (int i = 0; i < result.length; i++) {
            // System.out.println("result[" + i + "]:" + result[i]);
            // System.out.println(result[i]);
            total += result[i];
        }
        //检查生成的红包的总额是否正确
        System.out.println("total:" + total);

        for (int i = 0; i < result.length; i++) {
            System.out.println("" + i + "  " + result[i]);
        }

        //统计每个钱数的红包数量，检查是否接近正态分布
    /*    int count[] = new int[(int) max + 1];
        for (int i = 0; i < result.length; i++) {
            count[(int) result[i]] += 1;
        }
*/
     /*   for (int i = 0; i < count.length; i++) {
            System.out.println("" + i + "  " + count[i]);
        }*/
    }


    static double xRandom(double min, double max) {
        return sqrt(nextLong(sqr(max - min)));
    }




    public static double[] generate(double total, int count, double max, double min) {

        //生成 count 个
        double[] result = new double[count];


        double average = total / count;

        double a = average - min;
        double b = max - min;

        //
        //这样的随机数的概率实际改变了，产生大数的可能性要比产生小数的概率要小。
        //这样就实现了大部分红包的值在平均数附近。大红包和小红包比较少。
        double range1 = sqr(average - min);
        double range2 = sqr(max - average);

        for (int i = 0; i < result.length; i++) {
            //因为小红包的数量通常是要比大红包的数量要多的，因为这里的概率要调换过来。
            //当随机数>平均值，则产生小红包
            //当随机数<平均值，则产生大红包
            double temp = 0;
            if (nextLong(min, max) > average) {
                // 在平均线上减钱
                temp = min + xRandom(min, average);
                temp =Double.valueOf(df.format(temp));
                result[i] = temp;
                total -= temp;
            } else {
                // 在平均线上加钱
                temp = max - xRandom(average, max);
                temp =Double.valueOf(df.format(temp));
                result[i] = temp;
                total -= temp;
            }

//            System.out.println("temp:"+temp);
        }
        // 如果还有余钱，则尝试加到小红包里，如果加不进去，则尝试下一个。
        while (total > 0) {
            for (int i = 0; i < result.length; i++) {
                if (total > 0 && result[i] < max) {
                    result[i] =Double.valueOf(df.format(result[i]+0.01));
                    total -= 0.01;
                }
            }
        }
        // 如果钱是负数了，还得从已生成的小红包中抽取回来
        while (total < 0) {
            for (int i = 0; i < result.length; i++) {
                if (total < 0 && result[i] > min) {
                    result[i] =Double.valueOf(df.format(result[i]-0.01));
                    total+=0.01;
                }
            }
        }
        return result;
    }

    static double sqrt(double n) {
        // 改进为查表？
        return (double) Math.sqrt(n);
    }

    static double sqr(double n) {
        // 查表快，还是直接算快？
        return n * n;
    }

    static double nextLong(double n) {

        return random.nextDouble()*n;
    }

    static double nextLong(double min, double max) {
        return random.nextDouble()*(max - min) + min;
    }
}
