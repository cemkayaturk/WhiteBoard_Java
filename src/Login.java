import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame {
    String selectedUser ="";
    String studentName = "";
    String password = "1234";
    public Login() {
        this.setSize(400, 300);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();

        int xPos = (dim.width / 2) - (this.getWidth() / 2);
        int yPos = (dim.height / 2) - (this.getHeight() / 2);
        this.setLocation(xPos, yPos);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Login");
        while (selectedUser.equals("")) {


            JTextField passwordfield = new JTextField();
            passwordfield.setBounds(50, 30, 150, 20);
            JButton teacherbutton = new JButton("Teacher Login");
            teacherbutton.setBounds(50, 60, 130, 30);
            teacherbutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (passwordfield.getText().equals(password)) {
                        selectedUser = "Teacher";
                        System.out.println(selectedUser);
                        System.out.println(passwordfield.getText());
                    } else {
                        System.out.println(passwordfield.getText());
                    }
                }
            });
            JTextField name = new JTextField();
            name.setBounds(50, 100, 150, 20);
            JButton studentbutton = new JButton("Student Login");
            studentbutton.setBounds(50, 130, 130, 30);
            studentbutton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    studentName = name.getText();
                    selectedUser = "Student";
                    System.out.println(selectedUser);
                    System.out.println(studentName);
                }
            });
            this.add(teacherbutton);
            this.add(passwordfield);
            this.add(studentbutton);
            this.add(name);
            this.setLayout(null);
            this.setVisible(true);
        }
   }

    public String getUser(){
        return selectedUser;
    }

    public String getStudentName(){
        return studentName;
    }

}

