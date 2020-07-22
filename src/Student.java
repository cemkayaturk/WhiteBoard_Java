import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.net.*;
import java.io.*;




public class Student extends JFrame {

    int currentDrawCount =0;
    int cnt= 40;
    String name;

    public static class Student_Client {
        String host;
        String config = "";
        BufferedReader in;
        PrintWriter out;
        Socket socket;

        Student_Client() {
            try {
                host = "localhost";

                socket = new Socket(host, 5555);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());

            } catch (IOException e) {
                System.out.println("Connection Error");
            }
        }

        public String getConfig(){
            try {
                socket = new Socket(host, 5555);
                config = in.readLine();
                System.out.println(config);
            }catch (IOException e){
                config = "";
                System.out.println("Connection Error");
            }
            return config;
        }

        public void sendName(String name){
            try {
                out.println(name);
                out.flush();
                socket.close();
            }catch (IOException e){
                System.out.println("cannot send name");
            }

        }

        public void raiseHand(){
                out.println("raise");
                out.flush();
        }

    }


    Student.Student_Client student_client = new Student.Student_Client();

    public Student(String studentName){


        student_client.sendName(studentName);

        this.setSize(800, 600);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();

        int xPos = (dim.width / 2) - (this.getWidth() / 2);
        int yPos = (dim.height / 2) - (this.getHeight() / 2);
        this.setLocation(xPos, yPos);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Classroom/Student");

        JTextField jtf1 = new JTextField();
        JTextField jtf2 = new JTextField();
        JLabel lbl1= new JLabel();
        JLabel lbl2= new JLabel();

        jtf1.setBounds(700, 500, 80, 20);
        this.add(jtf1);
        jtf1.setVisible(true);

        lbl1.setText("Remaining Time:");
        lbl1.setBounds(600, 497, 200, 20);
        this.add(lbl1);
        lbl1.setVisible(true);

        jtf2.setBounds(90, 500, 40, 20);
        this.add(jtf2);
        jtf2.setVisible(true);

        lbl2.setText("Draw Count:");
        lbl2.setBounds(10, 497, 200, 20);
        this.add(lbl2);
        lbl2.setVisible(true);

        ActionListener al=new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(cnt>0) {
                    jtf1.setText(String.format("%s", cnt--));
                    jtf2.setText(String.format("%s", currentDrawCount));
                    repaint();
                }
                else {
                    jtf1.setText(String.format("Closing...", cnt--));
                    if(cnt==-5 )
                        System.exit(0);
                }
            }
        };
        Timer timer=new Timer(1000,al);
        timer.start();

        this.add(new DrawingBoard(), BorderLayout.CENTER);
        this.setVisible(true);



    }

    private class DrawingBoard extends JComponent {

        Graphics2D graphSettings;
        Color strokeColor = Color.BLACK;

        ArrayList<Shape> shapes = new ArrayList<Shape>();

        private DrawingBoard(){

            ActionListener dl=new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    Shape aShape = null;
                    String[] tokens = new String[0];
                    tokens = (student_client.getConfig()).split("-");
                    if (Integer.parseInt(tokens[0]) == 1) {
                        aShape = drawLine(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                    } else if (Integer.parseInt(tokens[0]) == 2) {
                        aShape = drawEllipse(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                    } else if (Integer.parseInt(tokens[0]) == 3) {
                        aShape = drawRectangle(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                    }
                    currentDrawCount++;
                    shapes.add(aShape);
                    repaint();
                }
            };
            Timer timer2 = new Timer(100,dl);
            timer2.start();

        }



        public void paint(Graphics g) {
            graphSettings = (Graphics2D) g;

            graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphSettings.setStroke(new BasicStroke(4));



            for (Shape s : shapes) {
                graphSettings.setPaint(strokeColor);
                graphSettings.draw(s);
            }
        }

        private Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2) {
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);

            int width = Math.abs(x1 - x2);
            int height = Math.abs(y1 - y2);

            return new Rectangle2D.Float(x, y, width, height);
        }

        private Ellipse2D.Float drawEllipse(int x1, int y1, int x2, int y2) {
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);
            int width = Math.abs(x1 - x2);
            int height = Math.abs(y1 - y2);

            return new Ellipse2D.Float(x, y, width, height);
        }

        private Line2D.Float drawLine(int x1, int y1, int x2, int y2) {
            return new Line2D.Float(x1, y1, x2, y2);
        }
    }
}
