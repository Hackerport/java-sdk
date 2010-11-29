/*
 * @(#)InterruptedException.java	1.18 10/03/23
 *
 * �������� (c) 2006, Oracle �/��� ��� �������. ��� ����� ��������.
 * ORACLE PROPRIETARY/CONFIDENTIAL. ������������ � ������������ � ���������.
 */

package java.lang;

/**
 * ��������, ����� ����� �������, ���� ��� ����� ���-�� ������,
 * � ����� �����������, ��� ��, ��� �� ����� ����������.
 * ������ ����� ����� �������� ���������, ��� �� ������� ����� �������
 * � ���� ��, �� ���������� ������� ��� ����������.
 * ��������� ��� ����� �������������� ��� ���������� ����� �������:
 * 
 * <pre>
 *  if (Thread.interrupted())  // ������� ������ ����������!
 *      throw new InterruptedException();
 * </pre>
 *
 * @author  Frank Yellin
 * @version 1.18, 03/23/10
 * @see     java.lang.Object#wait()
 * @see     java.lang.Object#wait(long)
 * @see     java.lang.Object#wait(long, int)
 * @see     java.lang.Thread#sleep(long)
 * @see     java.lang.Thread#interrupt()
 * @see     java.lang.Thread#interrupted()
 * @since   JDK1.0
 */
public
class InterruptedException extends Exception {
    /**
     * ������������ <code>InterruptedException</code> ��� ����������� ���������.
     */
    public InterruptedException() {
        super();
    }

    /**
     * ������������ <code>InterruptedException</code> � ��������� 
     * ���������� ����������. 
     *
     * @param   s   ���������� ���������.
     */
    public InterruptedException(String s) {
        super(s);
    }
}
