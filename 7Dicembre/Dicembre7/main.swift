import Foundation

let partOneCardValues: [Character] = ["A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"].reversed()
let partTwoCardValues: [Character] = ["A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J"].reversed()

let fiveOf = 6
let fourOf = 5
let fullHouse = 4
let threeOf = 3
let twoPair = 2
let onePair = 1
let highCard = 0

let mappingPartTwo: [Int: [Int: Int]] = [
    fourOf: [1: fiveOf, 4: fiveOf],
    fullHouse: [2: fiveOf, 3: fiveOf],
    threeOf: [1: fourOf, 3: fourOf],
    twoPair: [1: fullHouse, 2: fourOf],
    onePair: [1: threeOf, 2: threeOf],
    highCard: [1: onePair]
]

enum Part {
    case one
    case two
}

func calculatePoints(hand: [Character]) -> Int {
    let occurrencies: [Character: Int] = hand.reduce(into: [:]) { (counts, char) in
        counts[char, default: 0] += 1
    }
    if (occurrencies.count == 1) {
        return fiveOf
    } else if (occurrencies.count == 2) {
        if (occurrencies.values.contains(where: {$0 == 1})) {
            return fourOf
        } else {
            return fullHouse
        }
    } else if (occurrencies.count == 3) {
        if (occurrencies.values.contains(where: {$0 == 3})) {
            return threeOf
        } else {
            return twoPair
        }
    } else if (occurrencies.count == 4) {
        return onePair
    } else {
        return highCard
    }
}

class Hand : Comparable {
    
    static func == (lhs: Hand, rhs: Hand) -> Bool {
        return lhs.hand == rhs.hand && lhs.bid == rhs.bid
    }
    
    static func < (lhs: Hand, rhs: Hand) -> Bool {
        if (lhs.points != rhs.points) {
            return lhs.points < rhs.points
        }
        for i in 0..<5 {
            guard let valueCardL = (lhs.part == .one ? partOneCardValues : partTwoCardValues).firstIndex(of: lhs.hand[i]) else {
                preconditionFailure("\(lhs.hand[i]) not found in card values")
            }
            guard let valueCardR = (rhs.part == .one ? partOneCardValues : partTwoCardValues).firstIndex(of: rhs.hand[i]) else {
                preconditionFailure("\(rhs.hand[i]) not found in card values")
            }
            if (valueCardL != valueCardR) {
                return valueCardL < valueCardR
            }
        }
        return false
    }
    
    let hand: [Character]
    let bid: Int
    let points: Int
    let part: Part
    
    private init(hand: [Character], bid: Int, points: Int, part: Part) {
        self.hand = hand
        self.bid = bid
        self.points = points
        self.part = part
    }
    
    static func partOne(hand: [Character], bid: Int) -> Hand {
        let points = calculatePoints(hand: hand)
        return Hand(hand: hand, bid: bid, points: points, part: .one)
    }
    
    static func partTwo(hand: [Character], bid: Int) -> Hand {
        let js = hand.filter({$0 == "J"}).count
        let points = calculatePoints(hand: hand)
        let jolly = mappingPartTwo[points, default: [:]][js, default: points]
        return Hand(hand: hand, bid: bid, points: jolly, part: .two)
    }
}



func readFile(file: String) -> [String]? {
    do {
        let content = try String(contentsOfFile: file, encoding: .utf8)
        return content.components(separatedBy: "\n")
    } catch {
        print("\(file) not found")
    }
    return nil
}

func execute(part: Part) {
    guard let lines = readFile(file: ".../AdventOfCode2023/7Dicembre/input.txt") else {
        return
    }
    
    var hands: [Hand] = []
    for line in lines.filter({!$0.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty}) {
        let parts = line.components(separatedBy: " ")
        guard let bid = Int(parts[1].trimmingCharacters(in: .whitespacesAndNewlines)) else {
            preconditionFailure("\(parts[1]) is not integer")
        }
        let hand = part == .one ? Hand.partOne(hand: Array(parts[0]), bid: bid) : Hand.partTwo(hand: Array(parts[0]), bid: bid)
        hands.append(hand)
    }
    hands.sort()
    
    
    let totalWin = hands.enumerated()
        .map({ (index, hand) in return hand.bid * (index + 1) })
        .reduce(0, { (acc, val) in return acc + val })
    
    print("Total win is \(totalWin)")
}


execute(part: .one)
execute(part: .two)
