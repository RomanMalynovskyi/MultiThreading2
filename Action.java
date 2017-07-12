import java.io.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Малиновский Роман on 10.07.2017.
 */
public class Action {
    private String filesrc;
    private String filedest;
    private FileInputStream fileInputStream;
    private int blockSize;
    private byte[] buffer;
    private boolean isRead;
    private volatile int data;
    private int progress;
    private boolean isCopied;
    CyclicBarrier cyclicBarrier;


    public Action(String filesrc, String filedest, int blockSize) {
        this.filesrc = filesrc;
        this.filedest = filedest;
        this.blockSize = blockSize;

    }

    public void setCopied(boolean copied) {
        isCopied = copied;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress += progress;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public synchronized void readFile() {

        int numberBlock = 1;
        this.buffer = new byte[blockSize];
        try {
            fileInputStream = new FileInputStream(filesrc);
            while (fileInputStream.available() > 0) {
                while (isRead) {
                    wait();
                }
                int d = fileInputStream.read(getBuffer(), 0, getBuffer().length);
                setData(d);
                setProgress(d);
                System.out.println("Block " + numberBlock++ + " has been read!");
                setRead(true);
                notify();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeFile() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filedest)) {
            int numbW = 1;
            cyclicBarrier = new CyclicBarrier(1, new Prog());
            while (fileInputStream.available() > 0) {
                while (!isRead) {
                    wait();
                }
                fileOutputStream.write(getBuffer(), 0, getBuffer().length);
                setBuffer(new byte[blockSize]);
                System.out.println("Block " + numbW++ + " has been written!");
                setRead(false);
                cyclicBarrier.await();
                cyclicBarrier.reset();
                notifyAll();
            }
            fileInputStream.close();
            setCopied(true);
            System.out.println("File has been copied!!!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


    public class Prog extends Thread {
        @Override
        public void run() {
            try {
                showProgress();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void showProgress() throws InterruptedException {
            File file = new File(filesrc);
            System.out.println("Progress " + (getProgress() * 100) / file.length() + " %...");
            Thread.sleep(1000);
        }
    }
}


