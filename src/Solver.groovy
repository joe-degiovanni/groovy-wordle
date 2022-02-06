class Solver {

    String answer
    List<String> myGuesses = []
    List<String> include = []
    List<String> letterRegex = '.....'.split('')

    final List<String> candidateGuesses = Main.allAnswers.clone()

    List<String> solve() {
        while (!myGuesses.contains(answer)) {
            rankCandidates()
            nextGuess()
            check()
            eliminateInvalidCandidates()
        }
        return myGuesses
    }

    def getGuess() {
        myGuesses.last()
    }

    def rankCandidates() {
        Map<String, Integer> frequencyCounts = candidateGuesses.collect {it.split('') }.flatten().countBy {it }
        def rank =  { word -> word.split("").toUnique().sum {frequencyCounts[it]} as int}
        candidateGuesses.sort(true, {word ->  rank(word)}).reverse(true)
    }

    def eliminateInvalidCandidates() {
        candidateGuesses.remove(guess)
        candidateGuesses.removeIf { candidate -> !(candidate ==~ "$regex") }
        candidateGuesses.removeIf { candidate ->
            List<String> search = [] + include
            candidate.split('').each { letter ->
                search.remove(letter)
            }
            return !search.isEmpty()
        }
    }

    def nextGuess() {
        myGuesses << candidateGuesses.pop()
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

}
