import dev.refinedtech.configure.Configurations;
import dev.refinedtech.configure.storage.ConfigStorage;

import java.util.Arrays;

public class TestConfig {

    public static void main(String[] args) {

        /*
         * Annotations:
         *  - @Comment
         *
         * Getter:
         *  Not defaulted:
         *    Structure: <type> get<NameOfVariable>(<type> def...)
         *
         *    Example: String getName(String... def)
         *
         *    The argument in the getter is optional, there is no need to keep it.
         *
         *  Defaulted:
         *    Structure: default <type> get<NameOfVariable>(<type> def...) {
         *                   return defaultValue;
         *               }
         *
         *    Example:   default String getName() {
         *                   return "DefaultName";
         *               }
         * */

        Config myConfig = Configurations.create(Config.class, "myConfig.yml", file -> new ConfigStorage(file) {

            @Override
            public Object get(String path) {
                return path.equals("config.name") ? "Somebody" : null;
            }

            @Override
            public void set(String path, Object obj) {

            }

        });

        long[] times = new long[1000];
        String name = null;
        for (int i = 0; i < times.length; i++) {
            long start = System.nanoTime();
            name = myConfig.getName();
            long end = System.nanoTime();
            times[i] = end - start;
        }

        long min = Arrays.stream(times).min().getAsLong();
        long max = Arrays.stream(times).max().getAsLong();
        long avg = Arrays.stream(times).sum() / times.length;

        System.out.printf("Over %s iteration(s):%n%n------------%nMin: %sns (%sms)%nMax: %sns (%sms)%nAvg: %sns (%sms)%n------------%n%n%n",
            times.length,
            min, min / 1_000_000,
            max, max / 1_000_000,
            avg, avg / 1_000_000
        );

//        System.out.println("Took: " + (end - start) / 1_000_000 + "ms");
        System.out.println("Name: " + name);
    }

}
