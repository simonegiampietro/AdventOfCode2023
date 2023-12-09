struct Directions
{
  public readonly string left;
  public readonly string right;

  public Directions(string left, string right)
  {
    this.left = left;
    this.right = right;
  }
}

class Program
{

  private (string, Dictionary<string, Directions>) readInput()
  {
    string[] lines = File.ReadAllLines("input.txt");
    string path = lines[0];

    Dictionary<string, Directions> mapping = lines.TakeLast(lines.Length - 1)
        .Where(line => line.Trim().Length != 0)
        .Select(line =>
        {
          var split = line.Split(" = ");
          var directions = split[1].Split(", ");
          return KeyValuePair.Create(split[0].Trim(), new Directions(directions[0].Substring(1), directions[1].Substring(0, 3)));
        })
        .ToDictionary((pair) => pair.Key, (pair) => pair.Value);
    return (path, mapping);
  }

  private void part1()
  {
    var (path, mapping) = readInput();

    var step = "AAA";
    var count = 0;
    while (step != "ZZZ")
    {
      step = UpdateStep(path, mapping, step);
      count += path.Length;
    }

    Console.WriteLine($"P1: End found in {count} steps");
  }

  private string UpdateStep(string path, Dictionary<string, Directions> mapping, string step) {
    return path.ToCharArray().Aggregate(
      step, (act, direction) => {
        if (direction == 'L')
        {
          return mapping[act].left;
        }
        return mapping[act].right;
      }
    );
  }

  private long GreatestCommonDivisor(long a, long b) {
    return b == 0L ? a : GreatestCommonDivisor(b, a % b);
  }

  private void part2()
  {
    var (path, mapping) = readInput();

    var counters = mapping.Keys.Where(k => k.EndsWith("A"))
                .Select(step => {
                  var actual = step;
                  var count = 0L;
                  while (!actual.EndsWith("Z")) {
                    actual = UpdateStep(path, mapping, actual);
                    count += path.Length;
                  }
                  return count;
                })
                .ToList();
  
    var minimumSteps = counters.Aggregate((acc, act) => {
      var gcd = GreatestCommonDivisor(acc, act);
      return acc * act / gcd;
    });

    Console.WriteLine($"P2: End found in {minimumSteps} steps");
  }

  static void Main()
  {
    new Program().part1();
    new Program().part2();
  }

}

