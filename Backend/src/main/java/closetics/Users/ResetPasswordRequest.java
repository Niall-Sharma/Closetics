package closetics.Users;

public class ResetPasswordRequest {
    private long id;
    private String oldPassword;
    private String newPassword;
    private String securityQuestionAnswer;

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getSecurityQuestionAnswer() {return securityQuestionAnswer;}
    public void setSecurityQuestionAnswer(String securityQuestionAnswer) {
        this.securityQuestionAnswer = securityQuestionAnswer;}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

}
