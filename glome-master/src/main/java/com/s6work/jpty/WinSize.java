package com.s6work.jpty;


public class WinSize
{
  /** # of rows, in characters. */
  public short ws_row;
  /** # of columns, in characters. */
  public short ws_col;
  /** width, in pixels (not always supported). */
  public short ws_xpixel;
  /** height, in pixels (not always supported). */
  public short ws_ypixel;

  /**
   * Creates a new, empty, {@link WinSize} instance
   */
  public WinSize()
  {
    this( 0, 0, 0, 0 );
  }

  /**
   * Creates a new {@link WinSize} instance for the given columns and rows.
   */
  public WinSize( int columns, int rows )
  {
    this( columns, rows, 0, 0 );
  }

  /**
   * Creates a new {@link WinSize} instance for the given columns, rows and
   * dimensions.
   */
  public WinSize( int columns, int rows, int width, int height )
  {
    ws_col = ( short )columns;
    ws_row = ( short )rows;
    ws_xpixel = ( short )width;
    ws_ypixel = ( short )height;
  }
}
