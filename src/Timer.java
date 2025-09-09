public class Timer {
    private int startTime;
    private int endTime;

    //constructor
    public Timer(){
        this.startTime = 0;
        this.endTime = 0;
    }

    //start the timer
    public void start(){
        startTime = (int) System.currentTimeMillis();
    }

    //stop the timer
    public void stop(){
        endTime = (int) System.currentTimeMillis();
    }

    //resume the timer
    public void resume(){
        startTime = (int) System.currentTimeMillis() - getElapsedTime();
    }

    //reset the timer
    public void reset(){
        startTime = 0;
        endTime = 0;
    }

    //get the elapsed time in milliseconds
    public int getElapsedTime(){
        return endTime - startTime;
    }

    //format the elapsed time in minutes and seconds
    public String formatElapsedTime(){
        int elapsedTime = getElapsedTime();
        int minutes = elapsedTime / 60000;
        int seconds = (elapsedTime % 60000) / 1000;
        return String.format("%02d:%02d", minutes, seconds);
    }

}//end  of Timer
