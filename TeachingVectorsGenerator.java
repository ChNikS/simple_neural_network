package lab1;

/**
 * Created by ceredniknikita on 01.06.17.
 */
public class TeachingVectorsGenerator {
    protected int[] _sets;
    protected int _cursor;

    TeachingVectorsGenerator(int numberOfInputVectors) {
        _sets = new int[numberOfInputVectors];
        for (int i=0; i<_sets.length; i++) _sets[i] = -1;

        _cursor = 0;
    }

    int[] getNext() {

        while(true) {
            if(_cursor < 0) return null;

            _sets[_cursor]++;
            if (_sets[_cursor] == 16) {
                _cursor--;
                continue;
            } else {
                if(_cursor == _sets.length-1) {
                    int[] result = new int[_sets.length];
                    for (int i=0; i<_sets.length; i++) result[i] = _sets[i];
                    return result;
                } else {
                    _sets[_cursor+1] = _sets[_cursor];
                    _cursor++;
                    continue;
                }
            }


        }


    }
}

