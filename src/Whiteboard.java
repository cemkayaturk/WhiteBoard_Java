import java.io.IOException;

public class Whiteboard {
    public static void main(String[] args) throws IOException {
        Login login = new Login();
        while(true) {
            if (login.getUser().equals("Student")) {
                login.setVisible(false);
                new Student(login.getStudentName());
                break;
            } else if (login.getUser().equals("Teacher")) {
                login.setVisible(false);
                new Teacher();
                break;
            }
        }


    }
}
