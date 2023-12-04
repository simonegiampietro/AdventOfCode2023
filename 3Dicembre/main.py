import functools
import re

type Gear = tuple[int, int, int]

regex_pnt = re.compile("\\.")
regex_num = re.compile("[0-9]")


def read_file() -> list[str]:
    with open('./input.txt', 'r') as file:
        return list(map(lambda line: line.strip(), file.readlines()))


def index_special_chars(line: str) -> list[int]:
    indices = []
    for i in range(len(line)):
        match_pnt = regex_pnt.match(line[i])
        match_num = regex_num.match(line[i])
        if match_pnt is None and match_num is None:
            indices.append(i)
    return indices


def check_valid_number(specials: list[int], min_index: int, max_index: int) -> bool:
    for i in range(len(specials)):
        s_index = specials[i]
        if s_index >= min_index and s_index <= max_index:
            return True
    return False


def find_parts(lines: list[str]) -> list[list[Gear]]:
    special_for_line = list(map(index_special_chars, lines))

    parts = []
    min_char_num = -1
    max_char_num = -1
    for i in range(len(lines)):
        line = lines[i]
        line_parts = []
        parts.append(line_parts)
        for j in range(len(line)):
            char = line[j]

            match_num = regex_num.match(char)

            if match_num:
                if min_char_num == -1:
                    min_char_num = j
                max_char_num = j

            if (not match_num or j == len(line) - 1) and min_char_num != -1 and max_char_num != -1:
                prev_line = i - 1 if i > 0 else -1
                succ_line = i + 1 if i < len(lines) - 1 else -1

                prev_specials = special_for_line[prev_line] if prev_line != -1 else []
                line_specials = special_for_line[i]
                succ_specials = special_for_line[succ_line] if succ_line != -1 else []

                if ((min_char_num - 1) in line_specials or 
                    (max_char_num + 1) in line_specials or 
                    check_valid_number(prev_specials, max(min_char_num - 1, 0), min(max_char_num + 1, len(line) - 1)) or
                    check_valid_number(succ_specials, max(min_char_num - 1, 0), min(max_char_num + 1, len(line) - 1))):
                    num_str = line[min_char_num:max_char_num+1]
                    line_parts.append([int(num_str), min_char_num, max_char_num])

                min_char_num = -1
                max_char_num = -1

    return parts


def first():
    lines = read_file()

    parts = find_parts(lines)
    nums = list(functools.reduce(
        lambda prev, act: [*prev, *list(map(lambda n: n[0], act))],
        parts,
        []
    ))

    sum = 0
    for i in range(len(nums)):
        sum += nums[i]

    print(f"1. The sum of all of the part numbers in the engine schematic is: {sum}")


def index_star(line: str) -> list[int]:
    indices = []
    for i in range(len(line)):
        if line[i] == "*":
            indices.append(i)
    return indices


def calculate_ratio(star: int, line: list[Gear], next_line: list[Gear] | None, prev_line: list[Gear] | None):
    ## same line
    gears = list(filter(lambda gear: star == gear[1] - 1 or star == gear[2] + 1, line))

    ## prev line
    if prev_line is not None:
        prev_gears = list(filter(lambda gear: star >= gear[1] - 1 and star <= gear[2] + 1, prev_line))
        gears = [*gears, *prev_gears]

    ## succ line
    if next_line is not None:
        next_gears = list(filter(lambda gear: star >= gear[1] - 1 and star <= gear[2] + 1, next_line))
        gears = [*gears, *next_gears]
    
    if len(gears) == 2:
        return gears[0][0] * gears[1][0]
    else: 
        if len(gears) < 2:
            return 0
    raise f"Find more of 2 gears for star {star}"


def second():
    lines = read_file()
    stars = list(map(index_star, lines))
    gears = find_parts(lines)

    ratios = []
    for i in range(len(stars)):
        stars_in_line = stars[i]

        line = gears[i]
        next_line = gears[i + 1] if i + 1 < len(lines) else None
        prev_line = gears[i - 1] if i - 1 > -1 else None

        for j in range(len(stars_in_line)):
            star = stars_in_line[j]
        
            ratio = calculate_ratio(star, line, next_line, prev_line)
            ratios.append(ratio)

    sum = functools.reduce(lambda prev, succ: prev + succ, ratios, 0)

    print(f"2. The sum of all of the part numbers in the engine schematic is: {sum}")
            


first()
second()
    