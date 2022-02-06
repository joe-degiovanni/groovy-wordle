class Main {

    static List<String> allAnswers = new File('./src/wordle-answers-alphabetical.txt').text.split('\n')
    static List<String> allowedGuesses = new File('./src/wordle-allowed-guesses.txt').text.split('\n')
    static Map<String, Integer> frequencyCounts = allAnswers.collect {it.split('') }.flatten().countBy {it }

    static int rank(String word) {
        return word.split("").toUnique().sum {frequencyCounts[it]} as int
    }

    static List<String> allFiveLetterWords = (allAnswers + allowedGuesses)
            .sort(true, {word -> word in allAnswers ? rank(word)*2 : rank(word)})
            .reverse(true)

    static void main(String[] args) {
        File output = new File('output.txt')
        output.text = ''
        allAnswers.each {answer ->  output << new Solver(answer: answer).solve().join(",") + '\n' }
    }
}
