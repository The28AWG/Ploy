package io.github.the28awg.ploy.platform.android.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.github.the28awg.ploy.platform.android.PloyActivity;

/**
 * Created by the28awg on 24.12.16.
 */

public class IO extends io.github.the28awg.ploy.experiential.io.IO {
    @Override
    public InputStream stream(String file) throws IOException {
        return PloyActivity.context().getAssets().open(file);
    }

    @Override
    public String string(String file) throws IOException {
        StringBuilder s = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream(file)));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            s.append(line).append("\r\n");
        }
        bufferedReader.close();
        return s.toString();
    }
}
