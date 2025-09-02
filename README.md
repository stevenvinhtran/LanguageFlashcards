# LanguageFlashcards

A minimal, local-first flashcard app for languages. **Day-by-day reviews are scheduled from your retention using a modified SuperMemo-2 (SM-2) algorithm.**

---

## Features
- Vocabulary and grammar cards
- Daily review queue using SM-2 (modified to be more aggressive, all failures reset to learning steps)
- Sortable browser (Term, Definition, Type, Next Review Date)
- Plain CSV storage in `./data/`
- No accounts, no network

---

## CSV Format (cards)

CSV files live in ./data/. The app expects this header and order:

```bash
term,definition,type,reviewDate,dateAdded,repetitions,easeFactor,interval,isNewCard,isRelearning
```

term / definition: strings
type: Vocabulary or Grammar
reviewDate / dateAdded: YYYY-MM-DD HH:MM:SS
repetitions: integer (SM-2 repetition count)
easeFactor: decimal (SM-2 EF)
interval: integer days until next review
isNewCard / isRelearning: true/false

Example Card
```bash
Bonjour,Hello,Vocabulary,2025-08-09 00:00:38,2023-11-01 12:00:00,3,2.5,14,false,false
```

---

## Quick Start

**Prereqs:** JDK 17+ recommended. Maven is optional (wrapper included).

Jar executable has not been constructed yet. If you want to play around with the code, do the following.

# 1) Clone
```bash
git clone https://github.com/stevenvinhtran/LanguageFlashcards.git
cd LanguageFlashcards
```
# 2) Run from IDE (IntelliJ recommended)
- Open the project, then run the application's main class (with a public static void main).

