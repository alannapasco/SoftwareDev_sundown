package com.fish.common.board;

import com.fish.common.Coord;
import com.fish.common.tile.ProtectedTile;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HexGameBoardTest {

  private GameBoard noHolesBoard;
  private GameBoard holesBoard;
  private GameBoard constantFishNumBoard;

  @Before
  public void setUp() throws Exception {
    this.noHolesBoard = new HexGameBoard(6, 2, new ArrayList<>(),
        5, 1);

    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    this.holesBoard = new HexGameBoard(8, 3, holes,
        8, 1);

    this.constantFishNumBoard = new HexGameBoard(4, 4, 2);

  }

  /////Tests for Constructors
  @Test
  public void testBoardDataStructure() {
    //6 ROWS x 2 COLS board ; but the data is 2 ROWS x 6 COLS
    int[][] output = this.noHolesBoard.getBoardDataRepresentation();

    int[][] valuesOfTiles =
        {{1, 1, 1, 5, 4, 1},
            {1, 4, 5, 4, 5, 2}};

    assertArrayEquals(valuesOfTiles, output);
  }

  @Test
  public void testBoardDataStructureInputMatchesOutput() {
    //create a 2-d Array in our Coord structure
    int[][] valuesOfTiles =
        {{1, 1, 1, 5, 4, 1},
            {1, 4, 5, 4, 5, 2}};

    //Use the convenience constructor made for the test harness to construct the board
    GameBoard tHarnessBoard = new HexGameBoard(valuesOfTiles);
    int[][] output = tHarnessBoard.getBoardDataRepresentation();

    //This confirms that our data structure is as we designed it
    assertArrayEquals(valuesOfTiles, output);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeArgument() {
    GameBoard boardr = new HexGameBoard(-3, 2, new ArrayList<>(),
        5, 1);
    GameBoard boardc = new HexGameBoard(3, -2, new ArrayList<>(),
        5, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstantConstructorNegativeArgument() {
    GameBoard boardr = new HexGameBoard(4, -4, 2);
    GameBoard boardc = new HexGameBoard(-4, 4, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstantConstructorTooManyFish() {
    GameBoard tooManyFish = new HexGameBoard(4, 4, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNotEnoughSpacesForOneFishTiles() {
    GameBoard board = new HexGameBoard(3, 2, new ArrayList<>(),
        20, 1);
  }

  @Test
  public void testMinimumNumberOneFish() {
    int minOneFish = 0;
    for (int ii = 0; ii < this.noHolesBoard.getWidth(); ii++) {
      for (int jj = 0; jj < this.noHolesBoard.getHeight(); jj++) {
        if (this.noHolesBoard.getTileAt(new Coord(ii, jj)).isPresent() &&
            this.noHolesBoard.getTileAt(new Coord(ii, jj)).getNumFish() == 1) {
          minOneFish++;
        }
      }
    }
    assertTrue(minOneFish >= 5);

    minOneFish = 0;
    for (int ii = 0; ii < this.holesBoard.getWidth(); ii++) {
      for (int jj = 0; jj < this.holesBoard.getHeight(); jj++) {
        if (this.holesBoard.getTileAt(new Coord(ii, jj)) != null &&
            this.holesBoard.getTileAt(new Coord(ii, jj)).getNumFish() == 1) {
          minOneFish++;
        }
      }
    }
    assertTrue(minOneFish >= 8);
  }

  // xBoard constructor tests
  @Test(expected = IllegalArgumentException.class)
  public void testXBoardBadInput() {
    GameBoard board = new HexGameBoard(new int[0][1]);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testXBoardBadInputRow() {
    GameBoard board = new HexGameBoard(new int[2][0]);

  }

  @Test
  public void testXBoardConvenienceConstructor() {
    int[][] nums =
        {{2, 0},
         {3, 4},
         {5, 1}};
    GameBoard board = new HexGameBoard(nums);

    //Check dimensions
    assertEquals(2, board.getHeight());
    assertEquals(3, board.getWidth());
    //Check a hole
    assertFalse(board.getTileAt(new Coord(0, 1)).isPresent());
    //check a Tile
    assertEquals(2, board.getTileAt(new Coord(0, 0)).getNumFish());
  }


  /////Tests for Moves
  @Test
  public void getTilesReachableFrom() {
    GameBoard noHolesExpanded = new HexGameBoard(8, 3, 1);

    List<Coord> expectedMoves = new ArrayList<>(Arrays.asList(
            new Coord(0, 0), new Coord(0, 1), new Coord(1, 2),
            new Coord(2, 4), new Coord(2, 5),
            new Coord(1, 1), new Coord(1, 5), new Coord(1, 7),
            new Coord(2, 2), new Coord(2, 1),
            new Coord(1, 4), new Coord(0, 5), new Coord(0, 6)));

    List<Coord> actualMoves = noHolesExpanded.getTilesReachableFrom(new Coord(1, 3), new ArrayList<>());


    assertEquals(13, actualMoves.size());
    for (Coord c : expectedMoves) {
      assertTrue(actualMoves.contains(c));
    }

    // remove a tile and test it still works
    noHolesExpanded.removeTileAt(new Coord(1,2));
    actualMoves = noHolesExpanded.getTilesReachableFrom(new Coord(1, 3), new ArrayList<>());
    expectedMoves.remove(new Coord(1, 2));
    expectedMoves.remove(new Coord(0, 1));
    expectedMoves.remove(new Coord(0, 0));
    for (Coord c : expectedMoves) {
      assertTrue(actualMoves.contains(c));
    }
    assertEquals(10, actualMoves.size());
  }

  @Test
  public void getTilesReachableFromWithPenguin() {
    GameBoard noHolesExpanded = new HexGameBoard(8, 3, 1);

    List<Coord> expectedMoves = new ArrayList<>(Arrays.asList(
            new Coord(2, 4), new Coord(2, 5),
            new Coord(1, 1), new Coord(1, 5), new Coord(1, 7),
            new Coord(2, 2), new Coord(2, 1),
            new Coord(1, 4), new Coord(0, 5), new Coord(0, 6)));

    List<Coord> actualMoves = noHolesExpanded.getTilesReachableFrom(new Coord(1, 3),
        Collections.singletonList(new Coord(1, 2)));

    for (Coord c : expectedMoves) {
      assertTrue(actualMoves.contains(c));
    }
    assertEquals(10, actualMoves.size());
  }

  @Test
  public void testGetTilesReachableMoreRemoved() {
    List<Coord> moves = this.holesBoard.getTilesReachableFrom(new Coord(1, 2), new ArrayList<>());
    assertEquals(7, moves.size());
  }


  /////Tests for Tile Handling
  @Test
  public void getTileAtValid() {
    assertEquals(3, this.holesBoard.getTileAt(new Coord(0, 1)).getNumFish());
    assertTrue(this.holesBoard.getTileAt(new Coord(0, 1)).isPresent());
    assertEquals(1, this.holesBoard.getTileAt(new Coord(1, 0)).getNumFish());
    assertTrue(this.holesBoard.getTileAt(new Coord(1, 0)).isPresent());

    assertEquals(1, this.noHolesBoard.getTileAt(new Coord(0, 5)).getNumFish());
    noHolesBoard.removeTileAt(new Coord(0,5));
    assertEquals(1, this.noHolesBoard.getTileAt(new Coord(0, 5)).getNumFish());
    assertFalse(this.noHolesBoard.getTileAt(new Coord(0, 5)).isPresent());

    ProtectedTile immutable = this.noHolesBoard.getTileAt(new Coord(1,1));
    //Test that the getters can be called on the retrieved tile:
    assertTrue(immutable.isPresent());
    //But note that java will not compile if you try to mutate it (hence its commented out)
    //mmutable.meltTile();

    //Note that java will also not compile if you try to assign a mutable Tile object the result
    //of this method:
    //Tile mutable = this.noHolesBoard.getTileAt(new Coord(1,1));
  }

  @Test
  public void testAllTilesSameValue() {
    for (int ii = 0; ii < this.constantFishNumBoard.getWidth(); ii++) {
      for (int jj = 0; jj < this.constantFishNumBoard.getHeight(); jj++) {
        assertEquals(2, this.constantFishNumBoard.getTileAt(new Coord(ii, jj)).getNumFish());
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTileOutOfRange() {
    this.noHolesBoard.getTileAt(new Coord(10, 10));
  }

  @Test
  public void testGetTileRemoved() {
    assertFalse(this.holesBoard.getTileAt(new Coord(1, 4)).isPresent());
  }

  @Test
  public void removeTileAt() {
    assertEquals(3, this.holesBoard.removeTileAt(new Coord(0, 1)).getNumFish());
    assertEquals(5, this.holesBoard.removeTileAt(new Coord(0, 6)).getNumFish());
    assertEquals(3, this.holesBoard.removeTileAt(new Coord(2, 7)).getNumFish());

    //Note that java will not compile if you try to mutate the return value of this method:
    ProtectedTile immutable = this.noHolesBoard.removeTileAt(new Coord(0,0));
    //immutable.meltTile();
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileNegX() {
    this.holesBoard.removeTileAt(new Coord(-1, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileNegY() {
    this.holesBoard.removeTileAt(new Coord(0, -1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileTooBigY() {
    this.holesBoard.removeTileAt(new Coord(9, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileTooBigX() {
    this.holesBoard.removeTileAt(new Coord(1, 9));
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeTileAtAlreadyGone() {
    this.holesBoard.removeTileAt(new Coord(0, 0));
  }



  /////Tests for basic getters and helper methods

  @Test
  public void testGetWidth() {
    assertEquals(2, this.noHolesBoard.getWidth());
    assertEquals(4, this.constantFishNumBoard.getWidth());
  }

  @Test
  public void testGetHeight() {
    assertEquals(6, this.noHolesBoard.getHeight());
    assertEquals(4, this.constantFishNumBoard.getHeight());
  }


  @Test
  public void testEqualsNotCopy() {
    List<Coord> holes = Arrays.asList(new Coord(0, 0), new Coord(1, 1),
        new Coord(2, 2), new Coord(1, 4));
    GameBoard gb = new HexGameBoard(8, 3, holes,
        8, 1);
    assertEquals(gb, this.holesBoard);

    gb.removeTileAt(new Coord(0, 1));
    assertNotEquals(gb, this.holesBoard);
    this.holesBoard.removeTileAt(new Coord(0, 1));
    assertEquals(gb, this.holesBoard);
  }


}
