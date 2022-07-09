package practice.speedtyping;

public class Stopwatch{
    private long _startTime;
    private long _endTime;
    private long _totalTime = 0;
    private long _partialTime = 0;
    
    public Stopwatch(){
        start();
    }
    
    public void start(){
        _startTime = System.currentTimeMillis();
    }
    
    public void stop(){
        _endTime = System.currentTimeMillis();
        _totalTime = (_endTime - _startTime) + _partialTime;
    }
    
    public void pause(){
        _partialTime += System.currentTimeMillis() - _startTime;
    }

    public int getSeconds(){
        return (int)(_totalTime/1000);
    }
}
