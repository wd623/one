package MyTetris;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CellTest
{
  private static Cell cell = new Cell();
  
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
    cell.setCol(5);
    cell.setRow(5);
  }
  
  @After
  public void tearDown()
    throws Exception
  {}
  
  @Test
  public void testMoveRight()
  {
    cell.moveRight();
    Assert.assertEquals(6L, cell.getCol());
  }
  
  @Test
  public void testMoveLeft()
  {
    cell.moveLeft();
    Assert.assertEquals(4L, cell.getCol());
  }
  
  @Test
  public void testMoveDown()
  {
    cell.moveDown();
    
    Assert.assertEquals(6L, cell.getRow());
  }
}
