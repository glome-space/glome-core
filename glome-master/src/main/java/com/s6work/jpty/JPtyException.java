package com.s6work.jpty;


/**
 * Exception instance for JPty specific exceptions.
 */
@SuppressWarnings("serial")
public class JPtyException extends RuntimeException
{
  // VARIABLES

  private final int m_errno;

  // CONSTRUCTORS

  /**
   * Creates a new {@link JPtyException} instance with the given error number.
   * 
   * @param errno
   *          the error number providing more details on the exact problem.
   */
  public JPtyException( int errno )
  {
    m_errno = errno;
  }

  /**
   * Creates a new {@link JPtyException} instance with the given message and
   * error number.
   * 
   * @param message
   *          the message for this exception, can be <code>null</code>;
   * @param errno
   *          the error number providing more details on the exact problem.
   */
  public JPtyException( String message, int errno )
  {
    super( message );
    m_errno = errno;
  }

  // METHODS

  /**
   * Returns the error number.
   * 
   * @return the err number, > 0, or -1 if not defined.
   */
  public final int getErrno()
  {
    return m_errno;
  }
}
