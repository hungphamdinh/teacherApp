package com.hungpham.teacherapp.Presenter.MyAccount;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hungpham.teacherapp.Model.Entities.Tutor;

import java.util.HashMap;

public class MyAccount {
    private MyAccountListener myAccountListener;
    private Uri imageUri;
    public MyAccount(MyAccountListener myAccountListener){
        this.myAccountListener=myAccountListener;
    }
    public void updateTutor(final String phoneKey, HashMap<String,Object>map) {
        DatabaseReference table_user= FirebaseDatabase.getInstance().getReference("Tutor");
        table_user.child(phoneKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);
                map.put("userName",tutor.getUsername());
                map.put("mail",tutor.getEmail());
                map.put("pass",tutor.getPassword());
                map.put("avatar",tutor.getAvatar());
                myAccountListener.updateInform(map);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setOnClick(String phoneKey,HashMap<String,Object>listEdt) {

        final String usernameTemp = listEdt.get("userName").toString();
        final String passwordTemp = listEdt.get("password").toString();
        final String emailTemp = listEdt.get("email").toString();
        if (usernameTemp.equals("") || passwordTemp.equals("") || emailTemp.isEmpty()) {
            myAccountListener.onError("Kiểm tra lại thông tin");
        } else {
            String emailPattern = "[a-zA-Z0-9._-]+@gmail+\\.+com+";
            if (emailTemp.trim().matches(emailPattern)) {
                updateStorage(phoneKey,listEdt);

            }
            else {
                myAccountListener.onError("Sai thông tin email");
            }
        }
    }

    private void updateStorage(String phoneKey,HashMap<String,Object>listEdt) {
        final String usernameTemp = listEdt.get("userName").toString();
        final String passwordTemp = listEdt.get("password").toString();
        final String emailTemp = listEdt.get("email").toString();
        imageUri= (Uri) listEdt.get("imageUri");
        HashMap<String, Object> map = new HashMap<>();
        final String fileName=System.currentTimeMillis()+"";
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        storageReference.child("/Image/").child(fileName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                HashMap<String, Object> map = new HashMap<>();
                String url=taskSnapshot.getDownloadUrl().toString();
                // map.put("courseId", key);
                map.put("avatar", url);
                map.put("username", usernameTemp);
                map.put("password", passwordTemp);
                map.put("email", emailTemp);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tutor");
                reference.child(phoneKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            myAccountListener.onSuccess("Cập nhật thành công");
                        }
                        else{
                            myAccountListener.onError("Cập nhật thất bại");
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myAccountListener.onError("Tải file lên thất bại");
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress=(int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                myAccountListener.onLoading(currentProgress);
            }
        });
    }
}
