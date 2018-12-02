package candor.example.com.etutiony;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class ExamItem {

    @PrimaryKey
    private long timestamp;
    private String name;
    private String number_of_question;
    private String correct_score;
    private String incorrect_score;

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ExamItem(long timestamp , String name, String number_of_question, String correct_score, String incorrect_score) {

        this.timestamp = timestamp;
        this.name = name;
        this.number_of_question = number_of_question;
        this.correct_score = correct_score;
        this.incorrect_score = incorrect_score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber_of_question() {
        return number_of_question;
    }

    public void setNumber_of_question(String number_of_question) {
        this.number_of_question = number_of_question;
    }

    public String getCorrect_score() {
        return correct_score;
    }

    public void setCorrect_score(String correct_score) {
        this.correct_score = correct_score;
    }

    public String getIncorrect_score() {
        return incorrect_score;
    }

    public void setIncorrect_score(String incorrect_score) {
        this.incorrect_score = incorrect_score;
    }

ExamItem(Builder builder){

        timestamp = builder.timestamp;
        name = builder.name;
        number_of_question = builder.number_of_question;
        correct_score = builder.correct_score;
        incorrect_score = builder.incorrect_score;
    }

    static class Builder {
        private long timestamp;
        private String name;
        private String number_of_question;
        private String correct_score;
        private String incorrect_score;

        public Builder setTimestamp(final long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setName(final String name) {
            this.name = name;
            return this;
        }

        public Builder setNumberOfQuestion(final String number_of_question) {
            this.number_of_question = number_of_question;
            return this;
        }

        public Builder setCorrectScore (final String correct_score) {
            this.correct_score = correct_score;
            return this;
        }

        public Builder setInCorrectScore (final String incorrect_score) {
            this.incorrect_score = incorrect_score;
            return this;
        }


        public ExamItem create() {
            return new ExamItem(this);
        }
    }
}
