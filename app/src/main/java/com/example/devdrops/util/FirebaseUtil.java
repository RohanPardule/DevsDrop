package com.example.devdrops.util;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
