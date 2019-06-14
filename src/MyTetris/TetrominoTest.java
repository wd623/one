package MyTetris;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TetrominoTest
{
  private static Tetromino tetromino = new Tetromino();
  
  @BeforeClass
  public static void setUpBeforeClass()
    throws Exception
  {}
  
  @AfterClass
  public static void tearDownAfterClass()
    throws Exception
  {}
  
  @Before
  public void setUp()
    throws Exception
  {
    tetromino.cells[0] = new Cell(2, 4, Tetris.J);
    tetromino.cells[1] = new Cell(2, 3, Tetris.J);
    tetromino.cells[2] = new Cell(2, 5, Tetris.J);
    tetromino.cells[3] = new Cell(3, 5, Tetris.J);
  }
  
  @After
  public void tearDown()
    throws Exception
  {}
  
  @Test
  public void testSoftDrop()
  {
    tetromino.softDrop();
    Assert.assertEquals(3L, tetromino.cells[0].getRow());
    Assert.assertEquals(3L, tetromino.cells[1].getRow());
    Assert.assertEquals(3L, tetromino.cells[2].getRow());
    Assert.assertEquals(4L, tetromino.cells[3].getRow());
  }
  
  @Test
  public void testMoveRight()
  {
    tetromino.moveRight();
    Assert.assertEquals(5L, tetromino.cells[0].getCol());
    Assert.assertEquals(4L, tetromino.cells[1].getCol());
    Assert.assertEquals(6L, tetromino.cells[2].getCol());
    Assert.assertEquals(6L, tetromino.cells[3].getCol());
  }
  
  @Test
  public void testMoveLeft()
  {
    tetromino.moveLeft();
    Assert.assertEquals(3L, tetromino.cells[0].getCol());
    Assert.assertEquals(2L, tetromino.cells[1].getCol());
    Assert.assertEquals(4L, tetromino.cells[2].getCol());
    Assert.assertEquals(4L, tetromino.cells[3].getCol());
  }
  
  @Test
  public void testRotateRight()
  {
    tetromino.rotateRight();
    
    Assert.assertEquals(2L, tetromino.cells[0].getRow());
    Assert.assertEquals(4L, tetromino.cells[0].getCol());
    Assert.assertEquals(1L, tetromino.cells[1].getRow());
    Assert.assertEquals(4L, tetromino.cells[1].getCol());
    Assert.assertEquals(3L, tetromino.cells[2].getRow());
    Assert.assertEquals(4L, tetromino.cells[2].getCol());
    Assert.assertEquals(3L, tetromino.cells[3].getRow());
    Assert.assertEquals(3L, tetromino.cells[3].getCol());
  }
}