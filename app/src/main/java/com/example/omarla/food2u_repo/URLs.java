package com.example.omarla.food2u_repo;

/**
 * Created by hp on 2/27/2018.
 */


public class URLs {
    //Validate Contact No URLs
    public static String ValidateContactURL1 = "http://apilayer.net/api/validate?access_key=ceb880f5d03a6a34974069a530945ea7&number=+91";
    public static String ValidateContactURL2 = "&country_code=&format=1";

    //Validate Email URLs
    public static String ValidateEmailURL1 = "http://apilayer.net/api/check?access_key=6e2a3850859814fe3fbc9c3c08567204&email=";
    public static String ValidateEmaiURL2 = "&smtp=1&format=1";

    public static String BASE_URL = "http://172.26.17.57:8080/";
    public static String REGISTER = BASE_URL+"Signup";
    public static String LOGIN = BASE_URL+"login";


}


