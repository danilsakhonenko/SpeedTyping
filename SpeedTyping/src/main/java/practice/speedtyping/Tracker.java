package practice.speedtyping;

public class Tracker {
    private StringGenerator _sg;
    private boolean _paused = true;
    private int _errorCount;
    private int _stringLenght;
    private int _current;
    private int _correctCount;
    char[] _chars;
    
    public boolean isPaused(){
        return _paused;
    }
    
    public void startTest(int count,boolean punct, int language, DataBaseSession session) throws Exception{
        _sg = new StringGenerator(count,punct,language,session);
        _chars = _sg.Generate().toCharArray();
        _current=0;
        _paused = false;
    }
    
    public void reset() throws Exception{
        _chars = _sg.Generate().toCharArray();
        
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
        }
        else
            result = false;
        return result;
    }
    
    public int getCurrentIndex(){
        return _current;
    }
    
}
