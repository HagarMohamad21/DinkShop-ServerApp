package app.sunshine.android.example.com.drinkshopserver.Models;

public class Token {
    public String Token;
    public String Phone;
    public int IsServerToken;
    public Token(){}

    public Token(String token, String phone, int isServerToken) {
        Token = token;
        Phone = phone;
        this.IsServerToken = isServerToken;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getIsServerToken() {
        return IsServerToken;
    }

    public void setIsServerToken(int isServerToken) {
        this.IsServerToken = isServerToken;
    }
}
