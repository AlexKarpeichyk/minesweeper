package cw.minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Minesweeper_GUI implements ActionListener
{
    private int score, secs = 0;
    private int flags;
    private int r = 9;
    private int c = 9;
    private int m = 10;
    private int looser = 0;
    private final JFrame game = new JFrame("MS");
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu level = new JMenu("Difficulty");
    private final JMenu extras = new JMenu("Extras");
    private final JMenuItem beginner = new JMenuItem("Beginner");
    private final JMenuItem medium = new JMenuItem("Intermediate");
    private final JMenuItem expert = new JMenuItem("Expert");
    private final JMenuItem custom = new JMenuItem("Custom");
    private final JMenuItem bonus = new JMenuItem("Bonus");
    private final JMenuItem controls = new JMenuItem("EpicHowTo");
    private JPanel infoBar, mineField;
    private final JPanel play = new JPanel();
    private final JPanel timePanel = new JPanel();
    private final JPanel end = new JPanel();
    private Timer time;
    private JButton[][] bTiles;
    private JLabel[][] lTiles;
    private JLabel insult, bored, bye;
    private final JLabel timer = new JLabel();
    private final JLabel flagCount = new JLabel();
    private final JLabel scoreCount = new JLabel("Score:" + score);
    private final JButton smiley = new JButton();    
    private final ImageIcon bomb = new ImageIcon(getClass().getResource("bomb.png"));
    private final ImageIcon normal = new ImageIcon(getClass().getResource("normal.png"));
    private final ImageIcon win = new ImageIcon(getClass().getResource("win.png"));
    private final ImageIcon loose = new ImageIcon(getClass().getResource("dead.png"));
    private final InputStream song = getClass().getResourceAsStream("song.wav");
    private boolean dead, victory = false;
    private Minefield mf;
    private final DecimalFormat format = new DecimalFormat("000");
    
    public static void main(String[] args)
    {
        Minesweeper_GUI ms;
        ms = new Minesweeper_GUI();
    }
    
    public Minesweeper_GUI()
    {
        create();
    }
    
    private void create()
    {
        setMineField();
        setInfoBar();        
        setGame();
    }
    
    public void setGame()
    {
        game.setLayout(new BorderLayout());
        setMenu();
        game.setJMenuBar(menuBar);
        game.add(infoBar, BorderLayout.CENTER);
        game.add(mineField, BorderLayout.SOUTH);
        game.pack();
        game.setResizable(false);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setLocationRelativeTo(null);
        game.setVisible(true);      
    }
    
    public void setMenu()
    {
        beginner.addActionListener(this);
        medium.addActionListener(this);
        expert.addActionListener(this);
        custom.addActionListener(this);
        controls.addActionListener(this);
        bonus.addActionListener(this);
        level.add(beginner);  
        level.add(medium);
        level.add(expert);
        level.add(custom);
        extras.add(controls);
        extras.add(bonus);                
        menuBar.add(level);
        menuBar.add(extras);
    }

    public void setInfoBar() {        
        infoBar = new JPanel();
        infoBar.setBorder(BorderFactory.createEtchedBorder());      
        setPlayPanel();
        infoBar.add(play);        
        smiley.setIcon(normal);
        smiley.setPreferredSize(new Dimension(40, 40));
        smiley.setFocusPainted(false);
        smiley.setBackground(Color.LIGHT_GRAY);
        smiley.addActionListener((ActionEvent e) -> {
            gameReset();
        });
        infoBar.add(smiley);  
        setTimePanel();
        infoBar.add(timePanel);
    }
    
    public void gameReset()
    {
        secs = 0;
        time.stop();
        timer.setText(format.format(secs));         
        smiley.setIcon(normal);         
        victory = false;
        dead = false;
        mineField.removeAll();
        resetField();
        flags = mf.getMines();
        flagCount.setText("Flags:" + flags);
        mineField.revalidate();         
    }
    
    public void resetField()
    {
        mf = new Minefield(r, c, m);
        mf.populate();
        game.setSize(25*mf.getColumns()+40, 25*mf.getRows()+125);
        mineField.setLayout(new GridLayout(mf.getRows(), mf.getColumns()));
        bTiles = new JButton[mf.getRows()][mf.getColumns()];
        lTiles = new JLabel[mf.getRows()][mf.getColumns()];
        for(int i = 0; i < mf.getRows(); i++) {
            for(int n = 0; n < mf.getColumns(); n++) {
                lTiles[i][n] = new JLabel();
                if(mf.getNeighbours(i, n) != 0) {
                    lTiles[i][n].setText(Integer.toString(mf.getNeighbours(i, n)));
                }
                lTiles[i][n].setHorizontalAlignment(JLabel.CENTER);
                lTiles[i][n].setOpaque(true);
                numColor(i, n);
                lTiles[i][n].setBackground(new Color(255, 255, 255));
                lTiles[i][n].setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
                lTiles[i][n].setLayout(new BorderLayout());
                lTiles[i][n].add(bTiles[i][n] = new JButton(""), BorderLayout.CENTER);
                lTiles[i][n].setPreferredSize(new Dimension(25, 25));
                bTiles[i][n].setBackground(new Color(170, 170, 170));
                bTiles[i][n].addMouseListener(new MouseHandler(i, n));
                mineField.add(lTiles[i][n]);
            }
        }        
    }
    
    public void setPlayPanel()
    {
        flags = mf.getMines(); 
        play.setLayout(new BoxLayout(play, BoxLayout.Y_AXIS));
        play.setPreferredSize(new Dimension(85, 43));
        play.setBorder(BorderFactory.createEtchedBorder()); 
        play.add(flagCount);
        flagCount.setText("Flags:" + flags);
        flagCount.setBorder(BorderFactory.createEmptyBorder(2, 6, 3, 0));
        flagCount.setFont(new Font("Courier New", Font.BOLD, 14));
        play.add(scoreCount);
        scoreCount.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        scoreCount.setFont(new Font("Courier New", Font.BOLD, 14));
    }
    
    public void setTimePanel()
    {        
        timePanel.setBorder(BorderFactory.createEtchedBorder());
        timePanel.setPreferredSize(new Dimension(85, 43));
        timePanel.setBackground(Color.black);
        timer.setFont(new Font("OCR A Extended", Font.BOLD, 24));
        timer.setForeground(Color.red);
        timePanel.add(timer);        
        time = new Timer(1000, (ActionEvent e) -> {
            if(e.getSource() == time) {
                secs++;
                if(secs == 999) {
                    time.stop();
                }
                timer.setText(format.format(secs));
            }            
        });          
    }
    
    public void setMineField() {
        mf = new Minefield(9, 9, 10);
        mf.populate();
        //game.setSize(25*mf.getColumns()+40, 25*mf.getRows()+125);
        game.pack();
        mineField = new JPanel();
        mineField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mineField.setLayout(new GridLayout(mf.getRows(), mf.getColumns()));
        bTiles = new JButton[mf.getRows()][mf.getColumns()];
        lTiles = new JLabel[mf.getRows()][mf.getColumns()];
        for(int i = 0; i < mf.getRows(); i++) {
            for(int n = 0; n < mf.getColumns(); n++) {
                lTiles[i][n] = new JLabel();
                if(mf.getNeighbours(i, n) != 0) {
                    lTiles[i][n].setText(Integer.toString(mf.getNeighbours(i, n)));
                }
                lTiles[i][n].setHorizontalAlignment(JLabel.CENTER);                
                lTiles[i][n].setOpaque(true);
                numColor(i, n);
                lTiles[i][n].setBackground(new Color(255, 255, 255));
                lTiles[i][n].setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
                lTiles[i][n].setLayout(new BorderLayout());
                lTiles[i][n].add(bTiles[i][n] = new JButton(""), BorderLayout.CENTER);
                lTiles[i][n].setPreferredSize(new Dimension(25, 25));
                bTiles[i][n].setBackground(new Color(170, 170, 170));
                bTiles[i][n].addMouseListener(new MouseHandler(i, n));
                mineField.add(lTiles[i][n]);
            }
        }
    }
    
    public void stepTile(int row, int column)
    {
        if(!mf.isMined(row, column)) {
            if(mf.getNeighbours(row, column) == 0) {
                bTiles[row][column].setVisible(false);
                for(int i = row - 1; i <= row + 1; i++) {
                    for(int j = column - 1; j <= column + 1; j++) {
                        if(i >= 0 && i < mf.getRows() && j >= 0 && j < mf.getColumns()) {    
                            if(!mf.isRevealed(i, j) && !mf.isMarked(i, j) && bTiles[i][j].isVisible()) {
                                stepTile(i, j);
                            }
                        }
                    }
                }
            } else {
                bTiles[row][column].setVisible(false);
                bTiles[row][column].invalidate();                
            }            
        } else {
            for(int i = 0; i < mf.getRows(); i++) {
                for(int n = 0; n < mf.getColumns(); n++) {
                    if(mf.isMined(i, n)) {
                        bTiles[i][n].setBackground(new Color(240, 155, 155));
                        bTiles[i][n].setIcon(bomb);
                        smiley.setIcon(loose);
                        time.stop();
                        dead = true;
                    }
                }
            }
            looser++;
            if(looser % 3 == 0) {
                insult = new JLabel("You really suck at this game, don't you? =)");
                insult.setFont(new Font("Courier New", Font.BOLD, 16));
                JOptionPane.showMessageDialog(null, insult, "Friendly Host", JOptionPane.PLAIN_MESSAGE);
            } else if(looser == 5) {                                
                bored = new JLabel("I'm bored... BYE!");                
                end.setLayout(new BorderLayout());
                bored.setFont(new Font("Courier New", Font.BOLD, 16));
                bye = new JLabel(new ImageIcon(getClass().getResource("bye.jpg")));                
                end.add(bored, BorderLayout.PAGE_START);
                end.add(bye, BorderLayout.PAGE_END);
                JOptionPane.showConfirmDialog(null, end, "Friendly Host", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                game.dispose();
            }
        } 
    }
    
    public String numColor(int row, int column) 
    {
        String s = lTiles[row][column].getText();
        String color = "";
        switch (s) {
            case "1":
                lTiles[row][column].setForeground(Color.blue);
                break;
            case "2":
                lTiles[row][column].setForeground(new Color(0, 155, 18));
                break;
            case "3":
                lTiles[row][column].setForeground(Color.red);
                break;
            case "4":
                lTiles[row][column].setForeground(Color.black);
                break;
            case "5":
                lTiles[row][column].setForeground(new Color(135, 0, 132));
                break;
            case "6":
                lTiles[row][column].setForeground(Color.gray);
                break;
            case "7":
                lTiles[row][column].setForeground(new Color(145, 153, 0));
                break;
            case "8":
                lTiles[row][column].setForeground(new Color(0, 128, 132));
                break;
            default:
                break;
        }
        return color;
    }
    
    public void areAllMinesRevealed()
    {
        if(mf.areAllMinesRevealed()) {
            smiley.setIcon(win);
            victory = true;
            score++;
            looser = 0;
            time.stop();
            scoreCount.setText("Score:" + score);
            try {
                InputStream inputStream = song;
                AudioStream audioStream = new AudioStream(inputStream);
                AudioPlayer.player.start(audioStream);
            } catch (IOException e) {
            }
        }
    }
    
    private class MouseHandler extends MouseAdapter
    {
        private final int row, column;
        
        public MouseHandler(int row, int column) 
        {
            this.row = row;
            this.column = column;
        }
        
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if(!dead && !victory) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    time.start();
                    if(!mf.isMarked(row, column)) {
                        stepTile(row, column);
                        mf.step(row, column);
                    }                    
                }
                else if(e.getButton() == MouseEvent.BUTTON3)
                {    
                    time.start();
                    mf.markTile(row, column);
                    if(mf.isMarked(row, column) && !mf.isRevealed(row, column)) {
                        ImageIcon flag = new ImageIcon(getClass().getResource("flag.png"));
                        bTiles[row][column].setIcon(flag);
                        flags--;
                        flagCount.setText("Flags:" + flags);                        
                    } else {
                        bTiles[row][column].setIcon(null);
                        flags++;
                        flagCount.setText("Flags:" + flags); 
                    }                    
                }
                areAllMinesRevealed();                
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JMenuItem lvl = (JMenuItem) e.getSource();
        JMenuItem extra = (JMenuItem) e.getSource();
        if(lvl == beginner) {
            c = 9;
            r = 9;
            m = 10;
            gameReset();
        } else if(lvl == medium) {
            c = 16;
            r = 16;
            m = 40;
            gameReset();
        } else if(lvl == expert) {
            r = 16;
            c = 30;
            m = 99;
            gameReset();
        } else if(lvl == custom) {
            JPanel rcm = new JPanel();
            JTextField rows = new JTextField();
            JTextField columns = new JTextField();
            JTextField mines = new JTextField();
            rcm.setLayout(new GridLayout(3, 2));
            rcm.add(new JLabel("Rows (8 - 35):"));
            rcm.add(rows);
            rcm.add(new JLabel("Cols (8 - 50):"));
            rcm.add(columns);
            rcm.add(new JLabel("Mines (1 - 550):"));
            rcm.add(mines);
            int result = JOptionPane.showConfirmDialog(null, rcm, "Custom", JOptionPane.OK_CANCEL_OPTION);
            if(result == JOptionPane.OK_OPTION) {
                if(isInteger(rows.getText()) && isInteger(columns.getText()) && isInteger(mines.getText())) {
                    if(!rows.getText().equals("") && !mines.getText().equals("") && !columns.getText().equals("")) {
                        int rs = Integer.parseInt(rows.getText());
                        int cs = Integer.parseInt(columns.getText());
                        int ms = Integer.parseInt(mines.getText()); 
                        r = Integer.parseInt(rows.getText());
                        c = Integer.parseInt(columns.getText());
                        m = Integer.parseInt(mines.getText());
                        if(rs >= 8 && cs >= 8 && rs <= 35 && cs <= 50) {
                            if(ms >= 1 && ms <= 550) {
                                gameReset();
                            } 
                        }
                    }
                }
            }
        } else if(extra == controls) {
            JLabel howTo = new JLabel("<html> &nbsp&nbsp How to play:<br>"
                    + "<br>1. Click tiles to play the game.<br>"
                    +     "2. Click smiley face to restart.<br>"
                    +     "3. Go to 'Difficulty' to change, emm... you know... difficulty.<br>"
                    +     "4. Be gentle with it...<br>"
                    +     "5. Don't freak out too much if can't win..<br>"
                    + "<br> &nbsp&nbsp Happy Playing!<br>"
                    + "<br> &nbsp&nbsp Love, Friendly Host</html>");
            howTo.setFont(new Font("Courier New", Font.BOLD, 16));
            JOptionPane.showMessageDialog(null, howTo, "Friendly Host", JOptionPane.PLAIN_MESSAGE);
        } else if(extra == bonus) {
            JLabel eEggs = new JLabel("<html> &nbsp&nbsp Game's Easter Eggs:<br>"
                    + "<br>1. Win a game for a surprise!<br>"
                    +     "2. Loose 3 times in a row for a friendly insult...<br>"
                    +     "3. Loose 5 times in a row for another surprise!<br>"
                    + "<br> &nbsp&nbsp Love, Friendly Host </html>");
            eEggs.setFont(new Font("Courier New", Font.BOLD, 16));
            JOptionPane.showMessageDialog(null, eEggs, "Friendly Host", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    public boolean isInteger(String s)
    {
        for(int i = 0; i < s.length(); i++) {
            if(!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}