class Main {

    static List<String> allAnswers = new File('./src/wordle-answers-alphabetical.txt').text.split('\n')

    static void main(String[] args) {
        File output = new File('output.txt')
        output.text = ''
        allAnswers.each {answer ->  output << new Solver(answer: answer).solve().join(",") + '\n' }
    }
}
