package adventofcode;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Part1 {

    public void run() {
        Data data = readFile();
        Long minValue = data.seeds.stream()
                .map(seed -> {
                    long transformed = seed;
                    for (Ranges range : data.ranges) {
                        for (Mapping mapping : range.mappings()) {
                            if (mapping.checkLowerThanSource(transformed)) {
                                break;
                            } else if (mapping.checkInSource(transformed)) {
                                transformed = mapping.destinationStart + transformed - mapping.sourceStart;
                                break;
                            }
                        }
                    }
                    return transformed;
                })
                .min(Long::compareTo)
                .orElseThrow();
        System.out.println("------------------");
        System.out.println("Min value found: " + minValue);
    }

    private Data readFile() {
        try (InputStream fileInput = getClass().getResourceAsStream("/input.txt")) {
            if (fileInput == null) throw new RuntimeException("input.txt not found");
            Scanner scanner = new Scanner(fileInput);
            String firstLine = scanner.nextLine();
            List<Long> seeds = Arrays.stream(firstLine.split(" "))
                    .map(s -> {
                        if (s.startsWith("seeds")) return null;
                        return Long.parseLong(s.trim());
                    })
                    .filter(Objects::nonNull)
                    .sorted()
                    .toList();
            List<Ranges> ranges = new ArrayList<>();
            Ranges mappings = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank()) {
                    if (mappings != null) {
                        mappings.sort();
                        ranges.add(mappings);
                    }
                    if (scanner.hasNextLine()) {
                        String descriptionLine = scanner.nextLine();
                        if (descriptionLine == null) throw new RuntimeException("Malformed input.txt");
                        String description = descriptionLine.split(" ")[0];
                        mappings = new Ranges(description);
                    }
                    continue;
                }
                if (mappings == null) throw new RuntimeException("Want to read range with an empty range");
                String[] s = line.split(" ");
                long destinationStart = Long.parseLong(s[0]);
                long sourceStart = Long.parseLong(s[1]);
                long length = Long.parseLong(s[2]);
                Mapping mapping = new Mapping(sourceStart, destinationStart, length);
                mappings.add(mapping);
            }
            if (mappings != null) {
                mappings.sort();
                ranges.add(mappings);
            }
            return new Data(seeds, ranges);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    record Data(
            List<Long> seeds,
            List<Ranges> ranges
    ) {
    }

    record Ranges(
            String description,
            List<Mapping> mappings
    ) {
        Ranges(String description) {
            this(description, new ArrayList<>());
        }

        public void add(Mapping mapping) {
            mappings.add(mapping);
        }

        public void sort() {
            mappings.sort(Comparator.comparingLong(Mapping::sourceStart));
        }
    }

    record Mapping(
            long sourceStart,
            long destinationStart,
            long length
    ) {
        public boolean checkLowerThanSource(long number) {
            return number < sourceStart;
        }

        public boolean checkGreaterThanSource(long number) {
            return number > sourceEnd();
        }

        public boolean checkGreaterThanDestination(long number) {
            return number > destinationEnd();
        }

        public boolean checkInSource(long number) {
            return sourceStart <= number && number <= sourceEnd();
        }

        public boolean checkInDestination(long number) {
            return destinationStart <= number && number <= destinationEnd();
        }

        private long sourceEnd() {
            return sourceStart + length - 1;
        }

        private long destinationEnd() {
            return destinationStart + length - 1;
        }
    }
}
