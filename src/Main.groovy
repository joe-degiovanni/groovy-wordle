class Main {

    static final Comparator<String> rankCompare = Comparator.comparing({ word -> allAnswers.contains(word) })
            .thenComparing(Comparator.comparing({ word -> rank(word as String) })).reversed()

    static List<String> allAnswers = new File('./src/wordle-answers-alphabetical.txt').text.split('\n')

    static final List<String> candidateGuesses = (allAnswers + new File('./src/wordle-allowed-guesses.txt').text.split('\n')
            .toList().toSet())
            .sort(true, rankCompare)

    static int rank(String word) {
        word.split("").toUnique().size() + commonLetterCount(word)
    }

    static int commonLetterCount(String word) {
        word.split('').count { it in 'rstlne'.split('') }
    }

    static void main(String[] args) {
        File output = new File('output.txt')
        int numPuzzles = allAnswers.size()
        int numGuesses = 0
        allAnswers
                .take(numPuzzles)
                .each {answer ->
            List<String> guesses = new Solver(answer: answer).solve()
            numGuesses += guesses.size()
            output << guesses.join(",") + '\n'
        }
        println "average solution ${numGuesses/numPuzzles}"
    }
}
