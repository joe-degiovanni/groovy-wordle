class Main {

    static final Comparator<String> rankCompare = Comparator.comparing({ word -> rank(word as String) }).reversed()

    static List<String> allAnswers = new File('./src/wordle-answers-alphabetical.txt').text.split('\n')
    static List<String> allowedGuesses = new File('./src/wordle-allowed-guesses.txt').text.split('\n')
    static List<String> allFiveLetterWords = allAnswers + allowedGuesses

    static Map<String, Integer> frequencyCounts = allFiveLetterWords
            .collect {word -> word.split('') }
            .flatten()
            .countBy {letter -> letter }

    static final List<String> candidateGuesses = allFiveLetterWords.sort(true, rankCompare)

    static int rank(String word) {
        return word.split("").toUnique().sum {frequencyCounts[it]} as int
    }

    static void main(String[] args) {
        File output = new File('output.txt')
        int numPuzzles = allFiveLetterWords.size()
        int numGuesses = 0
        allFiveLetterWords
                .take(numPuzzles)
                .each {answer ->
            List<String> guesses = new Solver(answer: answer).solve()
            numGuesses += guesses.size()
            output << guesses.join(",") + '\n'
        }
        println "average solution ${numGuesses/numPuzzles}"
    }
}
