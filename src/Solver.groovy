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
            def tmp = candidate.split('')
            tmp.each { letter ->
                search.remove(letter)
            }
            return !search.isEmpty()
        }
        myGuesses << (myGuesses.isEmpty() ? 'salet' : candidateGuesses.first())
        candidateGuesses.remove(guess)
    }

    def check() {
        include = []
        List<String> answerLetters = answer.split('')
        List<String> guessLetters = guess.split('')
        (0..4).each {position ->
            String guessLetter = guessLetters[position]
            if(guessLetter == answerLetters[position]) {
                // exact match -- green
                set(position, guessLetter)
                include << guessLetter
                answerLetters[position] = '-'
            }
        }
        (0..4).each {position ->
            String guessLetter = guessLetters[position]
            if(answerLetters.contains(guessLetter) && letterRegex[position] != guessLetter) {
                // yellow
                exclude(position, guessLetter)
                include << guessLetter
                answerLetters[answerLetters.indexOf(guessLetter)] = '-'
            } else if (!include.contains(guessLetter)) {
                // black
                exclude(guessLetter)
            }
        }
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
        println new Solver(answer: 'folio').solve()
        println new Solver(answer: 'bible').solve()
        println new Solver(answer: 'igloo').solve()
    }

}
