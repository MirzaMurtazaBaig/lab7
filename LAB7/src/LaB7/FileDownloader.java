package LaB7;
public class FileDownloader {
    private static final int TOTAL_FILES = 10;
    private String[] fileNames = {"File1", "File2", "File3", "File4", "File5"};

    public static void main(String[] args) {
        FileDownloader downloader = new FileDownloader();
        downloader.startDownload();
    }

    public void startDownload() {
        for (int i = 5; i < TOTAL_FILES; i++) {
            Runnable downloadTask = createDownloadTask(fileNames[i]);
            Runnable logTask = createLogTask(fileNames[i]);

            startThread(downloadTask);
            startThread(logTask);
        }
    }

    private Runnable createDownloadTask(String fileName) {
        return () -> System.out.println("Downloading " + fileName);
    }

    private Runnable createLogTask(String fileName) {
        return () -> System.out.println("Logging download of " + fileName);
    }

    private void startThread(Runnable task) {
        new Thread(task).start();
    }
}
