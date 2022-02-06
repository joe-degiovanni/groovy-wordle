class Main {

    static List<String> allAnswers = new File('./src/wordle-answers-alphabetical.txt').text.split('\n')
    static List<String> allowedGuesses = new File('./src/wordle-allowed-guesses.txt').text.split('\n')
    static List<String> allFiveLetterWords = allAnswers + allowedGuesses

    static Map<String, Integer> frequencyCounts = allAnswers
            .collect {word -> word.split('') }
            .flatten()
            .countBy {letter -> letter }

    static int rank(String word) {
        return word.split("").toUnique().sum {frequencyCounts[it]} as int
    }

    static void main(String[] args) {
        File output = new File('output.txt')
        output.text = ''
        allFiveLetterWords.sort(true, {word -> word in allAnswers ? rank(word)*2 : rank(word)}).reverse(true)
        int numPuzzles = allAnswers.size()
        int numGuesses = 0
        allAnswers.take(numPuzzles).each {answer ->
            List<String> guesses = new Solver(answer: answer).solve()
            numGuesses += guesses.size()
            output << guesses.join(",") + '\n'
        }
        println "average solution ${numGuesses/numPuzzles}"
    }
}
