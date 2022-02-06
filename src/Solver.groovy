class Solver {

    String answer
    List<String> myGuesses = []
    List<String> include = []
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
        candidateGuesses.removeIf { candidate -> !(candidate ==~ "$regex") }
        candidateGuesses.removeIf { candidate ->
            List<String> search = [] + include
            candidate.split('').each { letter ->
                int found = search.indexOf(letter)
                if (found >= 0) search.remove(found)

            }
            return !search.isEmpty()
        }
        if (candidateGuesses.isEmpty() ) {
            println """
$answer
$guess
$myGuesses
$regex
$include
"""
        }
        myGuesses << (myGuesses.isEmpty() ? 'salet' : candidateGuesses.first())
        candidateGuesses.remove(guess)
    }

    def check() {
        List<String> yellows = []
        List<String> greens = []
        (0..4).each { pos ->
            String letter = guess.toCharArray()[pos] as String
            if (answer.toCharArray()[pos] as String == letter) {
                greens << letter
                set(pos, letter)
            } else if (answer.contains(letter)) {
                // yellow
                if (minusYellowsAndGreens(answer, greens, yellows).contains(letter)) {
                    yellows << letter
                }
                exclude(pos, letter)
            } else {
                // black
                exclude(letter)
            }
        }
        include = yellows
    }

    def minusYellowsAndGreens(String candidate, List<String> greens, List<String> yellows) {
        String result = candidate
        (greens + yellows).each {
            result = result.replaceFirst(it, '-')
        }
        return result
    }

    def exclude(int index, String letter) {
        def current = letterRegex[index].replaceAll('[^a-z]', '')
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

    static void main(String[] args) {
        Main.allFiveLetterWords.sort(true, {word -> word in Main.allAnswers ? Main.rank(word)*2 : Main.rank(word)}).reverse(true)
        println new Solver(answer: 'bible').solve()
    }

}
