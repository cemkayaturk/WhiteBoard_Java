import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.Timer;


public class Teacher extends JFrame {

    public static class Teacher_Server{
        int port;
        ServerSocket server;
        PrintWriter out;
        BufferedReader in;
        Socket socket;
        String name;
        String raise = "";
        // constructor with port
        public Teacher_Server()
        {   try {
            port =5555;
            server = new ServerSocket(5555);
            socket = server.accept();
            System.out.println("Server started");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Waiting for a client ...");
            }
            catch (IOException i){
                System.out.println(i);
            }
        }
        public void sendConfig(String config) {
            try {
                System.out.println("Client accepted");

                out = new PrintWriter(socket.getOutputStream());

                out.println(config);
                out.flush();

            }
            catch (IOException e){
                System.out.println(e);
            }
        }
        public String getName() {
            try{
            name = in.readLine();
            System.out.println(name);
            }catch (IOException e){
                System.out.println("No name");
            }
            if(!name.equals("raise")){
            return name;}
            else{
                return "";}
        }

    }


    int currentAction = 0;
    Color strokeColor = Color.BLACK;
    int currentDrawCount =0;
    int cnt= 10;
    Graphics2D graphSettings;

    Teacher_Server teacher_server = new Teacher_Server();


    public Teacher() {

        String close = "close";

        String studentName = teacher_server.getName();

        this.setSize(800, 600);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();

        int xPos = (dim.width / 2) - (this.getWidth() / 2);
        int yPos = (dim.height / 2) - (this.getHeight() / 2);
        this.setLocation(xPos, yPos);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle("Classroom/Teacher");

        JMenuItem i1, i2, i3, a1 ;
        JMenuBar mb = new JMenuBar();

        a1 = new JMenuItem(studentName);

        i1= new JMenuItem("Line", new ImageIcon("./src/Line.png"));
        i1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentAction = 1;
                System.out.println("actionNum: " + 1);
            }
        });

        i2=new JMenuItem("Ellipse",new ImageIcon("./src/Ellipse.png"));
        i2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentAction = 2;
                System.out.println("actionNum: " + 2);
            }
        });

        i3=new JMenuItem("Rectangle",new ImageIcon("./src/Rectangle.png"));
        i3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentAction = 3;
                System.out.println("actionNum: " + 3);
            }
        });

        JMenu menu1 = new JMenu("Shapes");
        JMenu menu2 = new JMenu("Attanded");
        menu1.add(i1); menu1.add(i2); menu1.add(i3);
        menu2.add(a1);
        mb.add(menu1);
        mb.add(menu2);
        setJMenuBar(mb);

        JTextField jtf1 = new JTextField();
        JTextField jtf2 = new JTextField();
        JLabel lbl1= new JLabel();
        JLabel lbl2= new JLabel();

        ActionListener al=new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(cnt>0) {
                    jtf1.setText(String.format("%s", cnt--));
                    jtf2.setText(String.format("%s", currentDrawCount));
                }
                else {
                    jtf1.setText(String.format("Closing...", cnt--));
                    if(cnt==-5 )
                        System.exit(0);
                }
            }
        };
        Timer timer=new Timer(1000,al);

        this.add(jtf1);

        jtf1.setBounds(700, 500, 80, 20);

        this.add(lbl1);

        lbl1.setText("Remaining Time:");
        lbl1.setBounds(600, 497, 200, 20);

        this.add(jtf2);

        jtf2.setBounds(90, 500, 40, 20);

        this.add(lbl2);

        lbl2.setText("Draw Count:");
        lbl2.setBounds(10, 497, 200, 20);

        this.add(new DrawingBoard(), BorderLayout.CENTER);
        this.setVisible(true);
        timer.start();

    }

    private class DrawingBoard extends JComponent {

        ArrayList<Shape> shapes = new ArrayList<Shape>();
        Point drawStart, drawEnd;


        private DrawingBoard() {
            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if(currentAction!=0){
                    drawStart = new Point(e.getX(), e.getY());
                    drawEnd = drawStart;
                    repaint();
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if (currentAction != 0) {
                        Shape aShape = null;

                        if (currentAction == 1) {
                            aShape = drawLine(drawStart.x, drawStart.y, e.getX(), e.getY());
                        } else if (currentAction == 2) {
                            aShape = drawEllipse(drawStart.x, drawStart.y, e.getX(), e.getY());
                        } else if (currentAction == 3) {
                            aShape = drawRectangle(drawStart.x, drawStart.y, e.getX(), e.getY());
                        }

                        shapes.add(aShape);

                        currentDrawCount++;

                        String config = (currentAction+"-"+drawStart.x+"-"+drawStart.y+"-"+ e.getX()+"-"+ e.getY());
                        teacher_server.sendConfig(config);

                        drawStart = null;
                        drawEnd = null;

                        repaint();
                    }
                }
            });

            this.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    drawEnd = new Point(e.getX(), e.getY());
                    repaint();
                }
            });
        }

        public void paint(Graphics g) {
            graphSettings = (Graphics2D) g;

            graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphSettings.setStroke(new BasicStroke(4));

            for (Shape s : shapes) {
                graphSettings.setPaint(strokeColor);
                graphSettings.draw(s);
            }


            if (drawStart != null && drawEnd != null) {
                graphSettings.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));

                graphSettings.setPaint(Color.LIGHT_GRAY);

                Shape aShape = null;

                if (currentAction == 1) {
                    aShape = drawLine(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
                } else if (currentAction == 2) {
                    aShape = drawEllipse(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
                } else if (currentAction == 3) {
                    aShape = drawRectangle(drawStart.x, drawStart.y, drawEnd.x, drawEnd.y);
                }
                graphSettings.draw(aShape);
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
