class Solver {

    String answer
    List<String> myGuesses = []
    Set<String> include = []
    List<String> letterRegex = '.....'.split('')

    final List<String> candidateGuesses = Main.allFiveLetterWords.clone()


    List<String> solve() {
        while (!myGuesses.contains(answer)) {
            nextGuess()
            check()
        }
        return myGuesses
    }

    def getGuess() {
        myGuesses.last()
    }

    def nextGuess() {
        candidateGuesses.removeIf{ candidate ->!(candidate ==~ "$regex")}
        candidateGuesses.removeIf { candidate ->!include.every {candidate.contains(it)} }
        myGuesses << candidateGuesses.first()
        candidateGuesses.remove(myGuesses.last())
    }

    def check() {
        (0..4).each { pos ->
            String letter = guess.toCharArray()[pos] as String
            if (answer.toCharArray()[pos] as String == letter) {
                // green
                include << letter
                set(pos, letter)
            } else if (answer.contains(letter)) {
                // yellow
                include << letter
                exclude(pos, letter)
            } else {
                // black
                exclude(letter)
            }
        }
    }

    def exclude(int index, String letter) {
        def current = letterRegex[index].replaceAll('[^a-z]','')
        def lettersToExclude = (current + letter).toCharArray().toSet().join("")
        letterRegex[index] = "[^$lettersToExclude]"
    }

    def exclude(String letter) {
        (0..4).each { if (!(letterRegex[it] ==~ /[a-z]/)) exclude(it, letter) }
    }

    def set(int index, String letter) {
        letterRegex[index] = letter
    }

    def getRegex() {
        return letterRegex.join("")
    }

}
