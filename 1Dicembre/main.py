from functools import reduce
import re


digit_pattern = re.compile(r'[0-9]')
num_pattern = re.compile(r'[0-9]|one|two|three|four|five|six|seven|eight|nine')
num_dict = {
    'one': 1,
    'two': 2,
    'three': 3,
    'four': 4,
    'five': 5,
    'six': 6,
    'seven': 7,
    'eight': 8,
    'nine': 9
}

def remove_last(line: str) -> str:
    if line.endswith('\n'):
        return line[0:len(line) - 1]
    return line


def openinput() -> list[str]:
    with open('./input.txt', 'r') as file:
        return list(map(remove_last, file.readlines()))


def take_number(line: str, pattern: re.Pattern) -> int:
    nums = re.findall(pattern, line)
    nums = list(map(lambda num: num_dict.get(num, num), nums))
    return int(f'{nums[0]}{nums[len(nums)-1]}')


def first():
    lines = openinput()
    parsed = list(map(lambda line: take_number(line, digit_pattern), lines))
    sum = reduce(lambda prev, curr: prev + curr, parsed, 0)
    print(f'Risultato di calibrazione: {sum}')


def second():
    lines = openinput()
    parsed = list(map(lambda line: take_number(line, num_pattern), lines))
    sum = reduce(lambda prev, curr: prev + curr, parsed, 0)
    print(f'Risultato di calibrazione: {sum}')


second()