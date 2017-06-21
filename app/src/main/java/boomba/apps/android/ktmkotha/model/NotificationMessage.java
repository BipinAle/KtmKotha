package boomba.apps.android.ktmkotha.model;


public  class NotificationMessage {
    private   String regId;
    public boolean isRegistered=false;
    public String message;


    public NotificationMessage(String refreshedToken, String message, boolean isRegistered) {
        this.message = message;
        this.isRegistered = isRegistered;
        this.regId=refreshedToken;


    }

    public String getMessage() {
        return message;
    }



    public boolean isRegistered() {
        return isRegistered;
    }



}
