package com.s6work.jpty;

import com.s6work.jpty.JPty.JPtyInterface;
import com.sun.jna.Native;
import com.sun.jna.StringArray;
import com.sun.jna.Structure;

import jtermios.macosx.JTermiosImpl.MacOSX_C_lib.termios;


/**
 * Provides a {@link JPtyInterface} implementation for MacOSX.
 */
public class JPtyMacImpl implements JPtyInterface
{
  // INNER TYPES

  public interface MacOSX_C_lib extends com.sun.jna.Library
  {
    int execv( String command, StringArray argv );

    int execve( String command, StringArray argv, StringArray env );

    int forkpty( int[] amaster, byte[] name, termios termp, winsize winp );

    int ioctl( int fd, int cmd, winsize data );

    int kill( int pid, int signal );

    int waitpid( int pid, int[] stat, int options );
  }

  public static class winsize extends Structure
  {
    public short ws_row;
    public short ws_col;
    public short ws_xpixel;
    public short ws_ypixel;

    public winsize()
    {
    }

    public winsize( WinSize ws )
    {
      ws_row = ws.ws_row;
      ws_col = ws.ws_col;
      ws_xpixel = ws.ws_xpixel;
      ws_ypixel = ws.ws_ypixel;
    }

    public void update( WinSize winSize )
    {
      winSize.ws_col = ws_col;
      winSize.ws_row = ws_row;
      winSize.ws_xpixel = ws_xpixel;
      winSize.ws_ypixel = ws_ypixel;
    }
  }

  // CONSTANTS

  private static final int TIOCGWINSZ = 0x40087468;
  private static final int TIOCSWINSZ = 0x80087467;

  // VARIABLES

  private static MacOSX_C_lib m_Clib = ( MacOSX_C_lib )Native.loadLibrary( "c", MacOSX_C_lib.class );

  // CONSTUCTORS

  /**
   * Creates a new {@link JPtyImpl} instance.
   */
  public JPtyMacImpl()
  {
    JPty.ONLCR = 0x02;

    JPty.VERASE = 3;
    JPty.VWERASE = 4;
    JPty.VKILL = 5;
    JPty.VREPRINT = 6;
    JPty.VINTR = 8;
    JPty.VQUIT = 9;
    JPty.VSUSP = 10;

    JPty.ECHOKE = 0x01;
    JPty.ECHOCTL = 0x40;
  }

  // METHODS

  @Override
  public int execve( String command, String[] argv, String[] env )
  {
    StringArray argvp = ( argv == null ) ? new StringArray( new String[] { command } ) : new StringArray( argv );
    StringArray envp = ( env == null ) ? null : new StringArray( env );
    return m_Clib.execve( command, argvp, envp );
  }

  @Override
  public int forkpty( int[] amaster, byte[] name, jtermios.Termios term, WinSize win )
  {
    termios termp = ( term == null ) ? null : new termios( term );
    winsize winp = ( win == null ) ? null : new winsize( win );
    return m_Clib.forkpty( amaster, name, termp, winp );
  }

  @Override
  public int getWinSize( int fd, WinSize winSize )
  {
    int r;

    winsize ws = new winsize();
    if ( ( r = m_Clib.ioctl( fd, TIOCGWINSZ, ws ) ) < 0 )
    {
      return r;
    }
    ws.update( winSize );

    return r;
  }

  @Override
  public int kill( int pid, int signal )
  {
    return m_Clib.kill( pid, signal );
  }

  @Override
  public int setWinSize( int fd, WinSize winSize )
  {
    winsize ws = new winsize( winSize );
    return m_Clib.ioctl( fd, TIOCSWINSZ, ws );
  }

  @Override
  public int waitpid( int pid, int[] stat, int options )
  {
    return m_Clib.waitpid( pid, stat, options );
  }
}
