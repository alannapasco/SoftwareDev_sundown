package com.fish.view;

import com.fish.common.Coord;
import com.fish.common.board.GameBoard;
import com.fish.common.board.HexGameBoard;
import com.fish.common.state.GameState;
import com.fish.common.state.HexGameState;
import com.fish.common.state.HexPlayer;
import com.fish.common.state.InternalPlayer;
import com.fish.common.state.PlayerColor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class HexBoardViewTest {

  private GameBoard board;
  private HexBoardView hbv;

  @Before
  public void setUp() throws Exception {
    this.board = new HexGameBoard(4, 5,
        Arrays.asList(new Coord(0, 1), new Coord(0, 2), new Coord(3, 3)),
        4, 1);

    GameState state = new HexGameState();
    List<InternalPlayer> players = Arrays.asList(new HexPlayer(PlayerColor.BROWN), new HexPlayer(PlayerColor.BLACK));
    state.initGame(this.board, players);
    this.hbv = new HexBoardView(state);
  }

  @Test
  public void testCalculateTopLeftCorner() {
    //We know that PIXEL_STEP is set to 50 for now
    int step = 50;
    Assert.assertEquals(step, this.hbv.calculateTopLeftXValue(new Coord(0, 0)));
    Assert.assertEquals(step * 15, this.hbv.calculateTopLeftXValue(new Coord(3, 3)));
    Assert.assertEquals(step * 37,  this.hbv.calculateTopLeftXValue(new Coord(9, 12)));
  }

  @Test
  public void testGetWindowDimension() {
    //We know that PIXEL_STEP is set to 50 for now
    int step = 50;
    Assert.assertEquals(new Dimension(1050, 250), this.hbv.calculateWindowSize());
  }
}