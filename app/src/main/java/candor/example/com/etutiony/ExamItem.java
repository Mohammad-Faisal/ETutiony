package candor.example.com.etutiony;

public class ExamItem {

    String name;
    int number_of_question;
    int correct_score;
    long incorrect_score;

    ExamItem(Builder builder){
        name = builder.name;
        number_of_question = builder.number_of_question;
        correct_score = builder.correct_score;
        incorrect_score = builder.incorrect_score;
    }

    static class Builder {
        private String name;
        private int number_of_question;
        private int correct_score;
        private int incorrect_score;

        public Builder setName(final String name) {
            this.name = name;
            return this;
        }

        public Builder setNumberOfQuestion(final int number_of_question) {
            this.number_of_question = number_of_question;
            return this;
        }

        public Builder setCorrectScore (final int correct_score) {
            this.correct_score = correct_score;
            return this;
        }

        public Builder setInCorrectScore (final int incorrect_score) {
            this.incorrect_score = incorrect_score;
            return this;
        }


        public ExamItem create() {
            return new ExamItem(this);
        }
    }
}
