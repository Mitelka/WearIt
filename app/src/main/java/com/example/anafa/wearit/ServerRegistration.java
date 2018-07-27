package com.example.anafa.wearit;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerRegistration
{
    private String FirsName;
    private String lastName;
    private String nickname;
    private String EmailAddress;
    private String Password;


    public ServerRegistration()
    {

    }
     public JSONObject createJSonToServer(String FirsName,String lastName,String nickname, String EmailAddress, String Password)
    {
        JSONObject RegistrationJson  = new JSONObject();
        try {
            RegistrationJson.put("FirsName", FirsName);
            RegistrationJson.put("FirsName", lastName);
            RegistrationJson.put("nickname", nickname);
            RegistrationJson.put("EmailAddress", EmailAddress);
            RegistrationJson.put("Password", Password);
        }
        catch (JSONException e)
        {
            //TODO - Auto-generated catch block
            e.printStackTrace();
        }
        return RegistrationJson;
    }
}
