public class Timer {
    private long startTime;
    private long endTime;
    private boolean running;

    //constructor
    public Timer(){
        this.startTime = 0;
        this.endTime = 0;
        this.running = false;
    }

    //start the timer
    public void start(){
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    //stop the timer
    public void stop(){
        this.endTime = System.currentTimeMillis();
        this.running = false;
    }

    //resume the timer
    public void resume(){
        this.startTime = System.currentTimeMillis() - getElapsedTime();
        this.running = true;
    }

    //reset the timer
    public void reset(){
        this.startTime = 0;
        this.endTime = 0;
        this.running = false;
    }

    //get the elapsed time in milliseconds
    public int getElapsedTime(){
        if (running) {
            return (int) (System.currentTimeMillis() - startTime);
        } else {
            return (int) (endTime - startTime);
        }
    }

    //format the elapsed time in minutes and seconds
    public String formatElapsedTime(){
        int elapsedTime = getElapsedTime();
        int minutes = elapsedTime / 60000;
        int seconds = (elapsedTime % 60000) / 1000;
        return String.format("%02d:%02d", minutes, seconds);
    }

}//end  of Timer
