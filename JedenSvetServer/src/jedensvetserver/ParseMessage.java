/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jedensvetserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 *
 * @author dhaffner
 */
class ParseMessage {

    InputStream in;
    OutputStream out;

    byte[] buffer;
    static final int BUFFSIZE = 16 * 1024;

    ParseMessage() {
        buffer = new byte[BUFFSIZE];
    }

    void write(String msg) throws IOException {
        out.write(msg.getBytes());
    }

    String read(String startStr, String endStr) throws IOException {
        byte[] start = startStr.getBytes();
        byte[] end = endStr.getBytes();
        int sI = 0;
        int eI = 0;
        byte r;
        int count = 0;
        while ((sI < start.length) && ((r = (byte) in.read()) != -1)) {
            sI = (r == start[sI]) ? sI + 1 : 0;
        }
        while ((eI < end.length) && (count < BUFFSIZE) && ((r = (byte) in.read()) != -1)) {
            buffer[count++] = r;
            eI = (r == end[eI]) ? eI + 1 : 0;
        }
        return new String(buffer, 0, count > end.length ? count - end.length : 0);
    }
}
