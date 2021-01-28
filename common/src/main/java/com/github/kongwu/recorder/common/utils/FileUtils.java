package com.github.kongwu.recorder.common.utils;

import com.github.kongwu.recorder.common.constant.Constants;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {

    private static final String PLUGIN_RECORDER_PATH = System.getProperty("java.io.tmpdir")+Constants.PLUGIN_PATH+File.separator;

    static {
        File pluginRecorderDirectory = new File(PLUGIN_RECORDER_PATH);
        if (!pluginRecorderDirectory.exists()){
            pluginRecorderDirectory.mkdir();
        }
    }

    public static FileChannel createProcessRecorderFile(int processId) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(createIfAbsent(PLUGIN_RECORDER_PATH+File.separator+processId+Constants.RECORDER_FILE_SUFFIX),"rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        return fileChannel;
    }

    public static File createIfAbsent(String path) throws IOException {
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();
        return file;
    }

    public static void main(String[] args) throws IOException {
        FileChannel fileChannel = createProcessRecorderFile(1111234);
        fileChannel.write(ByteBuffer.wrap("1231234123412344".getBytes()));
    }
}
