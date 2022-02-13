package com.developer.ck.checkin.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class PathConfig {
    private static final String PARENTFOLDER = "Dental";
    private static final String PHOTOS = "Photos";
    public static final String PHOTOS_PATH = "/" + PARENTFOLDER + "/Photos";
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private static final String VIDEOS = "Videos";
    public static final String VIDEOS_PATH = "/" + PARENTFOLDER + "/Videos";
    public static SdcardSelector sdcardItem = SdcardSelector.BUILT_IN;
    private static List<String> videoList = new ArrayList();

    public enum SdcardSelector {
        BUILT_IN,
        EXTERNAL
    }

    public void setSdcardItem(SdcardSelector item) {
        sdcardItem = item;
    }

    public static String getPhotoPath() {
        String sdCardDir;
        try {
            if (sdcardItem == SdcardSelector.BUILT_IN) {
                sdCardDir = SdCardUtils.getFirstExternPath();
            } else {
                sdCardDir = SdCardUtils.getSecondExternPath();
                if (sdCardDir == null) {
                    return null;
                }
            }
            String photoPath = sdCardDir + "/" + PARENTFOLDER + "/" + PHOTOS + "/";
            File folder = new File(photoPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return new File(photoPath + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis())) + ".jpg").getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getVideoPath(String parentFolder, String videoName) {
        String sdCardDir;
        String absolutePath = null;
        try {
            if (sdcardItem == SdcardSelector.BUILT_IN) {
                sdCardDir = SdCardUtils.getFirstExternPath();
            } else {
                sdCardDir = SdCardUtils.getSecondExternPath();
                if (sdCardDir == null) {
                    return null;
                }
            }
            String videoPath = sdCardDir + "/" + parentFolder + "/";
            File folder = new File(videoPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File saveVideo = new File(videoPath + videoName);
            if (!saveVideo.exists()) {
                saveVideo.createNewFile();
            }
            absolutePath = saveVideo.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return absolutePath;
    }

    public static String getVideoPath() {
        String sdCardDir;
        try {
            if (sdcardItem == SdcardSelector.BUILT_IN) {
                sdCardDir = SdCardUtils.getFirstExternPath();
            } else {
                sdCardDir = SdCardUtils.getSecondExternPath();
                if (sdCardDir == null) {
                    return null;
                }
            }
            String videoPath = sdCardDir + "/" + PARENTFOLDER + "/" + VIDEOS + "/";
            File folder = new File(videoPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return new File(videoPath + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis())) + ".mp4").getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAVIPath() {
        String sdCardDir;
        try {
            if (sdcardItem == SdcardSelector.BUILT_IN) {
                sdCardDir = SdCardUtils.getFirstExternPath();
            } else {
                sdCardDir = SdCardUtils.getSecondExternPath();
                if (sdCardDir == null) {
                    return null;
                }
            }
            String videoPath = sdCardDir + "/" + PARENTFOLDER + "/" + VIDEOS + "/";
            File folder = new File(videoPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            return new File(videoPath + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis())) + ".avi").getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRootPath() {
        if (sdcardItem == SdcardSelector.BUILT_IN) {
            return SdCardUtils.getFirstExternPath();
        }
        String sdCardDir = SdCardUtils.getSecondExternPath();
        if (sdCardDir == null) {
            return null;
        }
        return sdCardDir;
    }

    public static void savePhoto(Context context, String parentFolder, String photoName, byte[] imagedata) {
        String sdCardDir = getRootPath();
        if (sdCardDir != null) {
            try {
                String photoPath = sdCardDir + "/" + parentFolder + "/";
                File folder = new File(photoPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File savePhoto = new File(photoPath, photoName);
                if (!savePhoto.exists()) {
                    savePhoto.createNewFile();
                }
                String absolutePath = savePhoto.getAbsolutePath();
                Log.e("path", absolutePath);
                FileOutputStream fout = new FileOutputStream(absolutePath);
                fout.write(imagedata, 0, imagedata.length);
                fout.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void savePhotoBmp(Context context, String parentFolder, String photoName, Bitmap bmp) {
        if (getRootPath() != null) {
            try {
                File folder = new File(parentFolder);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File savePhoto = new File(parentFolder, photoName);
                if (!savePhoto.exists()) {
                    savePhoto.createNewFile();
                }
                String absolutePath = savePhoto.getAbsolutePath();
                Log.e("path", absolutePath);
                FileOutputStream fout = new FileOutputStream(absolutePath);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                fout.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static String savePhoto(Context context, Bitmap bmp) {
        String sdCardDir = getRootPath();
        if (sdCardDir != null) {
            try {
                String timeString = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
                String photoPath = sdCardDir + "/" + PARENTFOLDER + "/" + PHOTOS;
                Log.d("PathConfig", "save file:" + photoPath);
                File folder = new File(photoPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                    Log.d("PathConfig:", "create folder " + photoPath);
                }
                File savePhoto = new File(photoPath, timeString + ".jpg");
                if (!savePhoto.exists()) {
                    savePhoto.createNewFile();
                }
                String absolutePath = savePhoto.getAbsolutePath();
                Log.e("path", absolutePath);
                FileOutputStream fout = new FileOutputStream(absolutePath);
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, fout);
                fout.close();
                return absolutePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public static String savePhoto(Context context, String fileName, byte[] imagedata) {
        String sdCardDir = getRootPath();
        if (sdCardDir != null) {
            try {
                String photoPath = sdCardDir + "/" + PARENTFOLDER + "/" + PHOTOS;
                Log.d("PathConfig", "save file:" + photoPath);
                File folder = new File(photoPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                    Log.d("PathConfig:", "create folder " + photoPath);
                }
                File savePhoto = new File(photoPath, fileName);
                if (!savePhoto.exists()) {
                    savePhoto.createNewFile();
                }
                String absolutePath = savePhoto.getAbsolutePath();
                Log.e("path", absolutePath);
                FileOutputStream fout = new FileOutputStream(absolutePath);
                fout.write(imagedata, 0, imagedata.length);
                fout.close();
                return absolutePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public static String savePhoto(Context context, byte[] imagedata) {
        String timeString = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        String fileName = timeString + ".jpg";
        return savePhoto(context, fileName, imagedata);
    }

    @SuppressLint({"DefaultLocale"})
    public static List<String> getImagesList(File photoPath) {
        List<String> photoList = new ArrayList<>();
        File[] filterFiles = photoPath.listFiles(new FileFilter() {
            /* class com.wifiview.config.PathConfig.AnonymousClass1 */

            public boolean accept(File file) {
                if (!file.isFile() || (!file.getAbsolutePath().toLowerCase().endsWith(".bmp") && !file.getAbsolutePath().toLowerCase().endsWith(".jpg") && !file.getAbsolutePath().toLowerCase().endsWith(".png"))) {
                    return false;
                }
                return true;
            }
        });
        if (filterFiles != null && filterFiles.length > 0) {
            for (File file : sortVideoList1(Arrays.asList(filterFiles))) {
                if (photoList.indexOf(file.getAbsolutePath()) == -1) {
                    photoList.add(file.getAbsolutePath());
                }
            }
        }
        return photoList;
    }

    public static List<String> getVideosList(File videoPath) {
        videoList.clear();
        getVideoList(videoPath);
        getVideoListNew(videoPath);
        return videoList;
    }

    @SuppressLint({"DefaultLocale"})
    private static void getVideoListNew(File videoPath) {
        List<String> temp = new ArrayList<>();
        File[] files = videoPath.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                List<File> fileList = sortVideoList1(Arrays.asList(files));
                if (fileList.get(i).isFile()) {
                    if (fileList.get(i).getAbsolutePath().toLowerCase().endsWith(".mp4")) {
                        File videoFile = new File(fileList.get(i).getAbsolutePath());
                        if (videoFile.exists() && temp.indexOf(videoFile.getAbsolutePath()) == -1) {
                            temp.add(videoFile.getAbsolutePath());
                            videoList.add(videoFile.toString());
                        }
                    }
                } else if (fileList.get(i).isDirectory() && fileList.get(i).getPath().indexOf("/.") == -1) {
                    getVideoListNew(fileList.get(i));
                }
            }
        }
    }

    @SuppressLint({"DefaultLocale"})
    private static void getVideoList(File videoPath) {
        List<String> temp = new ArrayList<>();
        File[] files = videoPath.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                List<File> fileList = sortVideoList1(Arrays.asList(files));
                if (fileList.get(i).isFile()) {
                    if (fileList.get(i).getAbsolutePath().toLowerCase().endsWith(".avi") || fileList.get(i).getAbsolutePath().toLowerCase().endsWith(".3gp") || fileList.get(i).getAbsolutePath().toLowerCase().endsWith(".mp4")) {
                        String lcPath = fileList.get(i).getAbsolutePath().toLowerCase();
                        String absPath = fileList.get(i).getAbsolutePath();
                        String photopath = null;
                        if (lcPath.contains(".avi")) {
                            photopath = absPath.replace(".avi", ".jpg");
                        } else if (lcPath.contains(".mp4")) {
                            photopath = absPath.replace(".mp4", ".jpg");
                        } else if (lcPath.contains(".3gp")) {
                            photopath = absPath.replace(".3gp", ".jpg");
                        }
                        File photofile = new File(photopath);
                        if (photofile.exists() && temp.indexOf(photofile.getAbsolutePath()) == -1) {
                            temp.add(photofile.getAbsolutePath());
                            videoList.add(photofile.toString());
                        }
                    }
                } else if (fileList.get(i).isDirectory() && fileList.get(i).getPath().indexOf("/.") == -1) {
                    getVideoList(fileList.get(i));
                }
            }
        }
    }

    public static int getSdcardAvilibleSize() {
        StatFs stat = new StatFs(new File(getRootPath()).getPath());
        return (int) (((((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize())) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
    }

    public int getSdcardTotalSize() {
        StatFs stat = new StatFs(new File(getRootPath()).getPath());
        return (int) (((((long) stat.getBlockCount()) * ((long) stat.getBlockSize())) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
    }

    public static List<File> sortVideoList1(List<File> photoList) {
        Collections.sort(photoList, new Comparator<File>() {
            /* class com.wifiview.config.PathConfig.AnonymousClass2 */

            public int compare(File curFile, File nextFile) {
                return curFile.lastModified() > nextFile.lastModified() ? -1 : 1;
            }
        });
        return photoList;
    }

    public List<File> sortVideoList(List<File> photoList) {
        Collections.sort(photoList, new Comparator<File>() {
            /* class com.wifiview.config.PathConfig.AnonymousClass3 */

            public int compare(File curFile, File nextFile) {
                return curFile.lastModified() > nextFile.lastModified() ? 1 : -1;
            }
        });
        return photoList;
    }

    public static void deleteFiles(File file) {
        File[] files;
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                for (File file2 : file.listFiles()) {
                    deleteFiles(file2);
                }
            }
            file.delete();
        }
    }
}
