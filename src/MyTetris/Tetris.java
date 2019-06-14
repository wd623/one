package MyTetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris
  extends JPanel
{
  private Tetromino tetromino;
  private Tetromino nextOne;
  public static final int ROWS = 20;
  public static final int COLS = 10;
  private Cell[][] wall = new Cell[20][10];
  private int lines;
  private int score;
  public static final int CELL_SIZE = 26;
  private static Image background;
  public static Image I;
  public static Image J;
  public static Image L;
  public static Image S;
  public static Image Z;
  public static Image O;
  public static Image T;
  public static final int FONT_COLOR = 6715289;
  public static final int FONT_SIZE = 32;
  
  static
  {
    try
    {
      background = ImageIO.read(
        Tetris.class.getResource("tetris.png"));
      T = ImageIO.read(Tetris.class.getResource("T.png"));
      I = ImageIO.read(Tetris.class.getResource("I.png"));
      S = ImageIO.read(Tetris.class.getResource("S.png"));
      Z = ImageIO.read(Tetris.class.getResource("Z.png"));
      L = ImageIO.read(Tetris.class.getResource("L.png"));
      J = ImageIO.read(Tetris.class.getResource("J.png"));
      O = ImageIO.read(Tetris.class.getResource("O.png"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void action(){
    startAction();
    repaint();
    KeyAdapter l = new KeyAdapter()
    {
      public void keyPressed(KeyEvent e)
      {
        int key = e.getKeyCode();
        if (key == 81) {
          System.exit(0);
        }
        if (Tetris.this.gameOver)
        {
          if (key == 83) {
            Tetris.this.startAction();
          }
          return;
        }
        if (Tetris.this.pause)
        {
          if (key == 67) {
            Tetris.this.continueAction();
          }
          return;
        }
        switch (key)
        {
        case 39: 
          Tetris.this.moveRightAction(); break;
        case 37: 
          Tetris.this.moveLeftAction(); break;
        case 40: 
          Tetris.this.softDropAction(); break;
        case 38: 
          Tetris.this.rotateRightAction(); break;
     //   case 90: 
     //     Tetris.this.rotateLeftAction(); break;
        case 32: 
          Tetris.this.hardDropAction(); break;
        case 80: 
          Tetris.this.pauseAction();
        }
        Tetris.this.repaint();
      }
    };
    requestFocus();
    addKeyListener(l);
  }
  
  public void paint(Graphics g)
  {
    g.drawImage(background, 0, 0, null);
    g.translate(15, 15);
    paintTetromino(g);
    paintWall(g);
    paintNextOne(g);
    paintScore(g);
  }
  
  private void paintScore(Graphics g)
  {
    Font f = getFont();
    Font font = new Font(
      f.getName(), 1, 32);
    int x = 290;
    int y = 162;
    g.setColor(new Color(6715289));
    g.setFont(font);
    String str = "SCORE:" + this.score;
    g.drawString(str, x, y);
    y += 56;
    str = "LINES:" + this.lines;
    g.drawString(str, x, y);
    y += 56;
    str = "[P]Pause";
    if (this.pause) {
      str = "[C]Continue";
    }
    if (this.gameOver) {
      str = "[S]Start!";
    }
    g.drawString(str, x, y);
  }
  
  private void paintNextOne(Graphics g)
  {
    Cell[] cells = this.nextOne.getCells();
    for (int i = 0; i < cells.length; i++)
    {
      Cell c = cells[i];
      int x = (c.getCol() + 10) * 26 - 1;
      int y = (c.getRow() + 1) * 26 - 1;
      g.drawImage(c.getImage(), x, y, null);
    }
  }
  
  private void paintTetromino(Graphics g)
  {
    Cell[] cells = this.tetromino.getCells();
    for (int i = 0; i < cells.length; i++)
    {
      Cell c = cells[i];
      int x = c.getCol() * 26 - 1;
      int y = c.getRow() * 26 - 1;
      

      g.drawImage(c.getImage(), x, y, null);
    }
  }
  
  private void paintWall(Graphics g)
  {
    for (int row = 0; row < this.wall.length; row++)
    {
      Cell[] line = this.wall[row];
      for (int col = 0; col < line.length; col++)
      {
        Cell cell = line[col];
        int x = col * 26;
        int y = row * 26;
        if (cell != null) {
          g.drawImage(cell.getImage(), x - 1, y - 1, null);
        }
      }
    }
  }
  
  public void softDropAction()
  {
    if (tetrominoCanDrop())
    {
      this.tetromino.softDrop();
    }
    else
    {
      tetrominoLandToWall();
      destroyLines();
      checkGameOver();
      this.tetromino = this.nextOne;
      this.nextOne = Tetromino.randomTetromino();
      
    }
  }
  
  public void destroyLines()
  {
    int lines = 0;
    for (int row = 0; row < this.wall.length; row++) {
      if (fullCells(row))
      {
        deleteRow(row);
        lines++;
      }
    }
    this.lines += lines;
    this.score += SCORE_TABLE[lines];
  }
  
  private static final int[] SCORE_TABLE = { 0, 1, 10, 30, 200 };
  private boolean pause;
  private boolean gameOver;
  private Timer timer;
  
  public boolean fullCells(int row)
  {
    Cell[] line = this.wall[row];
    for (int i = 0; i < line.length; i++) {
      if (line[i] == null) {
        return false;
      }
    }
    return true;
  }
  
  public void deleteRow(int row)
  {
    for (int i = row; i >= 1; i--) {
      System.arraycopy(this.wall[(i - 1)], 0, this.wall[i], 0, 10);
    }
    Arrays.fill(this.wall[0], null);
  }
  
  public boolean tetrominoCanDrop()
  {
    Cell[] cells = this.tetromino.getCells();
    for (int i = 0; i < cells.length; i++)
    {
      Cell cell = cells[i];
      int row = cell.getRow();int col = cell.getCol();
      if (row == 19) {
    	  try {
			finalize();
		} catch (Throwable e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
        return false;
      }
    }
    for (int i = 0; i < cells.length; i++)
    {
      Cell cell = cells[i];
      int row = cell.getRow();
      int col = cell.getCol();
      if (this.wall[(row + 1)][col] != null) {
    	  try {
			finalize();
		} catch (Throwable e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
        return false;
      }
    }
    return true;
  }
  
  public void tetrominoLandToWall()
  {
    Cell[] cells = this.tetromino.getCells();
    for (int i = 0; i < cells.length; i++)
    {
      Cell cell = cells[i];
      int row = cell.getRow();
      int col = cell.getCol();
      this.wall[row][col] = cell;
    }
  }
  
  public void moveRightAction()
  {
    this.tetromino.moveRight();
    if ((outOfBound()) || (coincide())) {
      this.tetromino.moveLeft();
    }
  }
  
  public void moveLeftAction()
  {
    this.tetromino.moveLeft();
    if ((outOfBound()) || (coincide())) {
      this.tetromino.moveRight();
    }
  }
  
  private boolean outOfBound()
  {
    Cell[] cells = this.tetromino.getCells();
    for (int i = 0; i < cells.length; i++)
    {
      Cell cell = cells[i];
      int col = cell.getCol();
      if ((col < 0) || (col >= 10)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean coincide()
  {
    Cell[] cells = this.tetromino.getCells();
    for (Cell cell : cells)
    {
      int row = cell.getRow();
      int col = cell.getCol();
      if ((row < 0) || (row >= 20) || (col < 0) || (col >= 10) 
    		  || (this.wall[row][col] != null)) {
        return true;
      }
    }
    return false;
  }
  
  public void rotateRightAction()
  {
    this.tetromino.rotateRight();
    if ((outOfBound()) || (coincide())) {
      this.tetromino.rotateRight();
    }
}
  
  
 /* public void rotateLeftAction()
  {
    this.tetromino.rotateLeft();
    if ((outOfBound()) || (coincide())) {
      this.tetromino.rotateRight();
    }
  }*/
  
  public void hardDropAction()
  {
    while (tetrominoCanDrop()) {
      this.tetromino.softDrop();
    }
    tetrominoLandToWall();
    destroyLines();
    checkGameOver();
    this.tetromino = this.nextOne;
    this.nextOne = Tetromino.randomTetromino();
  }
  
  public void startAction()
  {
    clearWall();
    this.tetromino = Tetromino.randomTetromino();
    this.nextOne = Tetromino.randomTetromino();
    this.lines = 0;this.score = 0;
    this.pause = false;
    this.gameOver = false;
    this.timer = new Timer();
    this.timer.schedule(new TimerTask()
    {
      public void run()
      {
        Tetris.this.softDropAction();
        Tetris.this.repaint();
      }
    }, 700L, 700L);
  }
  
  private void clearWall()
  {
    for (int row = 0; row < 20; row++) {
      Arrays.fill(this.wall[row], null);
    }
  }
  
  public void pauseAction()
  {
    this.timer.cancel();
    this.pause = true;
    repaint();
  }
  
  public void continueAction()
  {
    this.timer = new Timer();
    this.timer.schedule(new TimerTask()
    {
      public void run()
      {
        Tetris.this.softDropAction();
        Tetris.this.repaint();
      }
    }, 700L, 700L);
    this.pause = false;
    repaint();
  }
  
  public void checkGameOver()
  {
    if (this.wall[0][4] == null) {
      return;
    }
    this.gameOver = true;
    this.timer.cancel();
    repaint();
  }
  
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    Tetris tetris = new Tetris();
    frame.add(tetris);
    frame.setSize(525, 590);
    frame.setUndecorated(false);
    frame.setTitle("俄罗斯方块");
    frame.setDefaultCloseOperation(3);
    
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    tetris.action();
    
    
  }
}



