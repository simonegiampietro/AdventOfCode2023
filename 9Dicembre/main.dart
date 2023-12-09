import 'dart:io';

List<List<int>> generateHistoryProgression(List<int> history) {
  List<List<int>> historyProgression = [history];
  while (historyProgression[0].any((n) => n != 0)) {
    List<int> newNumbers = [];
    for (int i = 0; i < historyProgression[0].length - 1; i++) {
      newNumbers.add(historyProgression[0][i + 1] - historyProgression[0][i]);
    }
    historyProgression.insert(0, newNumbers);
  }
  return historyProgression;
}

void partOne() {
  final lines = File('input.txt').readAsLinesSync();

  final sum = lines.map((line) {
    final history = line.trim().split(' ').map((s) => int.parse(s)).toList();
    List<List<int>> historyProgression = generateHistoryProgression(history);
    for (int i = 0; i < historyProgression.length - 1; i++) {
      final lastFirstLine = historyProgression[i].last;
      final lastSecondLine = historyProgression[i + 1].last;

      historyProgression[i + 1].add(lastFirstLine + lastSecondLine);
    }
    return historyProgression.last.last;
  }).reduce((act, val) => act + val);

  print("Forward histories sum is $sum");
}

void partTwo() {
  final lines = File('input.txt').readAsLinesSync();

  final sum = lines.map((line) {
    final history = line.trim().split(' ').map((s) => int.parse(s)).toList();
    List<List<int>> historyProgression = generateHistoryProgression(history);
    for (int i = 0; i < historyProgression.length - 1; i++) {
      final firstFirstLine = historyProgression[i].first;
      final firstSecondLine = historyProgression[i + 1].first;

      historyProgression[i + 1].insert(0, firstSecondLine - firstFirstLine);
    }
    return historyProgression.last.first;
  }).reduce((act, val) => act + val);

  print("Backward histories sum is $sum");
}

void main() {
  partOne();
  partTwo();
}
