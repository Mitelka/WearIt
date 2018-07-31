package com.example.anafa.wearit.DTO;

public class SignupDTO extends GenericDTO {

   private String _id;

   private String email;

    public SignupDTO(String _id, String email) {
        this._id = _id;
        this.email = email;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
