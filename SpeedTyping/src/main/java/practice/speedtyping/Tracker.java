package practice.speedtyping;

public class Tracker {
    private StringGenerator _generator;
    private boolean _paused = true;
    private boolean _finished =false;
    private int _errorCount;
    private int _current;
    private int _correctCount;
    private char[] _chars;
    private Stopwatch _watch;
    
    
    public boolean isPaused(){
        return _paused;
    }
    
    public boolean isFinished(){
        return _finished;
    }
    
    public void start(int count,boolean punct, int language, DataBaseSession session) throws Exception{
        _generator = new StringGenerator(count,punct,language,session);
        reset();
    }
    
    public void pause(){
        _paused = true;
        _watch.pause();
    }
    
    public void unpause(){
        _paused = false;
        _watch.start();
    }
    
    public void reset() throws Exception{
        _chars = _generator.Generate().toCharArray();
        _current = 0;
        _errorCount = 0;
        _paused = false;
        _watch= new Stopwatch();
  
    }
    
    private void finish(){
        _watch.stop();
        _finished = true;
    }
    
    public char[] getChars(){
        return _chars;
    }
    
    public boolean isCorrectChar(char input){
        boolean result;
        if(input == _chars[_current]){
            _correctCount++;
            _current++;
            result = true;
            if(_current == _chars.length)
                finish();
        }
        else{
            result = false;
            _errorCount++;
        }
        return result;
    }
    
    public int getCurrentIndex(){
        return _current;
    }
    
    public Object[] getResult(){
        int seconds = _watch.getSeconds();
        int minuteSpeed = (int)(_chars.length/(float)(seconds/60.0));
        float errorRatio = _errorCount/(float)_chars.length;
        Object[] result = {_chars.length,seconds,_errorCount, minuteSpeed,errorRatio};
        return result;
    }
    
}
