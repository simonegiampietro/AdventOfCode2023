#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define FILE_OK 0
#define FILE_NO 1

#define N_CARDS 202

#define PATTERN "Card %d: %d %d %d %d %d %d %d %d %d %d | %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d\n"

typedef struct {
  int i;
  int winnings[10];
  int play[25];
} Card;

void readCards(Card cards[]) { 
  FILE* pf;

  if ((pf = fopen("./input.txt", "r")) == NULL) {
    printf("Non sono riuscito a caricare il file input.txt");
    exit(1);
  }

  for (int i = 0; i < N_CARDS; i++) {
    Card* card = &cards[i];
    fscanf(pf, PATTERN, &(card->i), 
      &(card->winnings[0]),
      &(card->winnings[1]),
      &(card->winnings[2]),
      &(card->winnings[3]),
      &(card->winnings[4]),
      &(card->winnings[5]),
      &(card->winnings[6]),
      &(card->winnings[7]),
      &(card->winnings[8]),
      &(card->winnings[9]),
      &(card->play[0]),
      &(card->play[1]),
      &(card->play[2]),
      &(card->play[3]),
      &(card->play[4]),
      &(card->play[5]),
      &(card->play[6]),
      &(card->play[7]),
      &(card->play[8]),
      &(card->play[9]),
      &(card->play[10]),
      &(card->play[11]),
      &(card->play[12]),
      &(card->play[13]),
      &(card->play[14]),
      &(card->play[15]),
      &(card->play[16]),
      &(card->play[17]),
      &(card->play[18]),
      &(card->play[19]),
      &(card->play[20]),
      &(card->play[21]),
      &(card->play[22]),
      &(card->play[23]),
      &(card->play[24])
    );
  }
}

int calculateCardMatch(Card card) {
  int match = 0;
  for (size_t j = 0; j < 25; j++) {
    int playNumber = card.play[j];
    for (size_t k = 0; k < 10; k++) {
      int winningNumber = card.winnings[k];
      if (winningNumber == playNumber) {
        match++;
        break;
      }
    }
  }
  return match;
}

int part1(Card cards[]) {
  int points = 0;
  for (size_t i = 0; i < N_CARDS; i++) {
    int match = calculateCardMatch(cards[i]);
    if (match != 0) {
      points += pow(2, (match - 1));
    }
  }
  return points;
}

int part2(Card cards[]) {
  int repeatCards[N_CARDS];
  for (size_t i = 0; i < N_CARDS; i++) {
    repeatCards[i] = 1;
  }
  
  int count = 0;
  for (size_t i = 0; i < N_CARDS; i++) {
    int match = calculateCardMatch(cards[i]);
    for (size_t j = 0; j < repeatCards[i]; j++) {
      for (size_t k = 1; k < match + 1 && k < N_CARDS - i; k++) {
        repeatCards[i + k] += 1;
      }
    }    
  }
  for (size_t i = 0; i < N_CARDS; i++) {
    count += repeatCards[i];
  }
  return count;
}

int main(int argc, char const *argv[]) {
  Card cards[N_CARDS];
  readCards(cards);

  int points = part1(cards);
  printf("Points for part 1 are: %d\n", points);

  int scratchCards = part2(cards);
  printf("Scratchcards for part 2 are: %d\n", scratchCards);
  return 0;
}
