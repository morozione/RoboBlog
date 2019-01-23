//package com.morozione.roboblog.database;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Handler;
//import android.util.Log;
//
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageMetadata;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.maxml.offirent.BuildConfig;
//import com.maxml.offirent.utils.OffirentConstants;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ImageDao  {
//    private StorageReference storageRef;
//
//    public ImageDao(Handler handler) {
//        super(handler);
//
//        if (storageRef == null) {
//            storageRef = FirebaseStorage.getInstance().getReference();
//        }
//    }
//
//    public void load(String name) {
//        File localFile = null;
//        try {
//            localFile = File.createTempFile("Images", "jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        final File finalLocalFile = localFile;
//        if (localFile == null)
//            return;
//
//        storageRef.child(name).getFile(localFile)
//                .addOnSuccessListener(taskSnapshot -> {
//                    Bitmap image = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
//                    List<Bitmap> list = new ArrayList<>();
//                    list.add(image);
//                    success(OffirentConstants.HANDLER_IMAGE_LIST, list);
//                }).addOnFailureListener(e -> {
//        });
//    }
//
//    public void save(String uri) {
//        StorageMetadata metadata = new StorageMetadata
//                .Builder()
//                .setContentType("image/jpg")
//                .build();
//
//        Uri file = Uri.fromFile(new File(uri));
//        final String s = "image-" + file.getLastPathSegment().hashCode() + "-" + file.getLastPathSegment();
//        UploadTask uploadTask = storageRef.child(s).putFile(file, metadata);
//
//        uploadTask.addOnProgressListener(taskSnapshot -> {
//            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//            if (BuildConfig.DEBUG)
//                Log.i(OffirentConstants.LOG_TAG, "onProgress " + (int) progress);
//        }).addOnSuccessListener(taskSnapshot -> {
//            if (BuildConfig.DEBUG)
//                Log.i(OffirentConstants.LOG_TAG, "onSuccess Https " + taskSnapshot.getDownloadUrl());
//            success(OffirentConstants.IMAGEDAO_RESULT_PATH_OK, s);
//        }).addOnFailureListener(e -> {
//            if (BuildConfig.DEBUG)
//                Log.i(OffirentConstants.LOG_TAG, "onFailure " + e.toString());
//        });
//    }
//
//    public void saveBitmap(Bitmap bitmap, String nameImage) {
//        final String path = "image-" + nameImage;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = storageRef.child(path).putBytes(data);
//        uploadTask.addOnProgressListener(taskSnapshot -> {
//            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//            success(OffirentConstants.IMAGEDAO_UPLOAD_PROGRESS, progress);
//            if (BuildConfig.DEBUG)
//                Log.i(OffirentConstants.LOG_TAG, "onProgress " + (int) progress);
//        }).addOnFailureListener(e -> {
//            if (BuildConfig.DEBUG)
//                Log.i(OffirentConstants.LOG_TAG, "onError loading: " + e.getMessage());
//        }).addOnSuccessListener(taskSnapshot -> {
//            if (BuildConfig.DEBUG)
//                Log.i(OffirentConstants.LOG_TAG, "image loated");
//            if (taskSnapshot.getMetadata() == null ||
//                    taskSnapshot.getMetadata().getDownloadUrl() == null) {
//                return;
//            }
//            String temp = taskSnapshot.getMetadata().getDownloadUrl().toString();
//            success(OffirentConstants.IMAGEDAO_RESULT_PATH_OK, temp);
//        });
//    }
//}
