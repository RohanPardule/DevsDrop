package com.example.devdrops.util;

import androidx.annotation.NonNull;

import com.example.devdrops.interfaces.UserModelCallback;
import com.example.devdrops.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class FirebaseUtil {

    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }


    public static boolean isLoggedIn() {
        if (currentUserId() != null) {
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }



    public static DocumentReference PostUsername(String id) {
        return FirebaseFirestore.getInstance().collection("users").document(id);
    }



    // other user details
    public static DocumentReference getOtherUserDetails(String id) {

        return FirebaseFirestore.getInstance().collection("users").document(id);

    }

//    public static UserModel getCurrentUserModel(){
//        final UserModel[] userModel = {new UserModel()};
//        FirebaseFirestore.getInstance().collection("users")
//                .document(FirebaseUtil.currentUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                       userModel[0] = task.getResult().toObject(UserModel.class);
//
//
//
//
//
//                    }
//                });
//
//        return userModel[0];
//    }
    public static void getCurrentUserModel(final UserModelCallback callback) {
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseUtil.currentUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                UserModel userModel = document.toObject(UserModel.class);
                                callback.onUserModelCallback(userModel); // Pass the userModel to the callback
                            } else {
                                // Handle document not existing
                                callback.onUserModelCallback(null);
                            }
                        } else {
                            // Handle task failure
                            callback.onUserModelCallback(null);
                        }
                    }
                });
    }





    //    public static CollectionReference allVetCollectionReference(){
//        return  FirebaseFirestore.getInstance().collection("vet_list");
//    }
//    public static DocumentReference getChatRoomReference(String chatroomId)
//    {
//        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
//    }
//
//    public static  String getChatroomId(String userId,String userId2)
//    {
//        if(userId.hashCode()<userId2.hashCode())
//        {
//            return userId+"_"+userId2;
//        }
//        else {
//            return userId2+"_"+userId;
//        }
//
//    }
//    public static CollectionReference getChatrromMessageReference(String chatroomId){
//        return  getChatRoomReference(chatroomId).collection("chats");
//
//    }
    public static CollectionReference allUserCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }
//    public static CollectionReference allChatroomCollectionReference(){
//        return  FirebaseFirestore.getInstance().collection("chatrooms");
//    }
//    public static DocumentReference getOtherUserFromChatRoom(List<String> userIds) {
//        if (userIds.get(0).equals(FirebaseUtil.currentUserId()))
//        {
//            return allUserCollectionReference().document(userIds.get(1));
//        }
//        else
//        {
//            return allUserCollectionReference().document(userIds.get(0));
//        }
//    }


    public static String timestampTOString(Timestamp timestamp) {
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }


    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public static DocumentReference currentUserDetailsForPosts() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allPostCollectionReference() {
        return FirebaseFirestore.getInstance().collection("posts");
    }
}
