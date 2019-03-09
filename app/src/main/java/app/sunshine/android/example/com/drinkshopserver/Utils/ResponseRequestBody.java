package app.sunshine.android.example.com.drinkshopserver.Utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ResponseRequestBody extends RequestBody {
    File file;
    ProgressUpdate callback;
    public final int DEFAULT_BUFFER_LENGTH=4096;

    public ResponseRequestBody(File file, ProgressUpdate callback) {
        this.file = file;
        this.callback = callback;
    }
    @Override
    public long contentLength() throws IOException {
        return file.length();
    }
    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse("image/*");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
     long fileLenght=file.length();
     byte[] buffer=new byte[DEFAULT_BUFFER_LENGTH];
        FileInputStream fileInputStream=new FileInputStream(file);
        Handler  handler=new Handler(Looper.getMainLooper());
        try {
            int read=0,uploaded=0;
            while((read=fileInputStream.read(buffer))!=-1){
             handler.post(new progressUpdater(fileLenght,uploaded));
               uploaded+= read;
               sink.write(buffer,0,read);
            }
        }
       catch (Exception e){}
    }

    private class progressUpdater implements Runnable {
        long fileLenght,uploaded;
        public progressUpdater(long fileLenght, long uploaded) {
            this.fileLenght=fileLenght;
            this.uploaded=uploaded;
        }

        @Override
        public void run() {
            callback.onProgressUpdate((int)(100*uploaded/fileLenght));
        }
    }
}
