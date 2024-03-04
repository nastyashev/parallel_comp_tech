public class Score {
    private static int score;
    private static ScoreListener listener;

    public Score(){
        this.score = 0;
    }

    public static void setListener(ScoreListener listener){
        Score.listener = listener;
    }

    public static void increment(){
        score++;
        if (listener != null){
            listener.actionPerformed();
        }
    }

    public static int getScore(){
        return score;
    }
}
