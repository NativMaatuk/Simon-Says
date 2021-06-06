package com.example.simon_says;

public class Services {
    //NOTE: to example the parameter = "nativma22@gmail.com" send back "nativma22@gmail"
    public static String cutEmail(String str){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<str.length();i++){
            if(str.charAt(i) == '.') break;
            else
                sb.append(str.charAt(i));
        }
        return String.valueOf(sb);
    }
    //NOTE: to example the parameter = "nativma22@gmail.com" send back "nativma22"
    public static String cutEmailToName(String str){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<str.length();i++){
            if(str.charAt(i) == '@') break;
            else
                sb.append(str.charAt(i));
        }
        return String.valueOf(sb);
    }
}
