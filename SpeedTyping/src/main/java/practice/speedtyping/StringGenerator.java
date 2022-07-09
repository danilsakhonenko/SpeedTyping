package practice.speedtyping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StringGenerator {

    private int _count;
    private boolean _punctuation;
    private DataBaseSession _session;
    private int _language;
    private List<String> _punctArr;

    public StringGenerator(int count, boolean punct, int language, DataBaseSession session) {
        _punctArr = Arrays.asList(",", ".", "!", "?", ";");
        _count = count;
        _punctuation = punct;
        _session = session;
        _language = language;
    }

    public String Generate() throws Exception {
        StringBuilder builder = new StringBuilder();
        Random rand = new Random();
        List<String> list = _session.getWords(_count, _language);
        if (_punctuation) {
            int punct_count = rand.nextInt((int)_count / 2);
            for (int i = 0; i < punct_count; i++) {
                int j = rand.nextInt(_punctArr.size()- 1);
                list.add(rand.nextInt(list.size()), _punctArr.get(j));
            }
        }
        for(String s: list){
            if (!_punctArr.contains(s))
                builder.append(" ");
            builder.append(s);
        }
        builder.delete(0,1);
        return builder.toString();
    }

}
