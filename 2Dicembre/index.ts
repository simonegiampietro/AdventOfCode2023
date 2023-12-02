import { readFileSync } from 'fs';

type Game = [number, string];

type Colors = 'red' | 'blue' | 'green';

type PlayedCubes = {
    red: number,
    blue: number,
    green: number
};

const validCubes: PlayedCubes = {
    red: 12,
    green: 13,
    blue: 14
};

function readFile(): Array<string> {
    const content = readFileSync('./input.txt', 'utf-8');
    return content.split('\r\n');
}

function getGame(line: string): Game {
    const [idString, gameLine] = line.split(':');
    const id = parseInt(idString.substring('Game '.length - 1));
    return [id, gameLine];
}

function checkPossibileGame([_, gameLine]: Game): boolean {
    const sets = gameLine.split(';');
    return sets.find(checkImpossibleSet) === undefined;
}

function checkImpossibleSet(set: string): boolean {
    const playedCubes = getPlayedCubes(set);
    return playedCubes.red > validCubes.red ||
        playedCubes.blue > validCubes.blue ||
        playedCubes.green > validCubes.green;
}

function getPlayedCubes(set: string): PlayedCubes {
    return set.split(',')
        .map<[number, Colors]>(s => {
            const [numString, color] = s.trim().split(' ');
            return [parseInt(numString), color as Colors];
        })
        .reduce<PlayedCubes>((prev, [num, color]) => ({ ...prev, [color]: num }), { red: 0, blue: 0, green: 0 });
}

function fewestCubesForColor([_, gameLine]: Game): PlayedCubes {
    const sets = gameLine.split(';');
    return sets.map(getPlayedCubes)
        .reduce<PlayedCubes>((prev, curr) => ({
            red: curr.red > prev.red ? curr.red : prev.red,
            green: curr.green > prev.green ? curr.green : prev.green,
            blue: curr.blue > prev.blue ? curr.blue : prev.blue
        }), {red: 0, green: 0, blue: 0});
}


function first(): void {
    const lines = readFile();
    const sum = lines.filter(line => line.length > 0)
        .map(getGame)
        .filter(checkPossibileGame)
        .map(([id]) => id)
        .reduce((prev, curr) => prev + curr, 0);
    console.log('Sum of valid games: ' + sum);
}

function second(): void {
    const lines = readFile();
    const sum = lines.filter(line => line.length > 0)
        .map(getGame)
        .map(fewestCubesForColor)
        .map(({red, blue, green}) => red * blue * green)
        .reduce((prev, curr) => prev + curr, 0);
    console.log('Sum of the power: ' + sum);
}

first();
second();
