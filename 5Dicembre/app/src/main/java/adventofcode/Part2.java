package adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Part2 {

    public void run() {
        Read read = readFile();

        List<Range> ranges = read.seeds;
        for (Mapping mapping : read.mappings) {
            List<Range> subRanges = new ArrayList<>();

            for (Range range : ranges) {
                List<Range> splitRanges = range.split(mapping.sources);

                for (Range splitRange : splitRanges) {
                    boolean found = false;
                    for (int i = 0; i < mapping.sources.size(); i++) {
                        Range source = mapping.sources.get(i);
                        Range destination = mapping.dests.get(i);
                        if (!splitRange.isZeroOverlap(source)) {
                            long offset = splitRange.start - source.start;
                            long destStart = destination.start + offset;
                            subRanges.add(new Range(destStart, destination.end - (source.end - splitRange.end)));
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        subRanges.add(splitRange);
                    }
                }

            }
            ranges = subRanges;
        }

        Long min = ranges.stream()
                .map(Range::start)
                .min(Long::compareTo)
                .orElseThrow();
        System.out.println("------------------");
        System.out.println("Min value found: " + min);
    }

    private Read readFile() {
        try (InputStream resource = getClass().getResourceAsStream("/input.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
            String seedsLine = reader.readLine();
            String stringSeedsList = seedsLine.split(":")[1].trim();
            String[] seeds = stringSeedsList.split(" ");
            List<Range> seedRanges = new ArrayList<>();
            for (int i = 0; i < seeds.length / 2; i++) {
                long start = Long.parseLong(seeds[i * 2].trim());
                long length = Long.parseLong(seeds[i * 2 + 1].trim());
                seedRanges.add(new Range(start, start + length));
            }

            List<Mapping> mappings = new ArrayList<>();
            Mapping mapping = null;
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    mapping = new Mapping();
                    mappings.add(mapping);
                } else if (!line.endsWith("map:")) {
                    String[] nums = line.split(" ");
                    long source = Long.parseLong(nums[1]);
                    long dest = Long.parseLong(nums[0]);
                    long length = Long.parseLong(nums[2]);
                    Range sourceRange = new Range(source, source + length);
                    Range destRange = new Range(dest, dest + length);
                    mapping.add(sourceRange, destRange);
                }
            }
            if (mapping.sources.isEmpty()) {
                mappings.remove(mapping);
            }
            return new Read(seedRanges, mappings);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    record Read(
            List<Range> seeds,
            List<Mapping> mappings
    ) {
    }

    record Mapping(
            List<Range> sources,
            List<Range> dests
    ) {
        Mapping() {
            this(new ArrayList<>(), new ArrayList<>());
        }

        void add(Range source, Range dest) {
            sources.add(source);
            dests.add(dest);
        }
    }

    record Range(
            long start,
            long end
    ) {

        List<Range> split(List<Range> ranges) {
            Set<Long> points = new TreeSet<>();
            for (Range range : ranges) {
                points.add(range.start);
                points.add(range.end);
            }
            points.add(start);
            points.add(end);
            points.removeIf(p -> p < start || p > end);

            List<Range> result = new ArrayList<>();
            Long starting = null;
            for (Long point : points) {
                if (starting != null) {
                    result.add(new Range(starting, point));
                }
                starting = point;
            }

            return result;
        }

        boolean isZeroOverlap(Range other) {
            if (this.start < other.start)
                return this.end < other.end;
            else if (this.start > other.start)
                return this.end > other.end;
            return false;
        }
    }

}
